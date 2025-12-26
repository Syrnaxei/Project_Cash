package top.liewyoung;

import com.formdev.flatlaf.FlatLightLaf;
import top.liewyoung.view.mainWindows.HomePageComposeKt;

/**
 * 主入口类 - 启动 CashFlow 游戏
 * 
 * @author LiewYoung
 * @since 2025/12/10
 */
public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        // 使用 Compose 版本的 HomePage
        HomePageComposeKt.main();
    }

}
