package top.liewyoung.view.ColorSystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * 扩展函数转换颜色
 * @return [Color]
 */
private fun java.awt.Color.toComposeColor(): Color {
    return Color(this.rgb)
}


/**配色方案 */
private val MColorScheme = lightColorScheme(
    primary = MaterialPalette.MOSS.primary.toComposeColor(),
    onPrimary = MaterialPalette.MOSS.onPrimary.toComposeColor(),
    surface = MaterialPalette.MOSS.surface.toComposeColor(),
    onSurface = MaterialPalette.MOSS.onSurface.toComposeColor(),
    primaryContainer = MaterialPalette.MOSS.primaryContainer.toComposeColor(),
    onPrimaryContainer = MaterialPalette.MOSS.onPrimaryContainer.toComposeColor(),
    surfaceVariant = MaterialPalette.MOSS.surfaceVariant.toComposeColor(),
    error = MaterialPalette.MOSS.error.toComposeColor(),
)


/**
 * 应用主题
 * @param [content] 内容
 */
@Composable
fun AppTheme( content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
