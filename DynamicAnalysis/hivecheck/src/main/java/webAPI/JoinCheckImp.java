package webAPI;

import bias_check.DataImbalanceCheck;
import bias_check.ReduceNumCheck;
import org.springframework.web.client.RestTemplate;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class JoinCheckImp {
    public static String mlpReduceNum() throws IOException {
        return "-1";
    }

    public static String mlpReduceNum(int t1_size,int key1_size,int t2_size,int key2_size) throws IOException {
        System.out.println("JoinCheckImp.mlpReduceNum");
        RestTemplate restTemplate=new RestTemplate();
        // /predict_reduce/<int:t1>/<int:key1>/<int:t2>/<int:key2>
        FileInputStream in = new FileInputStream("src/main/resources/application.properties");
        Properties props = new Properties();
        props.load(in);
        String MLP_url = props.getProperty("MLP_url");
        in.close();
        String url=MLP_url+"predict_reduce/"+t1_size+"/"+key1_size+"/"+t2_size+"/"+key2_size;
        String mlpRn =restTemplate.getForObject(url,String.class);
        System.out.println("MLPReduce Num: "+mlpRn);
        return mlpRn;
    }

    public static JoinCheckMessageEntity joinCheckRun(String t1_name,String t1_key,String t2_name,String t2_key) throws IOException {
        JoinInfoEntity jie = new JoinInfoEntity();
        JoinCheckMessageEntity result = new JoinCheckMessageEntity();
        if (DataImbalanceCheck.isDataImbalanced(t1_name, t1_key, t2_name, t2_key, jie)) {
            result.setDataImbalancedSuggest("Data may be imbalanced!");
        }
        Map<String,Integer> keyMap1 = jie.getKeyMap1();
        Map<String,Integer> keyMap2 = jie.getKeyMap2();
        int keyNum1 = keyMap1.size();
        int keyNum2 = keyMap2.size();
        boolean isEmpty = false;

        int threshold = 2500000;

        if(keyNum1==0){
            System.out.println("Table "+t1_name+" is empty, cannot check reduce num.");
            isEmpty = true;
        }
        if(keyNum2==0){
            System.out.println("Table "+t2_name+" is empty, cannot check reduce num.");
            isEmpty = true;
        }
        if(isEmpty){
            result.setRecommendReduceNum("table empty, can not recommend reduce num. Please check HiveQL or database connection");
        }else{
            int recordNum1 = 0;
            for(int keyRecord : keyMap1.values()){
                recordNum1 += keyRecord;
            }
            int recordNum2 = 0;
            for(int keyRecord : keyMap2.values()){
                recordNum2 += keyRecord;
            }
            int rncRn = ReduceNumCheck.reduceNumCheckForAPI(keyNum1, keyNum2, recordNum1, recordNum2, threshold);
            int mlpRn = Integer.parseInt(mlpReduceNum(recordNum1,keyNum1,recordNum2,keyNum2));
            result.setRecommendReduceNum("Recommend reduce num: " + Math.max(rncRn, mlpRn));
        }
        return result;
    }
}
