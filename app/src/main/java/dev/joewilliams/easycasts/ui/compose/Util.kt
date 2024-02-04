package dev.joewilliams.easycasts.ui.compose

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource

@Composable
fun previewPlaceholder(@DrawableRes id: Int) = if (LocalInspectionMode.current) {
    painterResource(id = id)
} else {
    null
}