package bias_check;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;

// 自动执行实验
public class grid_search {
    // 执行SQL语句
    private static void execSQL(int reduceNum, int iter, FileWriter f){
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            FileInputStream in = new FileInputStream("src/main/java/bias_check/application.properties");
            Properties props = new Properties();
            props.load(in);
            String url = props.getProperty("url");
            Connection connection = DriverManager.getConnection(url,props.getProperty("username"),props.getProperty("password"));
            String sql1="set mapred.reduce.tasks="+reduceNum;
            String sql2="set hive.auto.convert.join=false";
            String sql3="SELECT n.name, a.age FROM mrtest_50kaverage n JOIN mrtest_50kaverage a ON n.city=a.city";
            Statement ps=connection.createStatement();
            ps.execute(sql1);
            ps.execute(sql2);
            System.out.print("Reduce Num:"+reduceNum+" ");
            long startTime=System.currentTimeMillis();
            for(int i=0; i<iter; i++){
                ps.execute(sql3);
                System.out.print("*");
            }
            long costTime=(System.currentTimeMillis()-startTime)/iter;
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            String ms = formatter.format(costTime);
            System.out.println(" Time:"+ms);
            f.write(""+reduceNum+" "+costTime+" "+ms+"\n");
            ps.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 主程序
    public static void main(String[] args){
        try{
            FileWriter f = new FileWriter("gridSearch.txt");
            for(int i=1; i<15; i++){
                execSQL(i, 3, f);
            }
            execSQL(20, 3, f);
            execSQL(40, 3, f);
            execSQL(80, 3, f);
            execSQL(120, 3, f);
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
