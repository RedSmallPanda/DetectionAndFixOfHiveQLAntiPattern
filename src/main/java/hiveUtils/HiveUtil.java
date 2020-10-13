package hiveUtils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

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

}
