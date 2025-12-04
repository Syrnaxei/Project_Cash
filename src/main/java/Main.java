import top.liewyoung.agentTools.Role;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args)  {
        System.out.println("Hello Agent");
        Properties prop = new Properties();
        prop.put("API_KEY","sk-1sefsfksahsdgjksj");
        try(FileOutputStream fos = new FileOutputStream("Pro.Properties")) {
            prop.store(fos,"New File");
        }catch (IOException ioe){
            System.out.println("Error");
        }

        for(Role role : Role.values()){
            System.out.println("Role: "+role);
        }
        
    }
}
