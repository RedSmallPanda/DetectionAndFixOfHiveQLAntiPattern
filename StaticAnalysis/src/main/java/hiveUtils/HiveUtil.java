package hiveUtils;

import otherUtils.MapUtil;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HiveUtil {
    public static boolean isDataImbalanced(String table1,String col1,String table2,String col2){
        Map<String,Long> keyNum1 = new HashMap<>();
        Map<String,Long> keyNum2 = new HashMap<>();
        Map<String,Long> joinedMap = new HashMap<>();
        long sum = 0;
        long keyNum = 0;
        long maxNum = 0;
        long average = 0;
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            //FileInputStream in = new FileInputStream("C:/Users/Lenovo/Documents/GitHub/DetectionAndFixOfHiveQLAntiPattern/StaticAnalysis/src/main/java/mysqlUtils/application.properties");
            FileInputStream in = new FileInputStream("src/main/java/mysqlUtils/application.properties");
            Properties props = new Properties();
            props.load(in);
            String url = props.getProperty("url");
            Connection connection = DriverManager.getConnection(url,props.getProperty("username"),props.getProperty("password"));
            PreparedStatement ps=connection.prepareStatement("select "+ col1 + ",count(*) from "+table1+" group by "+col1);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                keyNum1.put(rs.getString(1),Long.valueOf(rs.getString(2)));
            }
            ps = connection.prepareStatement("select "+ col2 + ",count(*) from "+table2+" group by "+col2);
            rs = ps.executeQuery();
            while(rs.next()){
                keyNum2.put(rs.getString(1),Long.valueOf(rs.getString(2)));
            }
            for(Map.Entry<String,Long> iter:keyNum1.entrySet()){
                if(keyNum2.get(iter.getKey()) != null) {
                    joinedMap.put(iter.getKey(), iter.getValue() * keyNum2.get(iter.getKey()));
                }
            }

            for(Map.Entry<String,Long> iter:joinedMap.entrySet()){
                if(iter.getValue() > maxNum){
                    maxNum = iter.getValue();
                }
                sum += iter.getValue();
                keyNum += 1;
            }
            average = keyNum>1?(sum-maxNum)/(keyNum-1):sum/keyNum;

            rs.close();
            ps.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(maxNum > 5*average);
    }
}
