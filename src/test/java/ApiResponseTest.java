import com.fasterxml.jackson.databind.ObjectMapper;
import top.liewyoung.agentTools.ChatRequest;
import top.liewyoung.agentTools.Message;
import top.liewyoung.agentTools.Role;
import top.liewyoung.config.ConfigLoader;
import top.liewyoung.network.Requests;
import top.liewyoung.network.response.ApiResponse;

import java.io.IOException;
import java.net.http.HttpResponse;

public class ApiResponseTest {
    public static void main(String[] args) {
        String url = "https://api.deepseek.com/chat/completions";
        Requests chat = new Requests(url, ConfigLoader.getValue("apiKey"));
        ChatRequest user1 = new ChatRequest("deepseek-chat", false);
        ObjectMapper mapper = new ObjectMapper();
        user1.addMessage(new Message(Role.SYSTEM, "You are a helpful assistant.你必须以json格式输出在content字段"));
        user1.addMessage(new Message(Role.USER, "Hello"));

        try {
            HttpResponse<String> response = chat.post(user1);
            ApiResponse api = mapper.readValue(response.body(), ApiResponse.class);
            System.out.println(api.choices().get(0).message().getContent());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
