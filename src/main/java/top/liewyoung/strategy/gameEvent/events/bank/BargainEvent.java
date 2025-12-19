package top.liewyoung.strategy.gameEvent.events.bank;

import top.liewyoung.agentTools.BargainAgent;
import top.liewyoung.strategy.TitlesTypes;
import top.liewyoung.strategy.gameEvent.EventContext;
import top.liewyoung.strategy.gameEvent.GameEvent;
import top.liewyoung.view.component.MDialog.MessageType;

/**
 * 银行砍价事件
 * 使用AI与玩家进行砍价对话，尝试降低贷款利率
 * 
 * @author LiewYoung
 * @since 2025/12/18
 */
public class BargainEvent implements GameEvent {

    private BargainAgent agent;

    public BargainEvent() {
        this.agent = new BargainAgent();
    }

    @Override
    public TitlesTypes getType() {
        return TitlesTypes.BANK;
    }

    @Override
    public String getName() {
        return "银行砍价";
    }

    @Override
    public String getDescription() {
        return "与银行经理协商降低贷款利率";
    }

    @Override
    public void execute(EventContext ctx) {
        // 重置代理状态
        agent.reset();

        int originalRate = 10; // 原始利率10%
        int loanAmount = ctx.randomInt(2000, 5000);

        // 显示初始贷款方案
        String[] options = { "尝试砍价", "直接接受" };
        int choice = ctx.showOptions(
                String.format("银行贷款方案\n贷款金额：%d元\n年利率：%d%%\n是否尝试砍价？", loanAmount, originalRate),
                "银行贷款",
                options,
                MessageType.QUESTION);

        if (choice == 1) {
            // 直接接受，不砍价
            ctx.addCash(loanAmount);
            ctx.showMessage(
                    String.format("贷款成功！\n获得：%d元\n利率：%d%%\n当前现金：%d元", loanAmount, originalRate, ctx.getCash()),
                    "贷款成功",
                    MessageType.INFO);
            return;
        }

        // 开始砍价对话
        ctx.showMessage(
                "您好，我是王经理。请问您有什么理由希望我们降低利率呢？\n（提示：您有3次机会说服我）",
                "银行经理",
                MessageType.INFO);

        int discount = 0;

        for (int round = 1; round <= agent.getMaxRounds(); round++) {
            // 玩家输入砍价理由
            String playerInput = ctx.showInput(
                    String.format("第%d/%d轮砍价\n请输入您的砍价理由：", round, agent.getMaxRounds()),
                    "砍价",
                    "我是老客户，信用一直很好");

            if (playerInput == null || playerInput.trim().isEmpty()) {
                ctx.showMessage("您没有提供理由，银行经理表示无法给予优惠。", "提示", MessageType.WARNING);
                break;
            }

            // 调用AI获取回复
            String response = agent.bargain(playerInput);

            // 移除标记后显示
            String displayResponse = response.replace("[同意]", "").replace("[拒绝]", "").trim();
            ctx.showMessage(displayResponse, "王经理", MessageType.INFO);

            if (agent.isAgreed()) {
                // 砍价成功
                discount = ctx.randomInt(1, 4); // 降低1-3%
                ctx.showMessage(
                        String.format("砍价成功！\n利率从%d%%降至%d%%\n节省了不少利息呢！", originalRate, originalRate - discount),
                        "砍价成功",
                        MessageType.INFO);
                break;
            }
        }

        // 最终贷款
        int finalRate = originalRate - discount;
        ctx.addCash(loanAmount);
        ctx.showMessage(
                String.format("贷款完成！\n获得：%d元\n最终利率：%d%%\n当前现金：%d元", loanAmount, finalRate, ctx.getCash()),
                "贷款完成",
                MessageType.INFO);
    }
}
