package webAPI;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Map;

public class JoinCheckMessageEntity {
    @JSONField(name="recommendReduceNum")
    public String recommendReduceNum;
    @JSONField(name="dataImbalancedSuggest")
    public String dataImbalancedSuggest;

    public Map<String,Long> keyMap1;
    public Map<String,Long> keyMap2;

    public JoinCheckMessageEntity(String recommendReduceNum,String dataImbalaced) {
        super();
        this.recommendReduceNum = recommendReduceNum;
        this.dataImbalancedSuggest = dataImbalaced;
    }

    public Map<String, Long> getKeyMap1() {
        return keyMap1;
    }

    public void setKeyMap1(Map<String, Long> keyMap1) {
        this.keyMap1 = keyMap1;
    }

    public Map<String, Long> getKeyMap2() {
        return keyMap2;
    }

    public void setKeyMap2(Map<String, Long> keyMap2) {
    }

    public String getRecommendReduceNum(){
        return recommendReduceNum;
    }

    public void setRecommendReduceNum(String recommendReduceNum) {
        this.recommendReduceNum = recommendReduceNum;
    }

    public String getDataImbalancedSuggest() {
        return dataImbalancedSuggest;
    }

    public void setDataImbalancedSuggest(String dataImbalancedSuggest) {
        this.dataImbalancedSuggest = dataImbalancedSuggest;
    }
}
