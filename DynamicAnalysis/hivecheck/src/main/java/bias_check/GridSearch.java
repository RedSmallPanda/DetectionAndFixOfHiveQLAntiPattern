package bias_check;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

// 自动执行实验
public class GridSearch {

    // 执行SQL语句
    private void execSQL(String[] conf, String sql, FileWriter f, String comment){
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
            ps.setQueryTimeout(30*60);
//            ps.setQueryTimeout(1);
            long startTime=System.currentTimeMillis();
            System.out.print(comment+" ");
            for(int i=0; i<1; i++){
                ps.execute(sql);
                System.out.print("*");
            }
            long costTime=(System.currentTimeMillis()-startTime);
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            String ms = formatter.format(costTime);
            System.out.println(" Time:"+ms);
//            f.write(comment+" "+costTime+" "+ms+"\n");
            f.write(comment+","+costTime+"\n");
            f.flush();
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

    /*
    x：join/mapjoin，t1的数据量，t1的key种数，t2的数据量，t2的key种数，reduce数, split数
    y：时间
    */
    public void mlpTrainingDataGen(String[] tableNameSuf){
        String tableNamePre = "mrtest_";
        int[] recordNum = new int[tableNameSuf.length];
        int[] keyNum = new int[tableNameSuf.length];
        try{
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            FileInputStream in = new FileInputStream("src/main/java/bias_check/application.properties");
            Properties props = new Properties();
            props.load(in);
            String url = props.getProperty("url");
            Connection connection = DriverManager.getConnection(url,props.getProperty("username"),props.getProperty("password"));
            Statement ps=connection.createStatement();
            for(int i=0; i<tableNameSuf.length; i++){
                String tableName = tableNamePre + tableNameSuf[i];
                int[] temp = ReduceNumCheck.getRecordNumAndKeyNum(tableName, "city", ps);
                recordNum[i] = temp[0];
                keyNum[i] = temp[1];
            }
            ps.close();
            connection.close();

            int[] reduce = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25};
//            int[] reduce = {1, 3};
            int[] splitMax = {128000000, 256000000};
            // t1Num t1Key t2Num t2Key reduce splitMax time
            FileWriter f1 = new FileWriter("data/joinMlpTrainData.txt", true);
            for(int i=0; i<tableNameSuf.length; i++){
                int t1Num = recordNum[i];
                int t1Key = keyNum[i];
                for(int j=i; j<tableNameSuf.length; j++){
                    int t2Num = recordNum[j];
                    int t2Key = keyNum[j];
                    String sql = "SELECT a.name, b.age FROM "+tableNamePre + tableNameSuf[i]+" a JOIN "
                            +tableNamePre + tableNameSuf[j]+" b ON a.city=b.city";
                    for (int sMax : splitMax) {
                        for (int r : reduce) {
                            String[] joinConf = {"set mapred.max.split.size="+sMax,
                                    "set mapred.reduce.tasks=" + r, "set hive.auto.convert.join=false"};
                            execSQL(joinConf, sql, f1,
                                    String.format("%d,%d,%d,%d,%d,%d", t1Num, t1Key, t2Num, t2Key, r, sMax));
                        }
                        if(sMax > t1Num*10 && sMax > t2Num*10){
                            break;
                        }
                    }
                }
            }
            f1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
