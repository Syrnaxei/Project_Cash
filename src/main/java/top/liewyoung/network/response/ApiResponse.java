package top.liewyoung.network.response;

import java.util.List;


/**
 * <b>本类的作用是封装请求</b>
 */

//TODO Liew.Y : 设置getter函数
public record ApiResponse(
        String id,
        String object,
        String created,
        String model,
        Usage usage,
        String system_fingerprint,
        List<Choice> choices
) {
}


