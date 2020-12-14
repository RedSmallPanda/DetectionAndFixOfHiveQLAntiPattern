package webAPI;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class Controller {
    @RequestMapping(value = "/join_check", method = RequestMethod.GET)
    public String join_check(@RequestParam(name = "t1_name") String t1_name, @RequestParam(name = "t1_key") String t1_key, @RequestParam(name = "t2_name") String t2_name, @RequestParam(name = "t2_key") String t2_key) throws IOException {
        System.out.println(t1_name +" "+ t1_key +" "+ t2_name +" "+ t2_key);
        JoinCheckMessageEntity joinCheckMessageEntity=JoinCheckImp.joinCheckRun(t1_name,t1_key,t2_name,t2_key);
//        MergedTest.astCheck(hiveql);
        String messageJson= JSON.toJSONString(joinCheckMessageEntity);
        return messageJson;
    }
}
