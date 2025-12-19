package top.liewyoung.agentTools;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

/**
 * @author LiewYoung
 * @since 2025/12/3
 */

// TODO 2025/12/3 LiewYoung : 完成模型类型的枚举或者记录
public class ChatRequest {
    @JsonProperty("model")
    private String model;
    @JsonProperty("messages")
    private ArrayList<Message> messages;
    @JsonProperty("stream")
    private boolean stream;
    @JsonProperty("response_format")
    private ResponseFormat responseFormat ;

    /**
     *
     * @param model 可选模型
     * @param stream 流式输出
     */
    public ChatRequest(String model, boolean stream) {
        this.model = model;
        this.messages = new  ArrayList<Message>();
        this.stream = stream;
        this.responseFormat = new ResponseFormat();
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    //测试代码
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ChatRequest newQ = new ChatRequest("gpt-3",false);
        newQ.addMessage(new Message(Role.SYSTEM,"You a helpful assistant"));
        newQ.addMessage(new Message(Role.USER,"Hello World!"));

        System.out.println(mapper.writeValueAsString(newQ));
    }

    class ResponseFormat {
        @JsonProperty("type")
        private String type;

        public ResponseFormat() {
            this.type = "json_object";
        }

        public ResponseFormat(String type) {
            this.type = type;
        }
    }

}
