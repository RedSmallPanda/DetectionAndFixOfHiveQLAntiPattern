package webAPI;

import com.alibaba.fastjson.annotation.JSONField;

public class JoinCheckMessageEntity {
    @JSONField(name="recommendReduceNum")
    public String recommendReduceNum;
    @JSONField(name="dataImbalancedSuggest")
    public String dataImbalancedSuggest;
    public JoinCheckMessageEntity(String recommendReduceNum,String dataImbalaced){
        super();
        this.recommendReduceNum=recommendReduceNum;
        this.dataImbalancedSuggest =dataImbalaced;
    }

    public String getRecommendReduceNum() {
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
