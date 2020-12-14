package webAPI;

import java.util.List;

public class ReturnMessageEntity {
    public String fixedHiveql;
    public List<String>fixedSuggestions;
    //参数顺序：t1_name,t1_joinkey,t2_name,t2_joinkey
    public List<String>joinPrams;

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

    public List<String> getJoinPrams() {
        return joinPrams;
    }

    public void setJoinPrams(List<String> joinPrams) {
        this.joinPrams = joinPrams;
    }
}
