package webAPI;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.*;
import myApplication.MergedTest;

@RestController
@CrossOrigin(value = "*")
public class staticCheckController {
    @RequestMapping(value="/astCheck",method = RequestMethod.GET)
    public String astCheck(@RequestParam(name="hiveql")String hiveql){
        System.out.println(hiveql);
        ReturnMessageEntity returnMessageEntity=StaticCheckImp.staticCheckRun(hiveql);
        if(returnMessageEntity!=null) {
            String messageJson = JSON.toJSONString(returnMessageEntity);
            System.out.println(messageJson);
            return messageJson;
        }
//        MergedTest.astCheck(hiveql);
        return "hiveql格式错误";
    }
    @RequestMapping(value="/configCheck",method = RequestMethod.GET)
    public String configCheck(){

        MergedTest.configCheck();
        return "config_check";
    }
}
