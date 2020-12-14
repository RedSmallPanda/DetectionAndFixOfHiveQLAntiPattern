package webAPI;

import java.util.ArrayList;
import java.util.List;

public class StaticCheckImp {
    public static ReturnMessageEntity staticCheckRun(String hiveql){
        List<String> fixedSuggestions=new ArrayList<>();
        fixedSuggestions.add("不可以哦.");
        List<String>joinParams=new ArrayList<>();
        joinParams.add("a");
        joinParams.add("key");
        joinParams.add("b");
        joinParams.add("key");
        //如果没有join，joinParams就直接设置为null，json中就没有joinParams这一项
        ReturnMessageEntity returnMessageEntity=new ReturnMessageEntity("select a from b",fixedSuggestions,null);

        return  returnMessageEntity;
    }
}
