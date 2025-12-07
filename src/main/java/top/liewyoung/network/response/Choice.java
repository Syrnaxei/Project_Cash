package top.liewyoung.network.response;

import top.liewyoung.agentTools.Message;

public record Choice (
    int index,
    Message message,
    String logprobs,
    String finish_reason
){}
