import top.liewyoung.agentTools.ChatRequest;
import top.liewyoung.agentTools.Message;
import top.liewyoung.agentTools.Role;
import top.liewyoung.network.Requests;

import java.io.IOException;
import java.net.http.HttpResponse;

public class RequestsTest {
    public static void main(String[] args) {
        String url = "https://api.deepseek.com/chat/completions";
        Requests chat = new Requests(url,"sdfsdf");
        ChatRequest user1 = new ChatRequest("deepseek-chat",false);

        user1.addMessage(new Message(Role.USER,"Hello"));

        try{
            HttpResponse<String> response = chat.post(user1);
            System.out.println(response.body());
        }catch(IOException | InterruptedException e){
            System.out.println(e.getMessage());
        }

    }
}
