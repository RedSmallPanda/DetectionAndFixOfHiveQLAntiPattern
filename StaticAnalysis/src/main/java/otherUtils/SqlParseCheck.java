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
            ps.execute("explain " + sql);
        } catch (HiveSQLException e){
            // HiveQL无法解析报ParseException，ErrorCode是40000
            // 还可能报语义错误
//            e.printStackTrace();
            if(e.getErrorCode() == 40000){
                // 还有可能因为安全原因拒绝检查
                return e.toString().contains("SemanticException Cartesian products are disabled for safety reasons.");
            }
        } catch (SQLException e){
            // 还可能有网络连接问题
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(sqlParseCheck("select t1.city from t1 join t2"));
    }
}
