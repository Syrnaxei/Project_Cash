package top.liewyoung.config

import java.io.FileOutputStream
import java.io.IOException
import java.util.Properties

/**
 * 生成配置
 * @param [apiKey] API密钥
 * @param [model] 模型
 */
fun generateConfig(apiKey: String, model: String) {
    val prop = Properties()
    prop["apiKey"] = apiKey
    prop["model"] = model

    try {
        FileOutputStream(configPath).use {
            prop.store(it, null)
        }
    }catch (e: IOException){
        println(e.message)
    }

}