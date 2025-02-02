package com.seiko.imageloader.demo.scene

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.seiko.imageloader.demo.data.imageJsonDataGif

@Composable
fun GifImagesScene(
    onBack: () -> Unit,
) {
    BackScene(
        onBack = onBack,
        title = { Text("Gif") },
    ) { innerPadding ->
        val images = rememberImageList(imageJsonDataGif)
        LazyVerticalGrid(
            GridCells.Fixed(3),
            Modifier.padding(innerPadding).fillMaxSize(),
        ) {
            items(images) { image ->
                ImageItem(image.imageUrl)
            }
        }
    }
}
