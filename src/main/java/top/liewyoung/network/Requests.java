package top.liewyoung.network;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;


import com.fasterxml.jackson.databind.ObjectMapper;
import top.liewyoung.agentTools.ChatRequest;
import top.liewyoung.agentTools.Message;
import top.liewyoung.agentTools.Role;

/**
 *
 * @author LiewYoung
 * @since 2025/12/1
 */


public final class Requests {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final URI uri;
    private final HttpClient client;
    private final String apiKey;

    /**
     *
     * @param uri 模型请求的base_url
     * @param apiKey 你自己的apiKey
     */
    public Requests(String uri,String apiKey){
        this.uri = URI.create(uri);
        this.apiKey = apiKey;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    /**
     * 本方法是<b>专为Chat优化后的方法</b>，请不要当成普通POST方法
     * @param content 需要一个ChatRequest对象
     * @return {@code HttpResponse<String>}
     * @throws IOException 读入错误
     * @throws InterruptedException 内部错误
     */
    public HttpResponse<String> post(ChatRequest content) throws IOException, InterruptedException {
        String body = mapper.writeValueAsString(content);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + this.apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(20))
                .build();

        return client.send(request,HttpResponse.BodyHandlers.ofString());
    }

    //测试代码
    public static void main(String[] args) throws IOException, InterruptedException {
        Message message1 = new Message(Role.SYSTEM,"You are a helpful assistant");
        Message message2 = new Message(Role.USER,"Hello world");

        ChatRequest req = new ChatRequest("deepseek-chat",false);
        req.addMessage(message1);
        req.addMessage(message2);

    }
}


