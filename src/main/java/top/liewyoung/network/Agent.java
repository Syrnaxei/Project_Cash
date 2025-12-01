package top.liewyoung.network;

/**
 *
 * @author LiewYoung
 * @since 2025-12-1
 */

// TODO 2025-12-1 Liew.Y : 设计角色属性和方法
public class agent {
    public String name;
    public String personality;

    /**
     *
     * @param firstName 角色名字
     * @param lastName 角色姓
     * @param personality 角色人格特质
     */
    public agent(String firstName,String lastName, String personality) {
        this.name = firstName+" "+lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personality = personality;
        this.sex = "男";
    }

    /**
     *
     * @param firstName 角色名字
     * @param lastName 角色姓
     * @param personality 角色人格特质
     * @param sex 性别（可选）
     */
    public agent(String firstName,String lastName, String personality,String sex) {
        this.name = firstName+" "+lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personality = personality;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "name=" + name + ", personality=" + personality;
    }

    protected String firstName;
    protected String lastName;
    protected String sex;
}
