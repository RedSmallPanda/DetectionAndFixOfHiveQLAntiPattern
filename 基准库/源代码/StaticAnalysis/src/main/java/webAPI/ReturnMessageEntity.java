package webAPI;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class ReturnMessageEntity {
    @JSONField(name="fixedHiveql")
    public String fixedHiveql;
    @JSONField(name="fixedSuggestions")
    public List<String>fixedSuggestions;
    @JSONField(name="joinParams")
    //参数顺序：t1_name,t1_joinkey,t2_name,t2_joinkey
    public List<String>joinParams;

    public ReturnMessageEntity(String fixedHiveql,List<String>fixedSuggestions,List<String>joinParams){
        super();
        this.fixedHiveql=fixedHiveql;
        this.fixedSuggestions=fixedSuggestions;
        this.joinParams=joinParams;
    }
    public ReturnMessageEntity(){
        super();
        this.fixedHiveql="";
        this.fixedSuggestions=new ArrayList<>();
        this.joinParams=null;
    }
    public void addSuggestion(String suggestion){
        this.fixedSuggestions.add(suggestion);
    }
    public String getFixedHiveql() {
        return fixedHiveql;
    }

    public void setFixedHiveql(String fixedHiveql) {
        this.fixedHiveql = fixedHiveql;
    }

    public List<String> getFixedSuggestions() {
        return fixedSuggestions;
    }

    public void setFixedSuggestions(List<String> fixedSuggestions) {
        this.fixedSuggestions = fixedSuggestions;
    }

    public List<String> getJoinParams() {
        return joinParams;
    }

    public void setJoinParams(List<String> joinPrams) {
        this.joinParams = joinPrams;
    }
}
