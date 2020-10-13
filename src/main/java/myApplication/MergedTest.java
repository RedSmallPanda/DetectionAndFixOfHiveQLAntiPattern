package myApplication;

import gen.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class MergedTest {
    public static void main(String[] args) throws Exception {
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
        String s = "select name from partitiontable where age=22 or city=\"shanghai\";";  // 在有分区的表上没有使用分区查询

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

    }
}
