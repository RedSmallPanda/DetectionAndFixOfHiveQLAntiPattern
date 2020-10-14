package mysqlUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class MysqlUtil {
    public static boolean compareTwoTableRowNum(String leftTable,String rightTable) {
        int leftTableRowNum = 0;
        int rightTableRowNum = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            FileInputStream in = new FileInputStream("src/main/java/mysqlUtils/application.properties");
            Properties props = new Properties();
            props.load(in);
            String url = props.getProperty("mysqlUrl");
            Connection connection = DriverManager.getConnection(url, props.getProperty("mysqlUsername"), props.getProperty("mysqlPassword"));
            PreparedStatement ps = connection.prepareStatement("select TBL_ID from TBLS where TBL_NAME=\"" + leftTable + "\"");
            ResultSet r = ps.executeQuery();
            String leftTableID = "";
            while(r.next()){
                leftTableID = r.getString(1);
            }
            ps = connection.prepareStatement("select TBL_ID from TBLS where TBL_NAME=\"" + rightTable + "\"");
            r = ps.executeQuery();
            String rightTableID = "";
            while(r.next()){
                rightTableID = r.getString(1);
            }
            ps = connection.prepareStatement("select PART_ID from PARTITIONS where TBL_ID=\"" + leftTableID + "\"");
            r = ps.executeQuery();
            // 判断分区表查询结果是否为空
            if(r.next()){
                //获取分区表的记录总数
                ps = connection.prepareStatement("select sum(PARAM_VALUE) from (select PART_ID from PARTITIONS where TBL_ID=\"" + leftTableID + "\") as partID join PARTITION_PARAMS on partID.PART_ID = PARTITION_PARAMS.PART_ID where PARAM_KEY = \"numRows\"");
                r = ps.executeQuery();
                while (r.next()) {
                    leftTableRowNum = Double.valueOf(r.getString(1)).intValue();
                }
            }
            else {
                //获取非分区表的记录总数
                ps = connection.prepareStatement("select PARAM_VALUE from TABLE_PARAMS where TBL_ID=\"" + leftTableID + "\" and PARAM_KEY=\"numRows\"");
                r = ps.executeQuery();
                while (r.next()) {
                    leftTableRowNum = Integer.valueOf(r.getString(1));
                }
            }

            ps = connection.prepareStatement("select PART_ID from PARTITIONS where TBL_ID=\"" + rightTableID + "\"");
            r = ps.executeQuery();
            if(r.next()){
                //获取分区表的记录总数
                ps = connection.prepareStatement("select sum(PARAM_VALUE) from (select PART_ID from PARTITIONS where TBL_ID=\"" + rightTableID + "\") as partID join PARTITION_PARAMS on partID.PART_ID = PARTITION_PARAMS.PART_ID where PARAM_KEY = \"numRows\"");
                r = ps.executeQuery();
                while (r.next()) {
                    rightTableRowNum = Double.valueOf(r.getString(1)).intValue();
                }
            }
            else {
                ps = connection.prepareStatement("select PARAM_VALUE from TABLE_PARAMS where TBL_ID=\"" + rightTableID + "\" and PARAM_KEY=\"numRows\"");
                r = ps.executeQuery();
                while (r.next()) {
                    rightTableRowNum = Integer.valueOf(r.getString(1));
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return(leftTableRowNum < rightTableRowNum);
    }

    public static boolean compareTwoTableRowNumByHive(String leftTable,String rightTable){
        int leftTableRowNum = 0;
        int rightTableRowNum = 0;
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            InputStream in = MysqlUtil.class.getResourceAsStream("/application.properties");
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
        if(table1.length < 2 || table2.length < 2){
            return true;
        }
        String tableName1 = table1[0];
        String column1 = table1[1];
        String tableName2 = table2[0];
        String column2 = table2[1];
        String type1 = "";
        String type2 = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            FileInputStream in = new FileInputStream("src/main/java/mysqlUtils/application.properties");
            Properties props = new Properties();
            props.load(in);
            String url = props.getProperty("mysqlUrl");
            Connection connection = DriverManager.getConnection(url,props.getProperty("mysqlUsername"),props.getProperty("mysqlPassword"));
            PreparedStatement ps=connection.prepareStatement("select TBL_ID from TBLS where TBL_NAME=\""+tableName1+"\"");
            ResultSet r=ps.executeQuery();
            String table1ID = "";
            while(r.next()){
                table1ID = r.getString(1);
            }
            String table2ID = "";
            ps = connection.prepareStatement("select TBL_ID from TBLS where TBL_NAME=\""+tableName2+"\"");
            r = ps.executeQuery();
            while(r.next()){
                table2ID = r.getString(1);
            }

            PreparedStatement ps2=connection.prepareStatement("select TYPE_NAME from COLUMNS_V2 where CD_ID=\""+table1ID+"\" and COLUMN_NAME=\""+column1+"\"");
            ResultSet r2=ps2.executeQuery();
            while(r2.next()){
                type1 = r2.getString(1);
            }
            ps2=connection.prepareStatement("select TYPE_NAME from COLUMNS_V2 where CD_ID=\""+table2ID+"\" and COLUMN_NAME=\""+column2+"\"");
            r2=ps2.executeQuery();
            while(r2.next()){
                type2 = r2.getString(1);
            }

            ps.close();
            ps2.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(type1.equals(type2));
    }

    public static boolean hasSameTable(HashSet<String> colName){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            FileInputStream in = new FileInputStream("src/main/java/mysqlUtils/application.properties");
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
            FileInputStream in = new FileInputStream("src/main/java/mysqlUtils/application.properties");
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
