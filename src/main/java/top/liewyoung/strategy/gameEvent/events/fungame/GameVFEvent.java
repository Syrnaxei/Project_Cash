package top.liewyoung.strategy.gameEvent.events.fungame;

import com.syrnaxei.game.gameVF.api.GameVF;
import top.liewyoung.strategy.TitlesTypes;
import top.liewyoung.strategy.gameEvent.EventContext;
import top.liewyoung.strategy.gameEvent.GameEvent;
import top.liewyoung.view.component.MDialog;

/**
 * VF游戏事件
 */
public class GameVFEvent implements GameEvent {
    private static volatile boolean isRunning = false;

    @Override
    public TitlesTypes getType() {
        return TitlesTypes.FUNGAME;
    }

    @Override
    public String getName() {
        return "VF射击游戏";
    }

    @Override
    public String getDescription() {
        return "玩VF获得分数";
    }

    @Override
    public void execute(EventContext ctx) {
        if (isRunning) {
            ctx.showMessage("VF游戏已经在运行中，请先完成当前游戏！", "游戏运行中", MDialog.MessageType.WARNING);
            return;
        }
        isRunning = true;

        // 启动VF游戏并设置结束回调
        GameVF.start(finalScore -> {
            isRunning = false;
            ctx.addCash(finalScore);
            ctx.showMessage(
                    String.format("VF 游戏结束！\n得分：%d\n当前现金：%d元", finalScore, ctx.getCash()),
                    "VF 结束",
                    MDialog.MessageType.INFO);
            ctx.refreshUI();
        });
    }
}
