package bias_check;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Reduce task 数量设置不合理的Anti-pattern检测
 * 针对join操作，sql格式：SELECT a.name, b.age FROM a JOIN b ON a.city=b.city
 */
public class ReduceNumCheck {
    /*单join key计算规模大于此阈值，值得使用单个reduce来处理*/
    private int threshold;

    public ReduceNumCheck(int threshold){
        this.threshold = threshold;
    }

    /*从集群获取逻辑CPU数*/
    private int getCpuNum(Statement ps){
        int cpuNum=-1;
        try{
            ResultSet r = ps.executeQuery("set yarn.nodemanager.resource.cpu-vcores");
            while (r.next()) {
                cpuNum = Integer.parseInt(r.getString(1).split("=")[1]);
            }
        } catch (Exception e){
            System.out.println("Cannot get cpu num from Hadoop, default it as 8.");
            return 8;
        }
        if(cpuNum==-1){
            System.out.println("Cannot get cpu num from Hadoop, default it as 8.");
            return 8;
        }
        return cpuNum;
    }

    /*根据表名找表的记录数和key的数量*/
    public static int[] getRecordNumAndKeyNum(String table, String key, Statement ps) throws Exception {
        System.out.print("Start get info of table " + table);
        try {
            ResultSet r = ps.executeQuery(String.format("select %s, count(1) from %s group by %s", key, table, key));
            int recordNum = 0, keyNum = 0;
            while (r.next()) {
                keyNum += 1;
                recordNum += r.getInt(2);
            }
            System.out.println(" recordNum: " + recordNum + " keyNum: " + keyNum);
            return new int[]{recordNum, keyNum};
        } catch (Exception e){
            System.out.println("Cannot get info of table " + table);
            throw new Exception();
        }
    }

    /*输出建议的reduce数量*/
    public int reduceNumCheck(String table1, String key1, String table2, String key2){
        int cpuNum=0, keyNum1=0, keyNum2=0, recordNum1=0, recordNum2=0;
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            FileInputStream in = new FileInputStream("src/main/java/bias_check/application.properties");
            Properties props = new Properties();
            props.load(in);
            String url = props.getProperty("url");
            Connection connection = DriverManager.getConnection(url,props.getProperty("username"),props.getProperty("password"));
            Statement ps=connection.createStatement();
            cpuNum = getCpuNum(ps);
            int[] temp = getRecordNumAndKeyNum(table1, key1, ps);
            recordNum1 = temp[0];
            keyNum1 = temp[1];
            if(!table1.equals(table2) || !key1.equals(key2)){
                temp = getRecordNumAndKeyNum(table2, key2, ps);
            }
            recordNum2 = temp[0];
            keyNum2 = temp[1];
            ps.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lack of info above, cannot check reduce num.");
            return -1;
        }
        if(keyNum1==0){
            System.out.println("Table "+table1+" is empty, cannot check reduce num.");
            return -1;
        }
        if(keyNum2==0){
            System.out.println("Table "+table2+" is empty, cannot check reduce num.");
            return -1;
        }

        int keyNum = Math.min(keyNum1, keyNum2);
        int calculationScalePerKey = recordNum1/keyNum1 * recordNum2/keyNum2;
        int reduceNum;
        // 若单key数据计算量超过阈值，则1key1reduce
        if(calculationScalePerKey > threshold){
            reduceNum = Math.min(keyNum, cpuNum);
        }else{
            // 若未超过，则总共1reduce
            reduceNum = 1;
        }
        System.out.println("Suggest set "+reduceNum+" reduce task(s).");
        return reduceNum;
    }

    public void testDemo(){
        //SELECT a.name, b.age FROM mrtest_5c a JOIN mrtest_5c b ON a.city=b.city;
        // mrtest_5c 5
        // mrtest_3c 3
        // mrtest_100c 1
        reduceNumCheck("mrtest_5c", "city", "mrtest_5c", "city");
        reduceNumCheck("mrtest_3c", "city", "mrtest_3c", "city");
        reduceNumCheck("mrtest_100c", "city", "mrtest_100c", "city");
    }

}
