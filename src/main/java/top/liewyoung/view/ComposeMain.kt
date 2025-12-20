package top.liewyoung.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import top.liewyoung.view.ColorSystem.AppTheme


fun main() = application {
    Window(onCloseRequest =  ::exitApplication, title = "我的纯 Compose 应用") {
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