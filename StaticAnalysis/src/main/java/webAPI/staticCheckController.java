package webAPI;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import myApplication.MergedTest;

@RestController
public class staticCheckController {
    @RequestMapping(value="/astCheck",method = RequestMethod.GET)
    public String astCheck(@RequestParam(name="hiveql")String hiveql){
        System.out.println(hiveql);
        MergedTest.astCheck(hiveql);
        return "123";
    }
}
