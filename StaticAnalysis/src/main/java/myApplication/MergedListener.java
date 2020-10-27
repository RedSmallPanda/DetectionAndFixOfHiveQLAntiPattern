package myApplication;

import gen.*;
import mysqlUtils.MysqlUtil;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public class MergedListener extends HplsqlBaseListener {
    private int joinNum = 0;
    private List<String> selectItemList;
    private List<Integer> groupByFlag = new ArrayList<>();
    private List<Integer> currentSelectListNum = new ArrayList<>();

    @Override
    public void enterProgram(HplsqlParser.ProgramContext ctx){
        selectItemList = new ArrayList<>();
    }

    //anti-pattern:不要过多使用join
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
        currentSelectListNum.add(0);
    }

    //anti-pattern:select的列未在group by中
    @Override
    public void exitSelect_stmt(HplsqlParser.Select_stmtContext ctx){
        if(groupByFlag.size() > 0){
            //if(selectItemList.size() != 0){
            if(currentSelectListNum.get(currentSelectListNum.size()-1) != 0){
                System.out.println("select的列未在group by中");
            }
            groupByFlag.remove(groupByFlag.size() - 1);
            currentSelectListNum.remove(currentSelectListNum.size()-1);
        }
        else{
            for(int i = 0;i < currentSelectListNum.get(currentSelectListNum.size()-1);i++){
                selectItemList.remove(selectItemList.size() - 1);
            }
            currentSelectListNum.remove(currentSelectListNum.size()-1);
        }
    }

    //anti-pattern:select的列未在group by中
    @Override
    public void enterSelect_list_item(HplsqlParser.Select_list_itemContext ctx) {
        //对于每个select item,将其添加进selectItemList
        if(ctx.expr() == null){
            return;
        }
        if(ctx.expr().expr_agg_window_func() == null) {
            selectItemList.add(ctx.expr().getText());
            currentSelectListNum.set(currentSelectListNum.size()-1,currentSelectListNum.get(currentSelectListNum.size()-1) + 1);
        }
    }

    //anti-pattern:条件允许时，没有将条目少的表放在join左侧，条目多的表放在join右侧
    @Override
    public void enterFrom_clause(HplsqlParser.From_clauseContext ctx) {
//        //重置join个数
//        joinNum = 0;
//        if(ctx.from_join_clause().size() != 0) {
//            String tableName1 = ctx.from_table_clause().from_table_name_clause().table_name().getText();
//            String tableName2 = ctx.from_join_clause(0).from_table_clause().from_table_name_clause().table_name().getText();
//
//            if(MysqlUtil.compareTwoTableRowNum(tableName1,tableName2) == false){
//                System.out.println("Please put the table containing less records on the left side of join.");
//            };
//        }
//        tableName = ctx.getStop().getText();
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
    //anti-pattern:将不同数据类型的字段进行join
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
            //处理子表达式存在括号的情况
            if(leftSymbol.getChild(0).getText().equals("(")){
                leftSymbol = leftSymbol.expr(0);
            }
            HplsqlParser.ExprContext rightSymbol = boolBinaryContext.expr(1);
            if(rightSymbol.getChild(0).getText().equals("(")){
                rightSymbol = rightSymbol.expr(0);
            }

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

            //判断是否将不同数据类型字段进行join
            if(MysqlUtil.compareParamType(leftSymbol.getChild(0).getText(),rightSymbol.getChild(0).getText()) == false){
                System.out.println("不要将不同数据类型字段进行join");
            }
        }
    }

    //anti-pattern:不要在where字句中进行运算
    @Override
    public void enterWhere_clause(HplsqlParser.Where_clauseContext ctx) {
        hasWhere = true;
        HplsqlParser.Bool_expr_binaryContext boolBinaryContext;
        if(ctx.bool_expr().T_OPEN_P() == null){
            if(ctx.bool_expr().bool_expr_atom() == null){
                // 与其他情况可能冲突
                return;
            }
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
    // 使用select *
    @Override
    public void enterSelect_list_asterisk(HplsqlParser.Select_list_asteriskContext ctx){
        System.out.println("Be careful! Using \"select *\" will cause poor performance! Please select " +
                "specific column.");
    }

    // 使用having进行过滤
    @Override
    public void enterNon_reserved_words(HplsqlParser.Non_reserved_wordsContext ctx){
        if(ctx.getText().equalsIgnoreCase("having")){
            System.out.println("Be careful! Using \"having\" will cause poor performance! Please use \"where\".");
        }
    }

    @Override
    public void enterHaving_clause(HplsqlParser.Having_clauseContext ctx){
        System.out.println("Be careful! Using \"having\" will cause poor performance! Please use \"where\".");
    }

    // 使用order by
    @Override
    public void enterOrder_by_clause(HplsqlParser.Order_by_clauseContext ctx){
        System.out.println("Be careful! Using \"order by\" will cause poor performance! Please use \"sort by\".");
    }

    // 在date_sub()中使用interval
    private boolean intervalInDatesub = false;

    @Override
    public void enterExpr_interval(HplsqlParser.Expr_intervalContext ctx){
        intervalInDatesub = true;
    }
    
    @Override
    public void exitSelect_list(HplsqlParser.Select_listContext ctx){
        if(intervalInDatesub && ctx.getText().toLowerCase().contains("date_sub(")){
            System.out.println("Be careful! Using \"interval\" in \"date_sub()\" will cause error!");
        }
    }

    // group by不和聚集函数搭配使用
    // anti-pattern: select的列未在group by中
    private boolean isGather = false;

    @Override
    public void enterGroup_by_clause(HplsqlParser.Group_by_clauseContext ctx){
        if(!isGather){
            System.out.println("Be careful! \"group\" by should be used with aggregate function!");
        }
        groupByFlag.add(1);
        List<HplsqlParser.ExprContext> exprContext = ctx.expr();
        //遍历group后的expr列表并将其中元素从selectItemList中删除
        for(HplsqlParser.ExprContext expr:exprContext){
            if(selectItemList.contains(expr.getText())){
                selectItemList.remove(expr.getText());
                currentSelectListNum.set(currentSelectListNum.size()-1,currentSelectListNum.get(currentSelectListNum.size()-1) - 1);
            }
        }
    }

    // 在大表中(频繁)使用count(distinct )
    @Override
    public void enterExpr_agg_window_func(HplsqlParser.Expr_agg_window_funcContext ctx){
        if(ctx.getText().toLowerCase().contains("count(distinct")){
            System.out.println("Be careful! Using \"count(distinct ...)\" may cause poor performance! " +
                    "Please use \"sum...group by\"");
        }
        isGather = true;
    }

    // case语句中then和else后数据类型不一致
    // 判断整数（int）
    private boolean isInteger(String t) {
        if (null == t|| "".equals(t)) {
            return false;
        }
        Pattern chk = Pattern.compile("^[-\\+]?[\\d]*$");
        return  chk.matcher(t).matches();
    }

    @Override
    public void enterExpr_case_searched(HplsqlParser.Expr_case_searchedContext ctx){
        if(ctx.getChild(0).getText().equalsIgnoreCase("case")
        && ctx.getChild(1).getText().equalsIgnoreCase("when")){
            String s1 = ctx.getChild(4).getText();
            String s2 = ctx.getChild(6).getText();
            boolean isDouble1 = !isInteger(s1) || s1.contains("/");
            boolean isDouble2 = !isInteger(s2) || s2.contains("/");
            if(isDouble1 != isDouble2){
                System.out.println("Be careful! Data type after \"then\" and \"else\" is different!");
            }
        }
    }

    // 建多个相同的表
    HashSet<String> colName = new HashSet<>();

    @Override
    public void enterCreate_table_columns_item(HplsqlParser.Create_table_columns_itemContext ctx){
        colName.add(ctx.getStart().getText());
    }

    @Override
    public void exitCreate_table_stmt(HplsqlParser.Create_table_stmtContext ctx){
        if(MysqlUtil.hasSameTable(colName)){
            System.out.println("重复建表！");
        }
    }

    // 在有分区的表上没有使用分区查询
    private boolean hasWhere=false;
    List<String> whereItemList = new ArrayList<>();

    private String tableName;
    @Override
    public void exitSubselect_stmt(HplsqlParser.Subselect_stmtContext ctx){
        if(!MysqlUtil.usePartitionCorrect(tableName, whereItemList)){
            System.out.println("Be careful! 在有分区的表上没有使用分区查询!");
        }
    }

    @Override
    public void enterBool_expr_atom(HplsqlParser.Bool_expr_atomContext ctx){
        if(hasWhere){
            whereItemList.add(ctx.getStart().getText());
        }
    }

}
