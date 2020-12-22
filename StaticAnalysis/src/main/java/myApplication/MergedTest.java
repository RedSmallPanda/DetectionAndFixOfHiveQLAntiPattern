package myApplication;

import gen.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import mysqlUtils.MysqlUtil;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import otherUtils.stringUtil;
import webAPI.ReturnMessageEntity;
import webAPI.StaticCheckImp;

public class MergedTest {
    public static ReturnMessageEntity astCheck(String s) {
        System.out.println("-HiveQL:"+s);
        System.out.println("-Suggestion:");
        try{
            s = stringUtil.join2innerJoin(s);
        }catch (Exception e){
            e.printStackTrace();
        }
        //创建输入字节流
        ANTLRInputStream input = new ANTLRInputStream(s);
        //构建词法分析器
        HplsqlLexer lexer = new HplsqlLexer(input);
        //将词存储在内存中
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        //构建语法分析器
        HplsqlParser parser = new HplsqlParser(tokens);
        //构建解析树
        ParseTree tree = parser.program();

        //构建树遍历器
        ParseTreeWalker walker = new ParseTreeWalker();
        //第一个参数是自己写的解析器
//        walker.walk(new MergedListener(),tree);
        try {
            TestFixListener testFixListener = new TestFixListener();
            walker.walk(testFixListener, tree);
            ReturnMessageEntity returnMessageEntity = testFixListener.returnMessageEntity;
            return returnMessageEntity;
        }
        catch(Exception e){
            System.out.println("There is something wrong with the HQL");
            return null;
        }
    }

    public static void configCheck(){
        MysqlUtil.configurationCheck();
    }

    public static void main(String[] args) throws Exception {
        // 使用select *
//        String s = "select * from a";

        // 使用order by
//        String s = "select b from a order by b";

        // group by不和聚集函数搭配使用
//        String s = "select pokes.col1,unique1.col2 from unique1 left join pokes on pokes.id = unique1.id;";
//        String s = "select t3.col1,t3.col2,sum(t3.col1) from (select t1.col1,t2.col2 from t1 join t2 on t1.id = t2.id group by t1.col1) as t3 group by t3.col1,t3.col2;";

        // 建多个相同表
//        String s = "CREATE TABLE tableD (bar int, foo float);";

        // 条件允许时，没有将条目少的表放在join左侧，条目多的表放在join右侧
//        String s = "SELECT t1.name, t2.age FROM mrtest_10 as t1 JOIN mrtest_500 as t2 ON t1.city=t2.city;";
//        String s = "SELECT t1.name, t2.age FROM mrtest_500 as t1 JOIN mrtest_10 as t2 ON t1.city=t2.city;";  // AP
//        String s = "select p1.name from mrtest_500 p1 join mrtest_50 p2 on p1.city = p2.city where p1.city = 1;";  // AP

        // 使用having进行过滤 https://blog.csdn.net/high2011/article/details/82686858
//        String s = "SELECT id, avg(age) avaAge from table001 group by id having id >='20180901';";
//        String s = "SELECT id from table001 having id >='20180901';";  // AP
        /*String s = "SELECT C.CustomerID, C.Name, Count(S.SalesID)\n" +
                "FROM Customers as C\n" +
                "   INNER JOIN Sales as S\n" +
                "   ON C.CustomerID = S.CustomerID\n" +
                "GROUP BY C.CustomerID, C.Name\n" +
                "HAVING S.LastSaleDate BETWEEN '1/1/2019' AND '12/31/2019';";*/

        // 在date_sub()中使用interval
//        String s = "Select date_add('2020-9-16', interval '10' day) from a;";  // AP
//        String s = "Select mrtest_10.name, mrtest_500.age FROM mrtest_10 inner JOIN mrtest_500 on mrtest_10.age = mrtest_500.age group by mrtest_10.name;";  // AP
//        String s = "select date_sub('2020-9-16',10) From a;";
//        String s = "select '2020-9-16' - interval '10' day From a;";

        // 在有分区的表上没有使用分区查询
//        String s = "select name from partitiontable;";  // AP
//        String s = "select name from partitiontable where name='cn';";  // AP
//        String s = "select name from partitiontable where city='changzhou';";

        // select的列未在group by中
//        String s = "select name, city, avg(age) from t group by name;";  // AP
//        String s = "select name, city, avg(age) from t group by city, name;";

        // subselect
//        String s = "select p1.name from mrtest_500 p1 join (select city from mrtest_50) p2 on p1.city = p2.city where p1.city = 1;";

        // 不要过多使用join
        String s = "select t1.name,t2.age from t1 inner join t2 on t1.id = t2.id;";

        astCheck(s);

        //System.out.println(tree.toStringTree(parser));

        //配置项检测
//        configCheck();
    }
}
