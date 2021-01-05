package bias_check;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 测量该集群下，单个reduce节点应该至少处理多大的数据量
 * reduce启动有开销，若单个reduce处理数据过少，则应该将任务合并到更少的reduce里共同完成
 * redBench_5000 join redBench_12500 Reduce 1 *** Time cost: 42319
 * redBench_5000 join redBench_12500 Reduce 5 *** Time cost: 38362
 * Calculation scale threshold: 2500000
 */
public class ReduceTaskBaselineTest {
    /*执行实验SQL方法*/
    private long execSQL(String[] conf, String sql, Statement ps) throws SQLException {
        for(String c : conf){
            ps.execute(c);
        }
        long startTime=System.currentTimeMillis();
        for(int i=0; i<3; i++){
            ps.execute(sql);
            System.out.print("*");
        }
        return (System.currentTimeMillis()-startTime)/3;
    }

    /*测试主程序*/
    public int test(){
        System.out.println("This is a performance baseline test, " +
                "please make sure hadoop cluster is in the best situation, this test may take a long time.");
        int calculationScaleThreshold=12500*12500/25;
        try{
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            FileInputStream in = new FileInputStream("src/main/resources/application.properties");
            Properties props = new Properties();
            props.load(in);
            String url = props.getProperty("url");
            Connection connection = DriverManager.getConnection(url,props.getProperty("username"),props.getProperty("password"));
            Statement ps=connection.createStatement();
            int[] recordNumList = {5000, 7500, 10000, 12500};
            String tableName1, tableName2, sql;
            for(int i=0; i<4; i++){
                for(int j=i; j<4; j++){
                    tableName1 = "redBench_"+recordNumList[i];
                    tableName2 = "redBench_"+recordNumList[j];
                    sql = "SELECT a.name, b.age FROM "+tableName1+" a JOIN "+tableName2+" b ON a.city=b.city";
                    String[] tempConf1 = {"set mapred.reduce.tasks="+1, "set hive.auto.convert.join=false"};
                    System.out.print(tableName1+" join "+tableName2+" Reduce "+1+" ");
                    long time1 = execSQL(tempConf1, sql, ps);
                    System.out.println(" Time cost: "+time1);
                    String[] tempConf2 = {"set mapred.reduce.tasks="+5, "set hive.auto.convert.join=false"};
                    System.out.print(tableName1+" join "+tableName2+" Reduce "+5+" ");
                    long time2 = execSQL(tempConf2, sql, ps);
                    System.out.println(" Time cost: "+time2);
                    if(time1/1000 > time2/1000){
                        calculationScaleThreshold = recordNumList[i]*recordNumList[j]/25;
                        break;
                    }
                }
                if(calculationScaleThreshold != 12500*12500/25){
                    break;
                }
            }
            ps.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Calculation scale threshold: "+calculationScaleThreshold);
        return calculationScaleThreshold;
    }
}
