package hiveUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class HiveUtil {
    public static boolean compareTwoTableRowNum(String leftTable,String rightTable) {
        int leftTableRowNum = 0;
        int rightTableRowNum = 0;
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            InputStream in = HiveUtil.class.getResourceAsStream("/hive.properties");
            Properties props = new Properties();
            props.load(in);
            String url = props.getProperty("url");
            Connection connection = DriverManager.getConnection(url,props.getProperty("username"),props.getProperty("password"));
            PreparedStatement ps=connection.prepareStatement("select count(*) from "+leftTable);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                leftTableRowNum = Integer.valueOf(rs.getString(1));
            }
            ps = connection.prepareStatement("select count(*) from "+rightTable);
            rs = ps.executeQuery();
            while(rs.next()){
                rightTableRowNum = Integer.valueOf(rs.getString(1));
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(leftTableRowNum < rightTableRowNum);
    }
    public static boolean compareParamType(String leftParam,String rightParam){
        String[] table1 = leftParam.split("\\.");
        String[] table2 = rightParam.split("\\.");
        String tablename1 = table1[0];
        String column1 = table1[1];
        String tablename2 = table2[0];
        String column2 = table2[1];
        System.out.println(tablename1);
        System.out.println(column1);
        System.out.println(tablename2);
        System.out.println(column2);
        return true;
    }

    public static boolean hasSameTable(HashSet<String> colName){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            FileInputStream in = new FileInputStream("src/main/java/hiveUtils/hive.properties");
            Properties props = new Properties();
            props.load(in);
            String url = props.getProperty("mysqlUrl");
            Connection connection = DriverManager.getConnection(url,props.getProperty("mysqlUsername"),props.getProperty("mysqlPassword"));
            PreparedStatement ps=connection.prepareStatement("select CD_ID, COLUMN_NAME from COLUMNS_V2");
            ResultSet r=ps.executeQuery();
            Map<String, HashSet<String>> colNameDic = new HashMap<String, HashSet<String>>();
            while(r.next()){
                String key = r.getString(1);
                String value = r.getString(2);
                if(!colNameDic.containsKey(key)){
                    colNameDic.put(key, new HashSet<String>());
                }
                colNameDic.get(key).add(value);
            }
            if(colNameDic.containsValue(colName)){
                return true;
            }
            ps.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean usePartitionCorrect(String tableName, List<String> whereItemList){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            FileInputStream in = new FileInputStream("src/main/java/hiveUtils/hive.properties");
            Properties props = new Properties();
            props.load(in);
            String url = props.getProperty("mysqlUrl");
            Connection connection = DriverManager.getConnection(url,props.getProperty("mysqlUsername"),props.getProperty("mysqlPassword"));
            PreparedStatement ps=connection.prepareStatement("select TBL_ID from TBLS where TBL_NAME=\""+tableName+"\"");
            ResultSet r=ps.executeQuery();
            String tableID = "";
            while(r.next()){
                tableID = r.getString(1);
            }
            if(tableID.equals("")){
                return true;
            }
            PreparedStatement ps2=connection.prepareStatement("select PKEY_NAME from PARTITION_KEYS where TBL_ID=\""+tableID+"\"");
            ResultSet r2=ps2.executeQuery();
            HashSet<String> partCol = new HashSet<String>();
            while(r2.next()){
                partCol.add(r2.getString(1));
            }
            if(partCol.isEmpty()){
                return true;
            }
            for(String s: whereItemList){
                if(partCol.contains(s)){
                    return true;
                }
            }
            ps.close();
            ps2.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
