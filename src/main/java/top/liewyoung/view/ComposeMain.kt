package top.liewyoung.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel

import androidx.compose.ui.unit.dp
import com.formdev.flatlaf.FlatLightLaf
import top.liewyoung.view.ColorSystem.AppTheme
import javax.swing.JFrame
import javax.swing.SwingUtilities


fun getPanel(): ComposePanel {
    return ComposePanel().apply {
        setContent {
            AppTheme {
                Column (
                    modifier = Modifier.safeContentPadding()
                        .padding(16.dp)
                ) {
                    Text("这是一个纯 Compose 窗口！" )
                    Text("没有 Swing，没有 JPanel，没有 add()")
                    Button(content = { Text("我是按钮") }, onClick = { println("点击了") })
                }
            }
        }
    }
}


fun main() {
    val frame = JFrame("Compose 窗口")
    FlatLightLaf.setup();
    SwingUtilities.invokeLater {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.add(getPanel())
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }
}