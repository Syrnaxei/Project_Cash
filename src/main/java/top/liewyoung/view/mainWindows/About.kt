package top.liewyoung.view.mainWindows

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.atom.Player
import top.liewyoung.strategy.TitlesTypes
import top.liewyoung.thanos.CommandRegistry
import top.liewyoung.thanos.Command
import top.liewyoung.thanos.getCodePanel
import top.liewyoung.thanos.getEngine
import top.liewyoung.view.ColorSystem.AppTheme
import top.liewyoung.view.Stater
import top.liewyoung.view.component.MDialog
import top.liewyoung.view.displayFonts.sharpSans
import javax.swing.JFrame
import javax.swing.SwingUtilities


/**
 * 获取关于面板
 * @return [ComposePanel]
 */
fun getAboutPanel(player: Player,vararg context: Command): ComposePanel {

    val commandConfig = CommandRegistry(null)
    val engine by lazy { getEngine(commandConfig) }

    /**
     * 注册机
     */
    fun registryEngine( ){

        commandConfig.registerAll(
            Command("player", player),
            Command("titles", TitlesTypes.entries.toTypedArray())
        )

        for(command in context){
            commandConfig.register(command)
        }
    }

    return ComposePanel().apply {
        setContent {
            AppTheme {

                Box(
                    modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("关于", fontWeight = FontWeight.Bold, fontSize = 32.sp)

                        Spacer(modifier = Modifier.size(16.dp))

                        cardFactory(
                            name = "LiewYoung",
                            icon = painterResource("Avstar.jpg")
                        )

                        cardFactory(
                            name = "刘瑞翔",
                            icon = painterResource("Avstar_1.jpg")
                        )

                        cardFactory(
                            name = "Syrnaxei",
                            icon = painterResource("Avstar_2.jpg")
                        )

                        Row {
                            Button(
                                content = { Text("刷新地图", fontWeight = FontWeight.Medium) },
                                onClick = { Stater.map.refreshMap() })

                            Spacer(modifier = Modifier.size(16.dp))

                            Button(
                                content = { Text("开启控制台", fontWeight = FontWeight.Medium)},
                                onClick = {
                                    SwingUtilities.invokeLater {
                                        val frame = JFrame("控制台")
                                        val dialog = MDialog ("提示","由于引擎问题打开会耗时，请耐心等待", MDialog.MessageType.INFO)
                                        dialog.isVisible = true
                                        registryEngine()
                                        frame.add(getCodePanel(engine))
                                        frame.setSize(800, 600)
                                        frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
                                        frame.isVisible = true
                                    }
                                }
                            )

                        }


                    }


                }
            }
        }
    }
}

/**
 * 获取个人介绍卡片
 *
 * @param [name] 名称
 * @param [desc] 描述
 * @param [icon] 图标
 */
@Composable
fun cardFactory(
    name: String,
    desc: String = "North China University of Water Resources and Electric Power",
    icon: Painter
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(painter = icon, contentDescription = null, modifier = Modifier.size(64.dp).clip(CircleShape))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(name, fontWeight = FontWeight.Bold, fontSize = 24.sp, fontFamily = sharpSans)
            Text(desc, fontWeight = FontWeight.Medium, fontSize = 16.sp, fontFamily = sharpSans)
        }
    }
}

