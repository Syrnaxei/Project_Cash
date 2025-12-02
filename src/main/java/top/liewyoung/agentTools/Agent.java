package top.liewyoung.agentTools;

/**
 *
 * @author LiewYoung
 * @since 2025-12-1
 */

// TODO 2025-12-1 Liew.Y : 设计角色属性和方法
public class Agent {
    /**
     *
     * @param firstName   角色名字
     * @param lastName    角色姓
     * @param personality 角色人格特质
     */
    public Agent(String firstName, String lastName, String personality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personality = personality;
        this.sex = Sex.MALE;
    }

    /**
     *
     * @param firstName   角色名字
     * @param lastName    角色姓
     * @param personality 角色人格特质
     * @param sex         性别（可选）
     */
    public Agent(String firstName, String lastName, String personality, Sex sex) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personality = personality;
        this.sex = Sex.MALE;
    }

    public String createMessage(Role role,String content) {
        Message message = new Message(role,content);
        return message.toString();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getSex() {
        return switch (this.sex) {
            case MALE -> "MALE";
            case FEMALE -> "FEMALE";
            case PLASTIC -> "PLASTIC";
            default -> "NONE";
        };
    }

    @Override
    public String toString() {
        return "name=" + firstName + " " + lastName + ", personality=" + personality;
    }

    protected final String firstName;
    protected final String lastName;
    protected final Sex sex;
    private final String personality;
}


