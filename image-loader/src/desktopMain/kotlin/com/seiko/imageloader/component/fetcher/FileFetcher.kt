package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.option.Options
import okio.buffer
import okio.source
import java.io.File

internal class FileFetcher(private val data: File) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchResult.Source(
            source = data.source().buffer(),
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is File) return null
            return FileFetcher(data)
        }
    }
}
