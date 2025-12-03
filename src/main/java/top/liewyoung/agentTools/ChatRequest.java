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

    /**
     *
     * @param model 可选模型
     * @param messages 需要ArrayLists<message>
     * @param stream 流式输出
     */
    public ChatRequest(String model, ArrayList<Message> messages, boolean stream) {
        this.model = model;
        this.messages = messages;
        this.stream = stream;
    }


    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message(Role.SYSTEM,"You are a helpful assistant"));
        messages.add(new Message(Role.USER,"Hello world"));
        System.out.println(mapper.writeValueAsString(new ChatRequest("deepseek",messages,false)));
    }

}
