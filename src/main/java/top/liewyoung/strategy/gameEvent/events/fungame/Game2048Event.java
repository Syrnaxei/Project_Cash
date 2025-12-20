package top.liewyoung.strategy.gameEvent.events.fungame;

import com.syrnaxei.game.game2048.api.Game2048;
import com.syrnaxei.game.game2048.api.Game2048Listener;
import top.liewyoung.strategy.TitlesTypes;
import top.liewyoung.strategy.gameEvent.EventContext;
import top.liewyoung.strategy.gameEvent.GameEvent;
import top.liewyoung.view.component.MDialog.MessageType;

/**
 * 2048游戏事件
 */
public class Game2048Event implements GameEvent {

    private static volatile boolean isRunning = false;

    @Override
    public TitlesTypes getType() {
        return TitlesTypes.FUNGAME;
    }

    @Override
    public String getName() {
        return "2048游戏";
    }

    @Override
    public String getDescription() {
        return "玩2048游戏获得分数奖励";
    }

    @Override
    public void execute(EventContext ctx) {
        if (isRunning) {
            ctx.showMessage("2048游戏已经在运行中，请先完成当前游戏！", "游戏运行中", MessageType.WARNING);
            return;
        }

        isRunning = true;
        Game2048.start(finalScore -> {
            isRunning = false;
            ctx.addCash(finalScore);
            ctx.showMessage(
                    String.format("2048 游戏结束！\n得分：%d\n当前现金：%d元", finalScore, ctx.getCash()),
                    "2048 结束",
                    MessageType.INFO);
            ctx.refreshUI();
        });
    }

    public static boolean isGameRunning() {
        return isRunning;
    }
}
