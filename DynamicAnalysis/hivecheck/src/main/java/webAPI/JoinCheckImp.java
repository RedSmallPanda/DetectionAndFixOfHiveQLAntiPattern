package webAPI;

import bias_check.DataImbalanceCheck;

public class JoinCheckImp {
    public static String mlpReduceNum(int t1_size,int key1_size,int t2_size,int key2_size ){
        return "";
    }
    public static JoinCheckMessageEntity joinCheckRun(String t1_name,String t1_key,String t2_name,String t2_key){

        JoinCheckMessageEntity result = new JoinCheckMessageEntity();
        if(DataImbalanceCheck.isDataImbalanced(t1_name, t1_key, t2_name, t2_key, result)){
            result.setDataImbalaced("Data may be imbalanced!");
        }
        int mlpRn = Integer.parseInt(mlpReduceNum(20000,5,2000,5));

        return result;
    }
}
