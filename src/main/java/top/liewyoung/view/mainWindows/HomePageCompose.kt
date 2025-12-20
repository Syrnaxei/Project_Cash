package top.liewyoung.view.mainWindows

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import com.formdev.flatlaf.FlatLightLaf
import top.liewyoung.view.ColorSystem.AppTheme
import top.liewyoung.view.Stater
import javax.swing.SwingUtilities

/**
 * Compose 版本的主页
 * @author LiewYoung
 * @since 2025/12/20
 */

fun main() {
    // 预先初始化 FlatLaf，避免点击按钮时卡顿
    FlatLightLaf.setup()

    // 启动 Compose 窗口
    var windowClosed = false


    androidx.compose.ui.window.application(exitProcessOnExit = false) {
        if (!windowClosed) {
            Window(
                onCloseRequest = {
                    windowClosed = true
                    exitApplication()
                },
                title = "CashFlow",
                state = rememberWindowState(size = DpSize(1200.dp, 850.dp))
            ) {
                AppTheme {
                    HomePageContent(
                        onStartGame = {
                            // 启动 Swing 游戏界面
                            SwingUtilities.invokeLater {
                                val frame = javax.swing.JFrame()
                                frame.title = "CashFlow"
                                frame.layout = java.awt.BorderLayout()
                                Stater.map = MapDraw()
                                Stater.map.background = java.awt.Color(253, 253, 245)
                                frame.add(Stater.map, java.awt.BorderLayout.CENTER)
                                frame.add(DashboardPanel(Stater.map), java.awt.BorderLayout.EAST)
                                frame.background = java.awt.Color(253, 253, 245)
                                frame.setSize(1200, 835)
                                frame.setLocation(300, 100)
                                frame.isResizable = false
                                frame.defaultCloseOperation = javax.swing.JFrame.EXIT_ON_CLOSE
                                frame.isVisible = true
                            }
                            // 关闭 Compose 窗口
                            windowClosed = true
                            exitApplication()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomePageContent(onStartGame: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp)
            ) {


                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    // 标题
                    Text(
                        text = "CashFlow",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // 开始游戏按钮
                    FilledTonalButton(
                        onClick = onStartGame,
                        modifier = Modifier
                            .height(48.dp)
                            .width(160.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = "开始游戏",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                // 说明文字
                Text(
                    text = "CashFlow是一款基于JavaSwing的桌面游戏，游戏内容由多名作者开发，游戏内容基于《Cash Flow》游戏。",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(32.dp))


            }
        }
    }
}
