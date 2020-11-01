package myApplication;

import gen.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import mysqlUtils.MysqlUtil;
import otherUtils.stringUtil;

public class MergedTest {
    public static void main(String[] args) throws Exception {
        String s ="select count(distinct col1) from (select col2,col1 from t2 where col2 >100) as t1";
//        String s = "select t1.col1, t1.col2, t2.col3 from t1 join t2 on t1.col1 = t2.col1 group by t2.col1, t1.col2, t2.col3";
//        String s ="select sum(col1), col2, distinct col3 from t1 group by col1, col2";
//        String s = "select col1,col2 from (select t1.col1,t1.col2,t2.col3 from t1 join t2 on t1.id = t2.id having t1.col1 >100) as t3";
//        String s = "select t1.col1, t2.col2 from table1 as t1 join table2 as t2 on count(t1.col1)=t2.col2";
        //String s ="select count(col1) from (select distinct col1 from t2 where col2 > 100) as t1";
//        String s ="select t2.age,t1.name from t2 inner join t1 on t2.id = t1.id";
        //String s ="select t1.name,t2.age from t1 inner join t2 on t1.id = t2.id";//Please put the table containing less records on the left side of join.
        //String s = "select * from a";  // 使用select *
        /*String s = "SELECT C.CustomerID, C.Name, Count(S.SalesID)\n" +
                "FROM Customers as C\n" +
                "   INNER JOIN Sales as S\n" +
                "   ON C.CustomerID = S.CustomerID\n" +
                "GROUP BY C.CustomerID, C.Name\n" +
                "HAVING S.LastSaleDate BETWEEN '1/1/2019' AND '12/31/2019';";  // 使用having进行过滤*/
        //String s = "select b from a order by b";  // 使用order by
        //String s = "select date_sub('2020-9-16', interval 10 days) from a";  // 在date_sub()中使用interval
        //String s = "select pokes.col1,unique1.col2 from unique1 left join pokes on pokes.id = unique1.id;";  // group by不和聚集函数搭配使用
        //String s = "select t3.col1,t3.col2,sum(t3.col1) from (select t1.col1,t2.col2 from t1 join t2 on t1.id = t2.id group by t1.col1) as t3 group by t3.col1,t3.col2;";  // group by不和聚集函数搭配使用

        //String s = "CREATE TABLE tableD (bar int, foo float);";  // 建多个相同表
        //String s = "select partitiontable.col1,test.col2 from partitiontable left join pokes on pokes.foo = 100;";  // 在有分区的表上没有使用分区查询
<<<<<<< HEAD
        //String s = "select name from partitiontable where age=22;";  // 在有分区的表上没有使用分区查询
       // String s = "select count( distinct cookie ) from weblogs where dt <= ${today} and dt >= ${90daysAgo}";
=======
        String ss = "select t3.subname,t4.age from (select t1.subname,t2.age from t1 inner join t2 on t1.id = t2.id) as t3 inner join t4 on t4.name = t3.name;";  // 在有分区的表上没有使用分区查询
        String s = stringUtil.join2innerJoin(ss);

>>>>>>> 52e5cc2c348365c1700d9c7eb4355c630efa520f
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
        walker.walk(new MergedListener(),tree);

        //System.out.println(tree.toStringTree(parser));

        //配置项检测
        MysqlUtil.configurationCheck();
    }
}
