package top.liewyoung.agentTools;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.liewyoung.config.ConfigLoader;
import top.liewyoung.network.Requests;
import top.liewyoung.network.response.ChatResponse;

import java.net.http.HttpResponse;

/**
 * 砍价代理
 * 使用DeepSeek模型扮演银行经理，与玩家进行砍价对话
 * 
 * @author LiewYoung
 * @since 2025/12/18
 */
public class BargainAgent {

    private final Requests requests;
    private ChatRequest chatRequest;
    private boolean agreed = false;
    private int currentRound = 0;
    private final int maxRounds = 3;
    private final ObjectMapper mapper = new ObjectMapper();

    // 系统提示词
    private static final String SYSTEM_PROMPT = """
            你是一位银行贷款经理，名叫王经理。玩家正在尝试和你砍价降低贷款利率。
            你需要根据玩家的论据决定是否同意降低利率。

            规则：
            1. 如果玩家论据合理有力（如信用记录好、是老客户、有担保等），你可以同意降低利率
            2. 如果玩家论据薄弱或无理取闹，你应该礼貌拒绝
            3. 每次回复需要简短自然（50字以内）
            4. 在回复末尾必须添加标记：同意降息写 [同意]，拒绝写 [拒绝]
            5. 你可以适当讨价还价，不要太轻易同意
            6. 没有必要输出利率因为降低是随机的
            
            返回形式（Json）:
            {
            'content':'你对玩家的回复‘,
            ’isTrue':'布尔值（0或者1）1表示成交'
           }

            示例回复：
            "您是我们的老客户了，信用记录确实不错。好的，我给您降低随机利率。[同意]"
            "抱歉，仅凭这个理由我无法给您优惠，您还有其他条件吗？[拒绝]"
            """;

    public BargainAgent() {
        String apiUrl = ConfigLoader.getValue("deepseek.api.url");
        String apiKey = ConfigLoader.getValue("apiKey");
        String model = ConfigLoader.getValue("model");

        // 使用默认值
        if (apiUrl == null)
            apiUrl = "https://api.deepseek.com/chat/completions";
        if (model == null)
            model = "deepseek-chat";

        this.requests = new Requests(apiUrl, apiKey);
        this.chatRequest = new ChatRequest(model, false);
        this.chatRequest.addMessage(new Message(Role.SYSTEM, SYSTEM_PROMPT));
    }

    /**
     * 与银行经理砍价
     * 
     * @param playerMessage 玩家的砍价理由
     * @return 银行经理的回复
     */
    public String bargain(String playerMessage) {
        if (currentRound >= maxRounds) {
            return "抱歉，我们已经讨论很久了，无法再给您更多优惠。[拒绝]";
        }

        currentRound++;
        chatRequest.addMessage(new Message(Role.USER, playerMessage));

        try {
            HttpResponse<String> response = requests.post(chatRequest);
            ChatResponse chatResponse = ChatResponse.fromJson(response.body());
            String content = chatResponse.getContent();
            JsonResponse jsonResponse = mapper.readValue(content, JsonResponse.class);

            // 添加助手回复到对话历史
            chatRequest.addMessage(new Message(Role.ASSISTANT, jsonResponse.getContent()));

            // 检查是否同意
            if (jsonResponse.getIsTrue().equals("1")) {
                agreed = true;
            }

            return jsonResponse.getContent();
        } catch (Exception e) {
            return "系统繁忙，请稍后再试。[拒绝]"+e.getMessage();
        }
    }
    /**
     * 检查银行是否同意砍价
     */
    public boolean isAgreed() {
        return agreed;
    }

    /**
     * 获取当前轮次
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * 获取最大轮次
     */
    public int getMaxRounds() {
        return maxRounds;
    }

    //内部类
    public static class JsonResponse {
        @JsonProperty("content")
        private String content;
        @JsonProperty("isTrue")
        private String isTrue;

        /**
         * 获取内容
         * @return  内容
         */
        public String getContent() {
            return content;
        }

        /**
         * 获取是否同意
         * @return  是否同意
         */
        public String getIsTrue() {
            return isTrue;
        }



    }

    /**
     * 重置对话（开始新的砍价）
     */
    public void reset() {
        String model = ConfigLoader.getValue("deepseek.model");
        if (model == null)
            model = "deepseek-chat";

        this.chatRequest = new ChatRequest(model, false);
        this.chatRequest.addMessage(new Message(Role.SYSTEM, SYSTEM_PROMPT));
        this.agreed = false;
        this.currentRound = 0;
    }

    public static void main(String[] args) {
        BargainAgent agent = new BargainAgent();
        System.out.println(agent.bargain("我是一个老客户，信用记录好"));
    }
}
