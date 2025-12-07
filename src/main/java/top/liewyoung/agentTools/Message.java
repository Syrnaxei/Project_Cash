package top.liewyoung.agentTools;

import com.fasterxml.jackson.annotation.JsonProperty;
import top.liewyoung.agentTools.Exceptions.RoleInvalid;

public class Message {
    @JsonProperty("role")
    private String role;

    @JsonProperty("content")
    private String content;

    public Message() {
    }

    /**
     *
     * @param role 枚举
     * @param content 对话内容
     */
    public Message(Role role, String content) {
        this.role = switch (role) {
            case Role.SYSTEM -> "system";
            case Role.ASSISTANT ->  "assistant";
            default -> "user";
        };
        this.content = content;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}