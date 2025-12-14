import com.formdev.flatlaf.FlatLightLaf;
import top.liewyoung.view.mainWindows.HomePage;

import javax.swing.*;

/**
 * 主入口类 - 启动 CashFlow 游戏
 * 
 * @author LiewYoung
 * @since 2025/12/10
 */
public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HomePage();
            }
        });
    }
}
