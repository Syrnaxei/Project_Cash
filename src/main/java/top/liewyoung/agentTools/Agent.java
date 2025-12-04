package top.liewyoung.agentTools;

/**
 *
 * @author LiewYoung
 * @since 2025-12-1
 */


public class Agent implements AgentDo {
    String personality;
    Career career;

    public Agent(String personality, Career career) {
        this.personality = personality;
    }

    @Override
    public String response(String choice) {
        return "";
    }

    @Override
    public String Ask(QuestionLevel level) {
        return "";
    }

    @Override
    public String toString() {
        return "职业是:"+career+" 性格:"+personality;
    }
}


