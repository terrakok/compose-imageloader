package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.util.copyTo
import com.seiko.imageloader.util.toByteReadChannel
import io.github.aakira.napier.Napier

class DiskCacheInterceptor(
    private val diskCache: Lazy<DiskCache>,
) : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        Napier.d(tag = "Interceptor") { "intercept CacheInterceptor" }

        val request = chain.request
        val components = chain.components
        val options = chain.options

        val data = request.data
        val cacheKey = components.key(data, options) ?: return chain.proceed(request)

        val diskCache = diskCache.value
        val diskCacheValue = diskCache[cacheKey]
        if (diskCacheValue != null) {
            Napier.d(tag = "Interceptor") { "read disk cache data:$data" }
            return SourceResult(
                request = request,
                source = diskCache.fileSystem.source(diskCacheValue.data).toByteReadChannel(),
            )
        }

        val result = chain.proceed(request)
        when (result) {
            is SourceResult -> {
                diskCache.edit(cacheKey)?.let { edit ->
                    diskCache.fileSystem.write(edit.data) {
                        result.source.copyTo(this)
                    }
                    edit.commitAndGet()?.let {
                        SourceResult(
                            request = request,
                            source = diskCache.fileSystem.source(it.data).toByteReadChannel(),
                            metadata = result.metadata,
                        )
                    }
                }
            }
        }
        return result
    }
}
