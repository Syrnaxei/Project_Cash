package top.liewyoung.thanos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.formdev.flatlaf.FlatLightLaf
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.HostAccess
import org.graalvm.polyglot.Source
import java.io.ByteArrayOutputStream
import javax.swing.JFrame
import javax.swing.SwingUtilities


/**
 * Python 3 控制台
 *
 *@author LiewYoung
 *@since 2025/12/21
 */
class Python3Engine {
    private val outputStream = ByteArrayOutputStream()

    private var context: Context? = null

    private fun initContext() {
        context?.close()

        context = Context.newBuilder("python")
            .out(outputStream)      // 重定向标准输出
            .err(outputStream)      // 重定向错误输出
            .allowHostAccess(HostAccess.ALL) // 允许访问被 @HostAccess.Export 标记的 Java 方法
            .allowIO(false)         // 禁止文件 IO (安全沙箱)
            .allowCreateThread(false) // 禁止创建线程
            .allowCreateProcess(false) // 不允许创建进程 （超标）
            .build()
    }


    /**
     * 执行
     * @param [code] 代码
     * @return [String] 执行结果
     */
    fun execute(code: String): String {
        outputStream.reset()
        return try {
            // 使用 eval 执行 Python 3 代码
            context?.eval("python", code)
            val result = outputStream.toString().trim()
            if (result.isEmpty()) ">>> 执行成功 (Python 3.10+)" else result
        } catch (e: Exception) {
            "语法错误: ${e.message}"
        }
    }

    /**
     * 注入
     * @param [name] 对象名
     * @param [obj] 对象
     */
    fun inject(name: String, obj: Any) {
        context?.getBindings("python")?.putMember(name, obj)
    }

    /**
     * 请在开始时调用
     */
    fun restart() {
        initContext()
        outputStream.reset()
    }
}

/**
 *Compose 元素
 *
 * @param [engine] 解释器
 */
@Composable
fun PythonConsoleApp(engine: Python3Engine) {
    var inputText by remember { mutableStateOf("") }
    val history = remember { mutableStateListOf<Pair<String, String>>() }
    val listState = rememberLazyListState()
    val colorList = listOf<Color>(Color(0xFF7d45f8), Color(0xFFd113ec))


    LaunchedEffect(history.size) {
        if (history.isNotEmpty()) {
            listState.animateScrollToItem(history.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        Text(
            "THANOS PYTHON 3 控制台",
            color = Color(0xFF7d45f8),
            fontSize = 24.sp,
            style = TextStyle(
                brush = Brush.linearGradient(colorList),
                fontWeight = FontWeight.ExtraBold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "输入 'NewCli' 重新创建控制台",
            color = Color(0xFF9E9E9E),
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )


        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            items(history) { (cmd, res) ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    // 命令行定义
                    Text(
                        "In [${history.indexOf(cmd to res)}]: $cmd",
                        color = Color(0xFF81D4FA),
                        fontFamily = FontFamily.Monospace
                    )
                    if (res.isNotBlank()) {
                        Text(res, color = Color(0xFFEEEEEE), fontFamily = FontFamily.Monospace)
                    }
                    Divider(color = Color(0xFF333333), modifier = Modifier.padding(top = 4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 输入区
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(10.dp)
        ) {
            Text(">>> ", color = colorList[1], fontFamily = FontFamily.Monospace)
            BasicTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onKeyEvent { event ->
                        if (event.key == Key.Enter && event.type == KeyEventType.KeyUp) {
                            if(inputText.trim().equals("NewCli")) {
                                engine.restart()
                                history.clear()
                                history.add("NewCli" to ">>> 创建成功")
                                inputText = ""
                            }else if(inputText.isNotBlank()){
                                val response = engine.execute(inputText)
                                history.add(inputText to response)
                                inputText = ""
                            }
                            true
                        } else false
                    },
                textStyle = TextStyle(
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                ),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(colorList[0])
            )
        }
    }
}

/**
 * 产生一个 ComposePanel 和 Swing 配合
 * @param [engine] 引擎
 * @return [ComposePanel]
 */
fun getCodePanel(engine: Python3Engine): ComposePanel {

    return ComposePanel().apply {
        setContent {
            Surface {
                PythonConsoleApp(engine)
            }
        }
    }
}


/**
 * 获取引擎
 * @return [Python3Engine]
 */
fun getEngine(): Python3Engine {
    val engine = Python3Engine()
    engine.restart()
    engine.inject("Egg", EasterEgg())
    return engine
}

fun main() {

    FlatLightLaf.setup()

    SwingUtilities.invokeLater {
        val frame = JFrame("Thanos - Python 3 Console")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(900, 700)
        frame.setContentPane(getCodePanel(getEngine()))
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }
}