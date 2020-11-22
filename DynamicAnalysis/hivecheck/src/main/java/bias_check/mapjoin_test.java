package bias_check;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;

// 自动执行实验
public class mapjoin_test {
    // 执行SQL语句
    private static void execSQL(String[] conf, String sql, FileWriter f, String comment){
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            FileInputStream in = new FileInputStream("src/main/java/bias_check/application.properties");
            Properties props = new Properties();
            props.load(in);
            String url = props.getProperty("url");
            Connection connection = DriverManager.getConnection(url,props.getProperty("username"),props.getProperty("password"));
            Statement ps=connection.createStatement();
            for(String c : conf){
                ps.execute(c);
            }
            long startTime=System.currentTimeMillis();
            System.out.print(comment+" ");
            for(int i=0; i<1; i++){
                ps.execute(sql);
                System.out.print("*");
            }
            long costTime=(System.currentTimeMillis()-startTime)/1;
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            String ms = formatter.format(costTime);
            System.out.println(" Time:"+ms);
            f.write(comment+" "+costTime+" "+ms+"\n");
            ps.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 主程序
    public static void main(String[] args){
        try{
            FileWriter f = new FileWriter("data/selfJoinOrMapjoin.txt");
            String[] joinConf = {"set mapred.reduce.tasks=5", "set hive.auto.convert.join=false","set hive.auto.convert.join.noconditionaltask.size=10000000","set hive.mapjoin.smalltable.filesize=25000000","set mapred.max.split.size=256000000"};
            String[] mapJoinConf = {"set hive.auto.convert.join=true","set hive.mapjoin.smalltable.filesize=700000000","set mapred.max.split.size=100000000"};
            String[] fileList = {"50k","55age_500k","200age_5m","200age_50m"};
            for(int i=0; i<4; i++){
                for(int j=i; j<4; j++){
                    String a = "mrtest_" + fileList[i];
                    String b = "mrtest_" + fileList[j];
                    String sql = "SELECT a.name, b.age FROM "+a+" a JOIN "+b+" b ON a.name=b.name";
//                    execSQL(joinConf, sql, f, a+" join "+b);
                    execSQL(mapJoinConf, sql, f, a+" mapJoin "+b);
                }
            }
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
