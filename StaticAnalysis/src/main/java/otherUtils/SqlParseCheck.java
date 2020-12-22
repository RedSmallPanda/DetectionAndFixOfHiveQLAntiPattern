package otherUtils;

import org.apache.hive.service.cli.HiveSQLException;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/*
* 检测Sql是否符合基本的语法规则
* */
public class SqlParseCheck {
    public static boolean sqlParseCheck(String sql){
        FileInputStream in = null;
        Properties props = null;
        try{
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            in = new FileInputStream("src/main/resources/application.properties");
            props = new Properties();
            props.load(in);
        } catch (Exception e){
            e.printStackTrace();
        }
        if(props == null){
            return false;
        }
        try(
                Connection connection = DriverManager.getConnection(props.getProperty("url"),props.getProperty("username"),props.getProperty("password"));
                Statement ps=connection.createStatement()
        )
        {
            ResultSet r = ps.executeQuery("explain " + sql);
            while (r.next()) {
                System.out.println(r.getString(1));
            }
        } catch (HiveSQLException e){
//            e.printStackTrace();
            if(e.getErrorCode() == 40000){
                return false;
            }
        } catch (SQLException e){
//            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(sqlParseCheck("aselect * from a"));
    }
}
