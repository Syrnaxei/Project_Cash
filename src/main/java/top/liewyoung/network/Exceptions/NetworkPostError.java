package top.liewyoung.network.Exceptions;

/**
 * 消息发送错误
 *
 * @author LiewYoung
 * @since 2025/12/20
 */
public class NetworkPostError extends Exception {
    public NetworkPostError(String message) {
        System.out.println(message);
    }
}
