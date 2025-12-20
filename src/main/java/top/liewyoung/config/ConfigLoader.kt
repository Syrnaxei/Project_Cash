package top.liewyoung.config

import java.io.FileInputStream
import java.io.IOException
import java.util.Properties

const val configPath = "src/main/Config/config.prop"

/**
 * 获得价值
 * @param [key] 说明
 * @return [String]
 */
fun getValue(key: String): String {

    try {
        val prop = Properties();
        prop.load(FileInputStream(configPath))
        return prop.getProperty(key)?: ""
    }catch (e : IOException){
        println(e.message)
        return ""
    }
}