import top.liewyoung.agentTools.Role;

public class RoleTest {
    public static void main(String[] args) {
        for(Role role : Role.values()){
            System.out.println(role);
        }
        try {
            System.out.println(Role.valueOf("sys"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            System.out.println("程序结束");
        }

    }
}
