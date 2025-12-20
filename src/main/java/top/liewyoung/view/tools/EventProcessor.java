package top.liewyoung.view.tools;

import org.atom.Player;
import top.liewyoung.strategy.TitlesTypes;
import top.liewyoung.strategy.gameEvent.*;
import top.liewyoung.strategy.gameEvent.events.fungame.Game2048Event;
import top.liewyoung.view.component.MDialog;
import top.liewyoung.view.component.MDialog.MessageType;

/**
 * EventProcessor
 * 事件处理器 - 处理玩家走到不同格子上的事件
 * 使用事件注册表自动管理所有事件
 * <b>准确来说这个类已经弃用了但是由于不确定性保留了</b>
 *
 * @author LiewYoung
 * @since 2025/12/17
 */
public class EventProcessor {

    private EventRegistry registry;
    private EventContext context;

    public EventProcessor(Player player) {
        this.context = new EventContext(player);
        this.registry = new EventRegistry();
        DefaultEventConfig.registerAll(registry);
    }

    public EventProcessor(Player player, boolean testMode) {
        this.context = new EventContext(player, testMode);
        this.registry = new EventRegistry();
        DefaultEventConfig.registerAll(registry);
    }

    public void setCurrentPlayer(Player player) {
        this.context.setPlayer(player);
    }

    public void setUiRefreshCallback(Runnable callback) {
        this.context.setUiRefreshCallback(callback);
    }

    /**
     * 处理事件
     * 
     * @param titlesTypes 事件类型
     */
    public void processEvent(TitlesTypes titlesTypes) {
        if (context.getPlayer() == null) {
            if (!context.isTestMode()) {
                MDialog.showMessageDialog(null, "错误：玩家未设置！", "系统错误", MessageType.ERROR);
            }
            return;
        }

        GameEvent event = registry.getRandomEvent(titlesTypes, context.getRandom());
        if (event != null) {
            event.execute(context);
        } else {
            if (!context.isTestMode()) {
                MDialog.showMessageDialog(null, "未知的事件类型", "事件错误", MessageType.WARNING);
            }
        }

        context.refreshUI();
    }

    /**
     * 注册自定义事件
     */
    public void registerEvent(GameEvent event) {
        registry.register(event);
    }

    /**
     * 批量注册自定义事件
     */
    public void registerEvents(GameEvent... events) {
        registry.registerAll(events);
    }

    /**
     * 注销事件
     */
    public void unregisterEvent(GameEvent event) {
        registry.unregister(event);
    }

    /**
     * 获取事件注册表
     */
    public EventRegistry getRegistry() {
        return registry;
    }

    /**
     * 获取事件上下文
     */
    public EventContext getContext() {
        return context;
    }

    /**
     * 获取当前玩家的现金流信息
     */
    public String getPlayerCashflowInfo() {
        Player player = context.getPlayer();
        if (player == null) {
            return "未设置玩家";
        }

        return String.format(
                "职业：%s\n现金：%d元\n工资：%d元\n月支出：%d元\n被动收入：%d元\n现金流：%d元",
                player.getProfession(),
                player.getCash(),
                player.getSalary(),
                player.getMonthlyExpenses(),
                player.getPassiveIncome(),
                player.calculateCashflow());
    }

    /**
     * 切换测试模式
     */
    public void setTestMode(boolean testMode) {
        context.setTestMode(testMode);
    }

    /**
     * 获取测试模式状态
     */
    public boolean isTestMode() {
        return context.isTestMode();
    }

    /**
     * 获取2048游戏运行状态
     */
    public static boolean is2048Running() {
        return Game2048Event.isGameRunning();
    }
}
