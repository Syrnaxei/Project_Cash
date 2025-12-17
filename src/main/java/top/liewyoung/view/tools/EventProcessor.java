package top.liewyoung.view.tools;

import java.util.Random;
import javax.swing.JOptionPane;

import com.syrnaxei.game.game2048.api.Game2048;
import com.syrnaxei.game.game2048.api.Game2048Listener;
import org.atom.Player;
import top.liewyoung.strategy.TitlesTypes;

/**
 * EventProcessor
 * 事件处理器 - 处理玩家走到不同格子上的事件
 *
 * @author LiewYoung
 * @since 2025/12/17
 */
public class EventProcessor {

    private Player currentPlayer;
    private Random random;
    private boolean testMode;

    public EventProcessor(Player player) {
        this.currentPlayer = player;
        this.random = new Random();
        this.testMode = false;
    }

    public EventProcessor(Player player, boolean testMode) {
        this.currentPlayer = player;
        this.random = new Random();
        this.testMode = testMode;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public void processEvent(TitlesTypes titlesTypes) {
        if (currentPlayer == null) {
            if (!testMode) {
                JOptionPane.showMessageDialog(
                    null,
                    "错误：未设置当前玩家！",
                    "错误",
                    JOptionPane.ERROR_MESSAGE
                );
            }
            return;
        }

        switch (titlesTypes) {
            case START:
                handleStartEvent();
                break;
            case OPPORTUNITY:
                handleOpportunityEvent();
                break;
            case MARKET:
                handleMarketEvent();
                break;
            case FATE:
                handleFateEvent();
                break;
            case BANK:
                handleBankEvent();
                break;
            case FUNGAME:
                handleFunGameEvent();
                break;
            default:
                if (!testMode) {
                    JOptionPane.showMessageDialog(
                        null,
                        "未知的格子类型！",
                        "提示",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                break;
        }
    }

    /**
     * 处理起点事件
     * 玩家经过起点时获得工资
     */
    private void handleStartEvent() {
        int salary = currentPlayer.getSalary();
        int currentCash = currentPlayer.getCash();
        currentPlayer.setCash(currentCash + salary);

        if (!testMode) {
            String message = String.format(
                "经过起点！\n获得工资：%d元\n当前现金：%d元",
                salary,
                currentPlayer.getCash()
            );
            JOptionPane.showMessageDialog(
                null,
                message,
                "起点事件",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * 处理机会事件
     * 随机出现投资机会或额外收入
     */
    private void handleOpportunityEvent() {
        int eventType = random.nextInt(4); // 0-3 四种机会

        switch (eventType) {
            case 0: // 小生意机会
                int smallBusinessCost = 500 + random.nextInt(1000);
                int smallBusinessIncome = 100 + random.nextInt(200);

                int choice = JOptionPane.NO_OPTION;
                if (!testMode) {
                    choice = JOptionPane.showConfirmDialog(
                        null,
                        String.format(
                            "发现小生意机会！\n投资：%d元\n每月被动收入：%d元\n是否投资？",
                            smallBusinessCost,
                            smallBusinessIncome
                        ),
                        "机会事件 - 小生意",
                        JOptionPane.YES_NO_OPTION
                    );
                }

                if (choice == JOptionPane.YES_OPTION) {
                    if (currentPlayer.getCash() >= smallBusinessCost) {
                        currentPlayer.setCash(
                            currentPlayer.getCash() - smallBusinessCost
                        );
                        currentPlayer.addPassiveIncome(smallBusinessIncome);
                        if (!testMode) {
                            JOptionPane.showMessageDialog(
                                null,
                                String.format(
                                    "投资成功！\n现金减少：%d元\n每月被动收入增加：%d元",
                                    smallBusinessCost,
                                    smallBusinessIncome
                                ),
                                "投资成功",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } else {
                        if (!testMode) {
                            if (!testMode) {
                                JOptionPane.showMessageDialog(
                                    null,
                                    "现金不足，无法投资！",
                                    "投资失败",
                                    JOptionPane.WARNING_MESSAGE
                                );
                            }
                        }
                    }
                }
                break;
            case 1: // 额外工作收入
                int extraIncome = 200 + random.nextInt(300);
                currentPlayer.setCash(currentPlayer.getCash() + extraIncome);
                if (!testMode) {
                    JOptionPane.showMessageDialog(
                        null,
                        String.format(
                            "获得额外工作收入！\n收入：%d元\n当前现金：%d元",
                            extraIncome,
                            currentPlayer.getCash()
                        ),
                        "机会事件 - 额外收入",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                break;
            case 2: // 股票机会
                int stockCost = 100 + random.nextInt(400);
                int stockReturn = random.nextInt(3) == 0 ? stockCost * 2 : 0;

                choice = JOptionPane.NO_OPTION;
                if (!testMode) {
                    choice = JOptionPane.showConfirmDialog(
                        null,
                        String.format(
                            "发现股票投资机会！\n投资：%d元\n可能回报：%d元（50%概率翻倍，50%概率亏损）\n是否投资？",
                            stockCost,
                            stockReturn
                        ),
                        "机会事件 - 股票",
                        JOptionPane.YES_NO_OPTION
                    );
                }

                if (choice == JOptionPane.YES_OPTION) {
                    if (currentPlayer.getCash() >= stockCost) {
                        currentPlayer.setCash(
                            currentPlayer.getCash() - stockCost
                        );
                        if (random.nextBoolean()) {
                            // 50%概率成功
                            currentPlayer.setCash(
                                currentPlayer.getCash() + stockCost * 2
                            );
                            if (!testMode) {
                                JOptionPane.showMessageDialog(
                                    null,
                                    String.format(
                                        "股票投资成功！\n获得回报：%d元\n当前现金：%d元",
                                        stockCost * 2,
                                        currentPlayer.getCash()
                                    ),
                                    "投资成功",
                                    JOptionPane.INFORMATION_MESSAGE
                                );
                            }
                        } else {
                            if (!testMode) {
                                JOptionPane.showMessageDialog(
                                    null,
                                    "股票投资失败，资金亏损！",
                                    "投资失败",
                                    JOptionPane.WARNING_MESSAGE
                                );
                            }
                        }
                    } else {
                        if (!testMode) {
                            JOptionPane.showMessageDialog(
                                null,
                                "现金不足，无法投资！",
                                "投资失败",
                                JOptionPane.WARNING_MESSAGE
                            );
                        }
                    }
                }
                break;
            case 3: // 房地产机会
                int propertyCost = 2000 + random.nextInt(3000);
                int propertyIncome = 300 + random.nextInt(400);

                choice = JOptionPane.NO_OPTION;
                if (!testMode) {
                    choice = JOptionPane.showConfirmDialog(
                        null,
                        String.format(
                            "发现房地产投资机会！\n首付：%d元\n每月租金收入：%d元\n是否投资？",
                            propertyCost,
                            propertyIncome
                        ),
                        "机会事件 - 房地产",
                        JOptionPane.YES_NO_OPTION
                    );
                }

                if (choice == JOptionPane.YES_OPTION) {
                    if (currentPlayer.getCash() >= propertyCost) {
                        currentPlayer.setCash(
                            currentPlayer.getCash() - propertyCost
                        );
                        currentPlayer.addPassiveIncome(propertyIncome);
                        if (!testMode) {
                            JOptionPane.showMessageDialog(
                                null,
                                String.format(
                                    "房地产投资成功！\n现金减少：%d元\n每月被动收入增加：%d元",
                                    propertyCost,
                                    propertyIncome
                                ),
                                "投资成功",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "现金不足，无法投资！",
                            "投资失败",
                            JOptionPane.WARNING_MESSAGE
                        );
                    }
                }
                break;
        }
    }

    /**
     * 处理市场事件
     * 买卖资产或遭遇市场波动
     */
    private void handleMarketEvent() {
        int eventType = random.nextInt(3); // 0-2 三种市场事件

        switch (eventType) {
            case 0: // 资产升值
                if (currentPlayer.getPassiveIncome() > 0) {
                    int appreciation = currentPlayer.getPassiveIncome() / 4; // 升值25%
                    currentPlayer.addPassiveIncome(appreciation);
                    if (!testMode) {
                        JOptionPane.showMessageDialog(
                            null,
                            String.format(
                                "市场繁荣！\n你的资产升值了！\n每月被动收入增加：%d元\n当前每月被动收入：%d元",
                                appreciation,
                                currentPlayer.getPassiveIncome()
                            ),
                            "市场事件 - 资产升值",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                } else {
                    if (!testMode) {
                        if (!testMode) {
                            JOptionPane.showMessageDialog(
                                null,
                                "市场平稳，暂无变化。",
                                "市场事件",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    }
                }
                break;
            case 1: // 资产贬值
                if (currentPlayer.getPassiveIncome() > 0) {
                    int depreciation = Math.min(
                        currentPlayer.getPassiveIncome() / 5,
                        200
                    ); // 最多贬值200
                    currentPlayer.removePassiveIncome(depreciation);
                    if (!testMode) {
                        JOptionPane.showMessageDialog(
                            null,
                            String.format(
                                "市场衰退！\n你的资产贬值了！\n每月被动收入减少：%d元\n当前每月被动收入：%d元",
                                depreciation,
                                currentPlayer.getPassiveIncome()
                            ),
                            "市场事件 - 资产贬值",
                            JOptionPane.WARNING_MESSAGE
                        );
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "市场平稳，暂无变化。",
                        "市场事件",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                break;
            case 2: // 紧急支出
                int emergencyCost = 100 + random.nextInt(400);
                if (currentPlayer.getCash() >= emergencyCost) {
                    currentPlayer.setCash(
                        currentPlayer.getCash() - emergencyCost
                    );
                    if (!testMode) {
                        JOptionPane.showMessageDialog(
                            null,
                            String.format(
                                "市场波动导致紧急支出！\n支出：%d元\n当前现金：%d元",
                                emergencyCost,
                                currentPlayer.getCash()
                            ),
                            "市场事件 - 紧急支出",
                            JOptionPane.WARNING_MESSAGE
                        );
                    }
                } else {
                    // 如果现金不足，从被动收入中扣除
                    currentPlayer.setCash(0);
                    if (!testMode) {
                        JOptionPane.showMessageDialog(
                            null,
                            "现金不足支付紧急支出！\n现金已清零。",
                            "市场事件 - 财务危机",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
                break;
        }
    }

    /**
     * 处理命运事件
     * 随机的好运或厄运
     */
    private void handleFateEvent() {
        int eventType = random.nextInt(6); // 0-5 六种命运事件

        switch (eventType) {
            case 0: // 中彩票
                int lotteryPrize = 500 + random.nextInt(1000);
                currentPlayer.setCash(currentPlayer.getCash() + lotteryPrize);
                if (!testMode) {
                    JOptionPane.showMessageDialog(
                        null,
                        String.format(
                            "命运眷顾！\n中得彩票！\n获得：%d元\n当前现金：%d元",
                            lotteryPrize,
                            currentPlayer.getCash()
                        ),
                        "命运事件 - 中彩票",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                break;
            case 1: // 继承遗产
                int inheritance = 1000 + random.nextInt(2000);
                currentPlayer.setCash(currentPlayer.getCash() + inheritance);
                if (!testMode) {
                    JOptionPane.showMessageDialog(
                        null,
                        String.format(
                            "命运眷顾！\n继承遗产！\n获得：%d元\n当前现金：%d元",
                            inheritance,
                            currentPlayer.getCash()
                        ),
                        "命运事件 - 继承遗产",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                break;
            case 2: // 遭遇盗窃
                int theftAmount = Math.min(
                    300 + random.nextInt(400),
                    currentPlayer.getCash()
                );
                currentPlayer.setCash(currentPlayer.getCash() - theftAmount);
                if (!testMode) {
                    JOptionPane.showMessageDialog(
                        null,
                        String.format(
                            "命运捉弄！\n遭遇盗窃！\n损失：%d元\n当前现金：%d元",
                            theftAmount,
                            currentPlayer.getCash()
                        ),
                        "命运事件 - 遭遇盗窃",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
                break;
            case 3: // 生病住院
                int hospitalCost = 400 + random.nextInt(600);
                if (currentPlayer.getCash() >= hospitalCost) {
                    currentPlayer.setCash(
                        currentPlayer.getCash() - hospitalCost
                    );
                    if (!testMode) {
                        JOptionPane.showMessageDialog(
                            null,
                            String.format(
                                "命运捉弄！\n生病住院！\n医疗费用：%d元\n当前现金：%d元",
                                hospitalCost,
                                currentPlayer.getCash()
                            ),
                            "命运事件 - 生病住院",
                            JOptionPane.WARNING_MESSAGE
                        );
                    }
                } else {
                    currentPlayer.setCash(0);
                    if (!testMode) {
                        JOptionPane.showMessageDialog(
                            null,
                            "现金不足支付医疗费用！\n现金已清零。",
                            "命运事件 - 医疗危机",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
                break;
            case 4: // 遇到贵人
                int mentorBonus = 300 + random.nextInt(400);
                currentPlayer.setCash(currentPlayer.getCash() + mentorBonus);
                if (!testMode) {
                    JOptionPane.showMessageDialog(
                        null,
                        String.format(
                            "命运眷顾！\n遇到贵人指点！\n获得：%d元\n当前现金：%d元",
                            mentorBonus,
                            currentPlayer.getCash()
                        ),
                        "命运事件 - 遇到贵人",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                break;
            case 5: // 意外之财
                int windfall = 200 + random.nextInt(300);
                currentPlayer.setCash(currentPlayer.getCash() + windfall);
                if (!testMode) {
                    JOptionPane.showMessageDialog(
                        null,
                        String.format(
                            "命运眷顾！\n获得意外之财！\n获得：%d元\n当前现金：%d元",
                            windfall,
                            currentPlayer.getCash()
                        ),
                        "命运事件 - 意外之财",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                break;
        }
    }

    /**
     * 处理银行事件
     * 存款、贷款、利息等金融操作
     */
    private void handleBankEvent() {
        String[] options = { "存款", "取款", "贷款", "还款", "跳过" };
        int choice = 4; // 默认选择"跳过"
        if (!testMode) {
            choice = JOptionPane.showOptionDialog(
                null,
                "欢迎来到银行！\n请选择要办理的业务：",
                "银行事件",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );
        }

        switch (choice) {
            case 0: // 存款
                String depositStr = "0";
                if (!testMode) {
                    depositStr = JOptionPane.showInputDialog(
                        null,
                        "请输入存款金额（当前现金：" +
                            currentPlayer.getCash() +
                            "元）：",
                        "存款",
                        JOptionPane.QUESTION_MESSAGE
                    );
                }
                try {
                    int depositAmount = Integer.parseInt(depositStr);
                    if (
                        depositAmount > 0 &&
                        depositAmount <= currentPlayer.getCash()
                    ) {
                        currentPlayer.setCash(
                            currentPlayer.getCash() - depositAmount
                        );
                        // 在实际游戏中，这里应该有一个银行账户来存储存款
                        if (!testMode) {
                            JOptionPane.showMessageDialog(
                                null,
                                String.format(
                                    "存款成功！\n存款金额：%d元\n当前现金：%d元",
                                    depositAmount,
                                    currentPlayer.getCash()
                                ),
                                "存款成功",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } else {
                        if (!testMode) {
                            JOptionPane.showMessageDialog(
                                null,
                                "存款金额无效！",
                                "存款失败",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } catch (NumberFormatException e) {
                    if (!testMode) {
                        if (!testMode) {
                            if (!testMode) {
                                JOptionPane.showMessageDialog(
                                    null,
                                    "请输入有效的数字！",
                                    "输入错误",
                                    JOptionPane.ERROR_MESSAGE
                                );
                            }
                        }
                    }
                }
                break;
            case 1: // 取款
                // 在实际游戏中，这里应该从银行账户中取款
                if (!testMode) {
                    JOptionPane.showMessageDialog(
                        null,
                        "取款功能暂未实现（需要银行账户系统）",
                        "功能未实现",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                break;
            case 2: // 贷款
                String loanStr = "0";
                if (!testMode) {
                    loanStr = JOptionPane.showInputDialog(
                        null,
                        "请输入贷款金额（建议不超过5000元）：",
                        "贷款",
                        JOptionPane.QUESTION_MESSAGE
                    );
                }
                try {
                    int loanAmount = Integer.parseInt(loanStr);
                    if (loanAmount > 0 && loanAmount <= 10000) {
                        currentPlayer.setCash(
                            currentPlayer.getCash() + loanAmount
                        );
                        // 在实际游戏中，这里应该记录贷款和利息
                        if (!testMode) {
                            JOptionPane.showMessageDialog(
                                null,
                                String.format(
                                    "贷款成功！\n贷款金额：%d元\n当前现金：%d元\n注意：贷款需要支付利息！",
                                    loanAmount,
                                    currentPlayer.getCash()
                                ),
                                "贷款成功",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } else {
                        if (!testMode) {
                            JOptionPane.showMessageDialog(
                                null,
                                "贷款金额无效（1-10000元）！",
                                "贷款失败",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                        null,
                        "请输入有效的数字！",
                        "输入错误",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
                break;
            case 3: // 还款
                // 在实际游戏中，这里应该偿还贷款
                if (!testMode) {
                    JOptionPane.showMessageDialog(
                        null,
                        "还款功能暂未实现（需要贷款记录系统）",
                        "功能未实现",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                break;
            case 4: // 跳过
            default:
                if (!testMode) {
                    JOptionPane.showMessageDialog(
                        null,
                        "欢迎下次光临！",
                        "银行",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                break;
        }
    }

    /**
     * 处理趣味游戏事件
     * 小游戏或趣味挑战
     */
    private void handleFunGameEvent() {
        //int gameType = random.nextInt(4); // 0-2 三种小游戏
        int gameType = 3;

        switch (gameType) {
            case 0: // 猜数字游戏
                int targetNumber = 1 + random.nextInt(10);
                String guessStr = "1";
                if (!testMode) {
                    guessStr = JOptionPane.showInputDialog(
                        null,
                        "趣味游戏：猜数字（1-10）\n猜中有奖！",
                        "猜数字游戏",
                        JOptionPane.QUESTION_MESSAGE
                    );
                }

                try {
                    int guess = Integer.parseInt(guessStr);
                    if (guess == targetNumber) {
                        int prize = 200 + random.nextInt(300);
                        currentPlayer.setCash(currentPlayer.getCash() + prize);
                        if (!testMode) {
                            JOptionPane.showMessageDialog(
                                null,
                                String.format(
                                    "恭喜猜中！\n正确答案：%d\n获得奖金：%d元\n当前现金：%d元",
                                    targetNumber,
                                    prize,
                                    currentPlayer.getCash()
                                ),
                                "游戏胜利",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } else {
                        if (!testMode) {
                            JOptionPane.showMessageDialog(
                                null,
                                String.format(
                                    "猜错了！\n正确答案：%d\n你的答案：%d\n下次加油！",
                                    targetNumber,
                                    guess
                                ),
                                "游戏失败",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                        null,
                        "请输入有效的数字！",
                        "输入错误",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
                break;
            case 1: // 选择题挑战
                String[] answers = { "Java", "Python", "C++", "JavaScript" };
                int correctAnswer = random.nextInt(4);

                int answerChoice = 0; // 默认选择第一个答案
                if (!testMode) {
                    answerChoice = JOptionPane.showOptionDialog(
                        null,
                        "趣味问答：\n哪种编程语言最初被称为'Oak'？",
                        "编程知识问答",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        answers,
                        answers[0]
                    );
                }

                if (answerChoice == correctAnswer) {
                    int prize = 150 + random.nextInt(250);
                    currentPlayer.setCash(currentPlayer.getCash() + prize);
                    if (!testMode) {
                        JOptionPane.showMessageDialog(
                            null,
                            String.format(
                                "回答正确！\nJava最初被称为'Oak'。\n获得奖金：%d元\n当前现金：%d元",
                                prize,
                                currentPlayer.getCash()
                            ),
                            "问答胜利",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                } else {
                    if (!testMode) {
                        JOptionPane.showMessageDialog(
                            null,
                            String.format(
                                "回答错误！\n正确答案是：Java\n你的选择：%s",
                                answerChoice >= 0
                                    ? answers[answerChoice]
                                    : "未选择"
                            ),
                            "问答失败",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
                break;
            case 2: // 掷硬币游戏
                int coinChoice = JOptionPane.YES_OPTION; // 默认猜正面
                if (!testMode) {
                    coinChoice = JOptionPane.showConfirmDialog(
                        null,
                        "掷硬币游戏！\n你猜正面还是反面？\n选择'是'为正面，'否'为反面",
                        "掷硬币游戏",
                        JOptionPane.YES_NO_OPTION
                    );
                }

                boolean playerGuessHeads = (coinChoice ==
                    JOptionPane.YES_OPTION);
                boolean coinResult = random.nextBoolean(); // true为正面，false为反面

                if (playerGuessHeads == coinResult) {
                    int prize = 100 + random.nextInt(200);
                    currentPlayer.setCash(currentPlayer.getCash() + prize);
                    if (!testMode) {
                        JOptionPane.showMessageDialog(
                            null,
                            String.format(
                                "猜对了！\n硬币结果是：%s\n获得奖金：%d元\n当前现金：%d元",
                                coinResult ? "正面" : "反面",
                                prize,
                                currentPlayer.getCash()
                            ),
                            "游戏胜利",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                } else {
                    if (!testMode) {
                        JOptionPane.showMessageDialog(
                            null,
                            String.format(
                                "猜错了！\n硬币结果是：%s\n你的猜测：%s",
                                coinResult ? "正面" : "反面",
                                playerGuessHeads ? "正面" : "反面"
                            ),
                            "游戏失败",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
                break;
            case 3:
                // 调用2048
                Game2048.start(new Game2048Listener() {
                    @Override
                    public void onGameEnd(int finalScore) {
                        currentPlayer.setCash(currentPlayer.getCash() + finalScore);
                        if (!testMode) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    String.format("2048 游戏结束！\n得分：%d\n当前现金：%d元",
                                            finalScore, currentPlayer.getCash()),
                                    "2048 结束",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    }
                });
                break;
        }
    }

    /**
     * 获取当前玩家的现金流信息
     */
    public String getPlayerCashflowInfo() {
        if (currentPlayer == null) {
            return "未设置玩家";
        }

        return String.format(
            "职业：%s\n现金：%d元\n工资：%d元\n月支出：%d元\n被动收入：%d元\n现金流：%d元",
            currentPlayer.getProfession(),
            currentPlayer.getCash(),
            currentPlayer.getSalary(),
            currentPlayer.getMonthlyExpenses(),
            currentPlayer.getPassiveIncome(),
            currentPlayer.calculateCashflow()
        );
    }
}
