package top.liewyoung.strategy.gameEvent.events.fate;

import top.liewyoung.strategy.TitlesTypes;
import top.liewyoung.strategy.gameEvent.EventContext;
import top.liewyoung.strategy.gameEvent.GameEvent;
import top.liewyoung.thanos.miniImpact.api.RunBetBall;

public class BetBallEvent implements GameEvent {

    @Override
    public TitlesTypes getType() {
        return TitlesTypes.FATE;
    }

    @Override
    public String getName() {
        return "赌球游戏";
    }

    @Override
    public String getDescription() {
        return "快试试你的运气吧";
    }

    @Override
    public void execute(EventContext ctx) {
        // 创建游戏
        RunBetBall game = new RunBetBall();

        // 开始游戏（会阻塞直到游戏结束）
        game.start();

        // 游戏结束后，获取分数并加到玩家身上
        int score = game.getScore();
        ctx.addCash(score);
    }
}
