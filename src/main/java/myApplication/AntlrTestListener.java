package myApplication;

import gen.*;

import java.util.ArrayList;
import java.util.List;

public class AntlrTestListener extends HplsqlBaseListener {
    private int joinNum = 0;
    private List<String> selectItemList;
    private int groupByFlag = 0;

    @Override
    public void enterProgram(HplsqlParser.ProgramContext ctx){
//        System.out.println("Hello Antlr!");
    }

    //anti-pattern:不要过多使用join4
    @Override
    public void exitProgram(HplsqlParser.ProgramContext ctx){
        //判断join个数是否超过一个
//        System.out.println("There is "+joinNum+" join");
        if(joinNum > 1){
            System.out.println("不要过多使用join");
        }
        joinNum = 0;
    }

    //anti-pattern:select的列未在group by中
    @Override
    public void enterSelect_stmt(HplsqlParser.Select_stmtContext ctx){
        selectItemList = new ArrayList<>();
    }

    //anti-pattern:select的列未在group by中
    @Override
    public void exitSelect_stmt(HplsqlParser.Select_stmtContext ctx){
        if(groupByFlag == 1){
            if(selectItemList.size() != 0){
                System.out.println("select的列未在group by中");
            }
            groupByFlag = 0;
        }
    }

    //anti-pattern:select的列未在group by中
    @Override
    public void enterSelect_list_item(HplsqlParser.Select_list_itemContext ctx) {
        //对于每个select item,将其添加进selectItemList
        if(ctx.expr().expr_agg_window_func() == null) {
            selectItemList.add(ctx.expr().getText());
        }
    }

    //anti-pattern:使用 *
    @Override
    public void enterSelect_list_asterisk(HplsqlParser.Select_list_asteriskContext ctx){
        System.out.println("Be careful! Using \"*\" will cause poor performance!");
    }

    //anti-pattern:select的列未在group by中
    @Override
    public void enterGroup_by_clause(HplsqlParser.Group_by_clauseContext ctx) {
        groupByFlag = 1;
        List<HplsqlParser.ExprContext> exprContext = ctx.expr();
        //遍历group后的expr列表并将其中元素从selectItemList中删除
        for(HplsqlParser.ExprContext expr:exprContext){
            if(selectItemList.contains(expr.getText())){
                selectItemList.remove(expr.getText());
            }
        }
    }

    @Override
    public void enterFrom_clause(HplsqlParser.From_clauseContext ctx) {
        //重置join个数
        joinNum = 0;
    }

    @Override
    public void exitFrom_clause(HplsqlParser.From_clauseContext ctx) {
//        //判断join个数是否超过一个
//        System.out.println("There is "+joinNum+" join");
//        if(joinNum > 1){
//            System.out.println("不要过多使用join");
//        }
//        joinNum = 0;
    }

    //anti-pattern:过多使用join
    //anti-pattern:不要在join子句中进行运算
    @Override
    public void enterFrom_join_clause(HplsqlParser.From_join_clauseContext ctx) {
        //累加join个数
        joinNum = joinNum + 1;

        if(ctx.T_ON() != null) {
            //判断join子句中是否存在 + / - / * / /
            HplsqlParser.Bool_expr_binaryContext boolBinaryContext;
            //表达式两侧不存在括号的情况
            if (ctx.bool_expr().T_OPEN_P() == null) {
                boolBinaryContext = ctx.bool_expr().bool_expr_atom().bool_expr_binary();
            }
            //表达式两侧存在括号
            else {
                boolBinaryContext = ctx.bool_expr().bool_expr(0).bool_expr_atom().bool_expr_binary();
            }
            HplsqlParser.ExprContext leftSymbol = boolBinaryContext.expr(0);
            HplsqlParser.ExprContext rightSymbol = boolBinaryContext.expr(1);

            //判断是否在on后的布尔表达式中使用了函数
            if(leftSymbol.expr_func() == null && leftSymbol.expr_agg_window_func() == null
                && rightSymbol.expr_func() == null && rightSymbol.expr_agg_window_func() == null){
                //什么都不干
            }
            else{
                System.out.println("不要在谓词中使用函数");
            }

            //判断是否在on后的布尔表达式中进行了四则运算
            if (leftSymbol.T_ADD() == null && leftSymbol.T_SUB() == null && leftSymbol.T_MUL() == null && leftSymbol.T_DIV() == null
                    && rightSymbol.T_ADD() == null && rightSymbol.T_SUB() == null && rightSymbol.T_MUL() == null && rightSymbol.T_DIV() == null) {
                //什么都不干
            } else {
                System.out.println("不要在join子句中进行运算");
            }
        }
    }

    //anti-pattern:不要在where字句中进行运算
    @Override
    public void enterWhere_clause(HplsqlParser.Where_clauseContext ctx) {
        HplsqlParser.Bool_expr_binaryContext boolBinaryContext;
        if(ctx.bool_expr().T_OPEN_P() == null){
            boolBinaryContext = ctx.bool_expr().bool_expr_atom().bool_expr_binary();
        }
        else{
            boolBinaryContext = ctx.bool_expr().bool_expr(0).bool_expr_atom().bool_expr_binary();
        }
        HplsqlParser.ExprContext leftSymbol = boolBinaryContext.expr(0);
        HplsqlParser.ExprContext rightSymbol = boolBinaryContext.expr(1);

        //判断是否在where后的布尔表达式中使用了函数
        if(leftSymbol.expr_func() == null && leftSymbol.expr_agg_window_func() == null
                && rightSymbol.expr_func() == null && rightSymbol.expr_agg_window_func() == null){
            //什么都不干
        }
        else{
            System.out.println("不要在谓词中使用函数");
        }

        //判断是否在where后的布尔表达式中进行了四则运算
        if(leftSymbol.T_ADD() == null && leftSymbol.T_SUB() == null && leftSymbol.T_MUL() == null && leftSymbol.T_DIV() == null
                && rightSymbol.T_ADD() == null && rightSymbol.T_SUB() == null && rightSymbol.T_MUL() == null && rightSymbol.T_DIV() == null){

        }
        else{
            System.out.println("不要在where子句中进行运算");
        }
    }
}
