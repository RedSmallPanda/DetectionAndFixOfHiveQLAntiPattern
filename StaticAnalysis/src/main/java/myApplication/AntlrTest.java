package myApplication;

import gen.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class AntlrTest {
    public static void main(String[] args) throws Exception {
//        String s = "select student.name from student;";
        //创建输入字节流，在输入内容结束时记得输入EOF(mac:command+D,win:ctrl+D)
        ANTLRInputStream input = new ANTLRInputStream(System.in);
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
        walker.walk(new AntlrTestListener(),tree);

//        System.out.println(tree.toStringTree(parser));

    }
}
