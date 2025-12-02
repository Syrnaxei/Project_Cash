package top.liewyoung.agentTools;

public record Message(Role roleType, String content) {

    @Override
    public String toString() {
        return switch (roleType) {
            case ASSISTANT -> "Assistant";
            case SYSTEM -> "System";
            case TOOL -> "ToolCall";
            default -> "User";
        } + ":" + content;
    }

}