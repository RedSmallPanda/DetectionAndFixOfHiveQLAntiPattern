package bias_check;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;

// 自动执行实验
public class GridSearch {

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
            for(int i=0; i<3; i++){
                ps.execute(sql);
                System.out.print("*");
            }
            long costTime=(System.currentTimeMillis()-startTime)/3;
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

    public void selfJoinOrMapjoinExperiment(){
        try {
            FileWriter f = new FileWriter("data/selfJoinOrMapjoin.txt");
            String[] joinConf = {"set mapred.reduce.tasks=5", "set hive.auto.convert.join=false"};
            String[] mapJoinConf = {"set hive.auto.convert.join=true"};
            String[] fileList = {"10", "50", "500", "5k", "50k"};
            for (int i = 0; i < 5; i++) {
                for (int j = i; j < 5; j++) {
                    String a = "mrtest_" + fileList[i];
                    String b = "mrtest_" + fileList[j];
                    String sql = "SELECT a.name, b.age FROM " + a + " a JOIN " + b + " b ON a.city=b.city";
                    execSQL(joinConf, sql, f, a + " join " + b);
                    execSQL(mapJoinConf, sql, f, a + " mapJoin " + b);
                }
            }
            f.close();
            FileWriter f2 = new FileWriter("data/reduceNum.txt");
            int[] numList = {1, 3, 5, 10, 30, 50, 100, 300};
            for (int i = 0; i < 8; i++) {
                int cityNum = numList[i];
                String tableName = "mrtest_" + cityNum + "c";
                for (int j = 0; j < 8; j++) {
                    int reduceNum = numList[j];
                    String[] tempConf = {"set mapred.reduce.tasks=" + reduceNum, "set hive.auto.convert.join=false"};
                    String sql = "SELECT a.name, b.age FROM " + tableName + " a JOIN " + tableName + " b ON a.city=b.city";
                    execSQL(tempConf, sql, f2, "city" + cityNum + " reduce " + reduceNum);
                }
                f2.write("\n");
            }
            f2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reduceNumExperiment() {
        try{
            FileWriter f2 = new FileWriter("data/reduceNum.txt");
            int[] numList = {11, 13, 15, 17, 19, 21};
            String tableName = "mrtest_30c60k";
            for(int j=0; j<6; j++){
                int reduceNum = numList[j];
                String[] tempConf = {"set mapred.reduce.tasks="+reduceNum, "set hive.auto.convert.join=false"};
                String sql = "SELECT a.name, b.age FROM "+tableName+" a JOIN "+tableName+" b ON a.city=b.city";
                execSQL(tempConf, sql, f2, "city"+30+" reduce "+reduceNum);
            }
            f2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
