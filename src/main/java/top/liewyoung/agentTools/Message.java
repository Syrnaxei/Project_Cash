package top.liewyoung.agentTools;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    @JsonProperty("role")
    private String role;

    @JsonProperty("content")
    private String content;

    /**
     *
     * @param role 枚举
     * @param content 对话内容
     */
    public Message(Role role, String content) {
        if(role == Role.SYSTEM) {
            this.role = "system";
        }else  {
            this.role = "user";
        }
        this.content = content;
    }
}