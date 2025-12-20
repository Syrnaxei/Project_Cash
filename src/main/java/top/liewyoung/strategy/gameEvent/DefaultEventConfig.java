package top.liewyoung.strategy.gameEvent;

import top.liewyoung.strategy.gameEvent.events.*;
import top.liewyoung.strategy.gameEvent.events.bank.*;
import top.liewyoung.strategy.gameEvent.events.fate.*;
import top.liewyoung.strategy.gameEvent.events.fungame.*;
import top.liewyoung.strategy.gameEvent.events.market.*;
import top.liewyoung.strategy.gameEvent.events.opportunity.*;


/**
 * 默认事件配置器
 * 提供所有默认游戏事件的注册
 *
 * @author LiewYoung
 * @since 2025/12/19
 */
public class DefaultEventConfig {

    /**
     * 注册所有默认事件到注册表
     */
    public static void registerAll(EventRegistry registry) {
        // 起点事件
        registry.register(new StartEvent());

        // 机会事件
        registry.registerAll(
                new StockOpportunityEvent(),
                new BusinessOpportunityEvent(),
                new InvestmentOpportunityEvent(),
                new PropertyOpportunityEvent());

        // 市场事件
        registry.registerAll(
                new StockMarketEvent(),
                new PropertyMarketEvent(),
                new InflationEvent());

        // 命运事件
        registry.registerAll(
                new WindfallEvent(),
                new CarAccidentEvent(),
                new LotteryEvent(),
                new HospitalEvent());

        // 银行事件
        registry.registerAll(
                new DepositEvent(),
                new WithdrawEvent(),
                new LoanEvent(),
                new RepaymentEvent(),
                new BargainEvent()
        ); // AI砍价事件


        // 趣味游戏事件
        registry.registerAll(
                new GuessNumberEvent(),
                new QuizEvent(),
                new ReactionEvent(),
                new Game2048Event());
    }
}
