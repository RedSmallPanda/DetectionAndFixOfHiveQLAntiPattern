package webAPI;

import java.util.ArrayList;
import java.util.List;

public class StaticCheckImp {
    public static ReturnMessageEntity staticCheckRun(String hiveql){
        List<String> fixedSuggestions=new ArrayList<>();
        fixedSuggestions.add("不可以哦.");
        List<String>joinParams;
        joinParams = new ArrayList<>();
        ReturnMessageEntity returnMessageEntity=new ReturnMessageEntity("select a from b",fixedSuggestions,joinParams);
        return  returnMessageEntity;
    }
}
