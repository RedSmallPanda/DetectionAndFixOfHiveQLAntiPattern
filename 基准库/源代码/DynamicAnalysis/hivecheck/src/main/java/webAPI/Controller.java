package webAPI;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@RestController
@CrossOrigin(value = "*")
public class Controller {
    @RequestMapping(value = "/join_check", method = RequestMethod.GET)
    public String join_check(@RequestParam(name = "t1_name") String t1_name, @RequestParam(name = "t1_key") String t1_key, @RequestParam(name = "t2_name") String t2_name, @RequestParam(name = "t2_key") String t2_key) throws IOException {
        System.out.println(t1_name +" "+ t1_key +" "+ t2_name +" "+ t2_key);
        JoinCheckMessageEntity joinCheckMessageEntity=JoinCheckImp.joinCheckRun(t1_name,t1_key,t2_name,t2_key);
//        MergedTest.astCheck(hiveql);
        String messageJson= JSON.toJSONString(joinCheckMessageEntity);
        return messageJson;
    }

    @RequestMapping(value = "/configSet", method = RequestMethod.POST)
    public String configSet(@RequestParam Map<String, String> params){
        StringBuilder sb = new StringBuilder();
        try{
            FileInputStream in = new FileInputStream("src/main/resources/application.properties");
            Properties props = new Properties();
            props.load(in);
            for(Map.Entry<String, String> entry : params.entrySet()){
                if(props.containsKey(entry.getKey())){
                    String old = (String) props.setProperty(entry.getKey(), entry.getValue());
                    sb.append("Change ").append(old).append(" to ").append(entry.getValue()).append(". \n");
                }
            }
            in.close();
            FileOutputStream out = new FileOutputStream("src/main/resources/application.properties");
            props.store(out, "");
            out.close();
        } catch (Exception e){
            e.printStackTrace();
            sb.replace(0, sb.length(), "Error!");
        }
        return sb.toString();
    }
    @RequestMapping(value = "/configGet", method = RequestMethod.GET)
    public String configGet(){
        String sb = "";
        JSONObject jsonObject=new JSONObject();
        try{
            FileInputStream in = new FileInputStream("src/main/resources/application.properties");
            Properties props = new Properties();
            props.load(in);
            in.close();
            for(String key : props.stringPropertyNames()){
                jsonObject.put(key,props.getProperty(key));
            }
            sb=jsonObject.toJSONString();

        } catch (Exception e){
            e.printStackTrace();
            sb=" Get config Error!";
        }
        return sb;
    }
}
