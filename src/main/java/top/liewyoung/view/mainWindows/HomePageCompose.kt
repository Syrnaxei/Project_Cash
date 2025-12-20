package top.liewyoung.view.mainWindows

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.formdev.flatlaf.FlatLightLaf
import top.liewyoung.view.ColorSystem.AppTheme
import top.liewyoung.view.Stater
import javax.swing.JFrame
import javax.swing.SwingUtilities


/**
 * 获取主页面板
 *
 * @param [frame] 父容器
 * @return [ComposePanel]
 */
fun getHomePanel(frame: JFrame): ComposePanel {
    return ComposePanel().apply {
        setContent {
            AppTheme {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                "CashFlow",
                                modifier = Modifier.align(Alignment.CenterVertically),
                                fontSize = 48.sp, fontWeight = FontWeight.Bold
                            )

                            Button(onClick = {
                                SwingUtilities.invokeLater {
                                    Stater.main(null)
                                }
                                frame.dispose()
                            }) {
                                Text("开始游戏")
                            }
                        }

                        Text(
                            "CashFlow是一款基于JavaSwing的桌面游戏，游戏内容由多名作者开发，游戏内容基于《Cash Flow》游戏",
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}


fun main() {
    val frame = JFrame("Compose 窗口")
    FlatLightLaf.setup()
    SwingUtilities.invokeLater {
        frame.add(getHomePanel(frame))
        frame.setSize(1200, 850)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
        println("启动成功")
    }
}