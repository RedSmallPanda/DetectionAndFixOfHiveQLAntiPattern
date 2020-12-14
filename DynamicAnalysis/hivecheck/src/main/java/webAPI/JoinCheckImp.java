package webAPI;

import bias_check.DataImbalanceCheck;
import org.springframework.web.client.RestTemplate;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class JoinCheckImp {
    public static String mlpReduceNum(int t1_size,int key1_size,int t2_size,int key2_size ) throws IOException {
        RestTemplate restTemplate=new RestTemplate();
//        /predict_reduce/<int:t1>/<int:key1>/<int:t2>/<int:key2>
        FileInputStream in = new FileInputStream("src/main/resources/application.properties");
        Properties props = new Properties();
        props.load(in);
        String MLP_url = props.getProperty("MLP_url");
        in.close();
        String url=MLP_url+"predict_reduce/"+t1_size+"/"+key1_size+"/"+t2_size+"/"+key2_size;
        String reduceNum=restTemplate.getForObject(url,String.class);
        return reduceNum;
    }

    public static JoinCheckMessageEntity joinCheckRun(String t1_name,String t1_key,String t2_name,String t2_key) throws IOException {
        JoinCheckMessageEntity result = new JoinCheckMessageEntity("", "");
        if (DataImbalanceCheck.isDataImbalanced(t1_name, t1_key, t2_name, t2_key, result)) {
            result.setDataImbalancedSuggest("Data may be imbalanced!");
        }

        int mlpRn=Integer.parseInt(mlpReduceNum(20000,5,2000,5));

        System.out.println("MLPReduce Num: "+mlpRn);
        return result;
    }
}
