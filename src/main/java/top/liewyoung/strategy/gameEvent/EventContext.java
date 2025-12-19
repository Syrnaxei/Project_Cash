package top.liewyoung.strategy.gameEvent;

import org.atom.Player;
import top.liewyoung.view.component.MDialog;
import top.liewyoung.view.component.MDialog.MessageType;

import java.util.Random;

/**
 * 事件执行上下文
 * 封装事件执行所需的所有依赖和便捷方法
 * 
 * @author LiewYoung
 * @since 2025/12/18
 */
public class EventContext {

    private Player player;
    private Random random;
    private boolean testMode;
    private Runnable uiRefreshCallback;

    public EventContext(Player player) {
        this.player = player;
        this.random = new Random();
        this.testMode = false;
    }

    public EventContext(Player player, boolean testMode) {
        this.player = player;
        this.random = new Random();
        this.testMode = testMode;
    }



    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Random getRandom() {
        return random;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public void setUiRefreshCallback(Runnable callback) {
        this.uiRefreshCallback = callback;
    }

    public void refreshUI() {
        if (uiRefreshCallback != null && !testMode) {
            uiRefreshCallback.run();
        }
    }



    /**
     * 显示消息对话框
     */
    public void showMessage(String message, String title, MessageType type) {
        if (!testMode) {
            MDialog.showMessageDialog(null, message, title, type);
        }
    }

    /**
     * 显示选项对话框
     * 
     * @return 用户选择的索引，测试模式下返回0（默认选第一个）
     */
    public int showOptions(String message, String title, String[] options, MessageType type) {
        if (testMode) {
            return 0;
        }
        return MDialog.showOptionDialog(null, message, title, options, type);
    }

    /**
     * 显示输入对话框
     * 
     * @return 用户输入的文本，测试模式下返回默认值
     */
    public String showInput(String message, String title, String defaultValue) {
        if (testMode) {
            return defaultValue;
        }
        return MDialog.showInputDialog(null, message, title, MessageType.QUESTION);
    }



    /**
     * 获取玩家当前现金
     */
    public int getCash() {
        return player.getCash();
    }

    /**
     * 增加现金
     */
    public void addCash(int amount) {
        player.setCash(player.getCash() + amount);
    }

    /**
     * 扣除现金
     */
    public void deductCash(int amount) {
        player.setCash(player.getCash() - amount);
    }

    /**
     * 检查是否能支付指定金额
     */
    public boolean canAfford(int amount) {
        return player.getCash() >= amount;
    }

    /**
     * 增加被动收入
     */
    public void addPassiveIncome(int amount) {
        player.addPassiveIncome(amount);
    }

    /**
     * 减少被动收入
     */
    public void removePassiveIncome(int amount) {
        player.removePassiveIncome(amount);
    }

    /**
     * 获取玩家工资
     */
    public int getSalary() {
        return player.getSalary();
    }

    /**
     * 获取玩家被动收入
     */
    public int getPassiveIncome() {
        return player.getPassiveIncome();
    }

    /**
     * 生成随机整数
     * 
     * @param bound 上界（不包含）
     */
    public int randomInt(int bound) {
        return random.nextInt(bound);
    }

    /**
     * 生成指定范围的随机整数
     * 
     * @param min 最小值（包含）
     * @param max 最大值（不包含）
     */
    public int randomInt(int min, int max) {
        return min + random.nextInt(max - min);
    }



    /**
     * 添加资产
     */
    public void addAsset(top.liewyoung.strategy.asset.Asset asset) {
        player.getAssetManager().addAsset(asset);
    }

    /**
     * 获取资产管理器
     */
    public top.liewyoung.strategy.asset.AssetManager getAssetManager() {
        return player.getAssetManager();
    }

    /**
     * 更新所有资产价值
     */
    public void updateAllAssets() {
        player.getAssetManager().updateAllAssets(random);
    }

    /**
     * 获取资产总价值
     */
    public int getTotalAssetValue() {
        return player.getTotalAssetValue();
    }
}
