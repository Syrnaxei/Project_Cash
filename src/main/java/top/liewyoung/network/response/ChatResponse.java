package top.liewyoung.network.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * DeepSeek API 响应解析
 * 
 * @author LiewYoung
 * @since 2025/12/18
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponse {

    private static final ObjectMapper mapper = new ObjectMapper();

    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("created")
    private long created;

    @JsonProperty("model")
    private String model;

    @JsonProperty("choices")
    private List<Choice> choices;

    @JsonProperty("usage")
    private Usage usage;

    // Getters
    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public long getCreated() {
        return created;
    }

    public String getModel() {
        return model;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public Usage getUsage() {
        return usage;
    }

    /**
     * 获取助手回复内容
     */
    public String getContent() {
        if (choices != null && !choices.isEmpty()) {
            return choices.get(0).getMessage().getContent();
        }
        return "";
    }

    /**
     * 从JSON字符串解析响应
     */
    public static ChatResponse fromJson(String json) throws Exception {
        return mapper.readValue(json, ChatResponse.class);
    }

    // 内部类: Choice
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        @JsonProperty("index")
        private int index;

        @JsonProperty("message")
        private ResponseMessage message;

        @JsonProperty("finish_reason")
        private String finishReason;

        public int getIndex() {
            return index;
        }

        public ResponseMessage getMessage() {
            return message;
        }

        public String getFinishReason() {
            return finishReason;
        }
    }

    // 内部类: ResponseMessage
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseMessage {
        @JsonProperty("role")
        private String role;

        @JsonProperty("content")
        private String content;

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }

    // 内部类: Usage
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;

        @JsonProperty("completion_tokens")
        private int completionTokens;

        @JsonProperty("total_tokens")
        private int totalTokens;

        public int getPromptTokens() {
            return promptTokens;
        }

        public int getCompletionTokens() {
            return completionTokens;
        }

        public int getTotalTokens() {
            return totalTokens;
        }
    }
}
