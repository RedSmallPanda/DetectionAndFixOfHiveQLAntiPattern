package myApplication;

import java.util.HashSet;
import java.util.List;

public class HadoopSchema {
    HashSet<String> tableName = new HashSet<>();
    HashSet<String> partitionName = new HashSet<>();

    public HadoopSchema(){
        tableName.add("tableA");
        tableName.add("tableB");
        tableName.add("tableC");
        partitionName.add("dt");
    }

    public boolean isSameTable(String name){
        return tableName.contains(name);
    }

    public boolean hasPartitionName(){
        return !partitionName.isEmpty();
    }

    public boolean usePartition(List<String> whereItemList){
        for(String s : whereItemList){
            if(partitionName.contains(s)){
                return true;
            }
        }
        return false;
    }

}
