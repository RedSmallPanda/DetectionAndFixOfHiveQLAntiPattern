package myApplication;

import gen.HplsqlBaseListener;
import gen.HplsqlParser;
import hiveUtils.HiveUtil;
import mysqlUtils.MysqlUtil;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.SyntaxTree;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import otherUtils.FromJoinTable;
import otherUtils.OrderByCondition;
import otherUtils.SelectStmt;
import webAPI.ReturnMessageEntity;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestFixListener extends HplsqlBaseListener {
    private int joinNum = 0;
    private List<String> selectItemList;
    private List<Integer> groupByFlag = new ArrayList<>();
    private List<Integer> currentSelectListNum = new ArrayList<>();
    private Map<String,String> aliasTableName = new HashMap<>();

    private STGroup group = new STGroupFile("src/main/java/test.stg");
    private ST testST = group.getInstanceOf("select_stmt");
    private List<SelectStmt> selectStmtList = new ArrayList<>(); //考虑到可能存在subselect语句，因此用列表存储所有出现的select_stmt
    private SelectStmt selectStmt = new SelectStmt();  //HQL修复，重新生成Select语句
    public ReturnMessageEntity returnMessageEntity = new ReturnMessageEntity();

    @Override
    public void enterProgram(HplsqlParser.ProgramContext ctx){
        selectItemList = new ArrayList<>();
        selectStmtList.add(selectStmt);
    }

    //anti-pattern:不要过多使用join
    @Override
    public void exitProgram(HplsqlParser.ProgramContext ctx){
        //判断join个数是否超过一个
//        System.out.println("There is "+joinNum+" join");
        if(joinNum > 1){
            System.out.println("不要过多使用join");
            returnMessageEntity.addSuggestion("不要过多使用join");
        }
        joinNum = 0;

        //TODO:测试修复
        testST.add("stmt",selectStmt);
        String res = regexCheck(testST.render());
        //完全正确的情况添加提醒
        if(returnMessageEntity.getFixedSuggestions().size() == 0){
            returnMessageEntity.addSuggestion("Correct HQL.");
        }
        System.out.println("-Fixed HiveQL:\n"+res+"\n");
        returnMessageEntity.setFixedHiveql(res);
    }

    public String regexCheck(String s){
        s =dateSubIntervalCheck(s);
        return s;
    }

    // 在date_sub()中使用interval
    public String dateSubIntervalCheck(String s){
        s = s.toLowerCase();
        String pattern = "(date_\\S+?\\s*\\(.+?,\\s*)(interval\\s*'*(\\d*)'*\\s*day(s)?)\\)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(s);
        boolean printFlag = false;
        while(m.find( )) {
            if(!printFlag){
                System.out.println("Be careful! Using \"interval\" in \"date_sub()\" will cause error!");
                returnMessageEntity.addSuggestion("Be careful! Using \"interval\" in \"date_sub()\" will cause error!");
                printFlag = true;
            }
            s = s.replaceFirst(m.group(2), m.group(3));
        }

        return s;
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
                System.out.println("Warning! Column selected should be concluded in group by");
                returnMessageEntity.addSuggestion("Warning! Column selected should be concluded in group by");
            }
            //TODO:修复语句添加group by 条件
            for(String s:selectItemList){
                selectStmt.addGroupByCondition(s);
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

    //anti-pattern: select的列未在group by中
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
        //TODO:为了修复，构造select item
        String alias = null;
        if(ctx.select_list_alias() != null){
            alias = ctx.select_list_alias().ident().getText();
        }
        selectStmt.addColumn(ctx.expr().getText(), alias);
    }

    //anti-pattern:条件允许时，没有将条目少的表放在join左侧，条目多的表放在join右侧
    @Override
    public void enterFrom_clause(HplsqlParser.From_clauseContext ctx) {
//        //重置join个数
//        joinNum = 0;
        String tableName1 = "";
        String tableName2 = "";
        String alias1 = "";
        String alias2 = "";
        //有join操作
        if(ctx.from_join_clause().size() != 0) {
            //两张表都是普通的表
            if(ctx.from_table_clause().from_table_name_clause() != null && ctx.from_join_clause(0).from_table_clause().from_table_name_clause() != null){
                tableName1 = ctx.from_table_clause().from_table_name_clause().table_name().getText();
                if(ctx.from_table_clause().from_table_name_clause().from_alias_clause() != null){
                    alias1 = ctx.from_table_clause().from_table_name_clause().from_alias_clause().ident().getText();
                }else{
                    alias1 = null;
                }
                aliasTableName.put(alias1,tableName1);

                //TODO:修复过程构建table token
                FromJoinTable tempTable1 = new FromJoinTable();
                tempTable1.setNameAlias(new String[]{tableName1,alias1});
                selectStmt.addTable(tempTable1);
                tableName2 = ctx.from_join_clause(0).from_table_clause().from_table_name_clause().table_name().getText();
                if(ctx.from_join_clause(0).from_table_clause().from_table_name_clause().from_alias_clause() != null){
                    alias2 = ctx.from_join_clause(0).from_table_clause().from_table_name_clause().from_alias_clause().ident().getText();
                }else{
                    alias2 = null;
                }
                FromJoinTable tempTable2 = new FromJoinTable();
                tempTable2.setNameAlias(new String[]{tableName2,alias2});
                aliasTableName.put(alias2,tableName2);

                //TODO:修复过程构建table token
                tempTable2.setJoinType(ctx.from_join_clause(0).from_join_type_clause().getText());
                tempTable2.setOnCondition(ctx.from_join_clause(0).bool_expr().getText());
//                selectStmt.addTable(tempTable2);


                if(!MysqlUtil.compareTwoTableRowNum(tableName1, tableName2)){
                    //System.out.println(tableName1 + " " + tableName2);
                    //System.out.println(MysqlUtil.compareTwoTableRowNum(tableName1,tableName2));
                    System.out.println("Please put the table containing less records on the left side of join. " +
                            "Or check if the metaData of related tables is correct.");
                    returnMessageEntity.addSuggestion("Please put the table containing less records on the left side of join. " +
                            "Or check if the metaData of related tables is correct.");
                    selectStmt.setDataImbalanced(true);
                };

            }
            //有subselect的情况
            else if(ctx.from_table_clause().from_table_name_clause() != null){
                tableName1 = ctx.from_table_clause().from_table_name_clause().table_name().getText();
                if(ctx.from_table_clause().from_table_name_clause().from_alias_clause() != null){
                    alias1 = ctx.from_table_clause().from_table_name_clause().from_alias_clause().ident().getText();
                }else{
                    alias1 = null;
                }
                aliasTableName.put(alias1,tableName1);

                FromJoinTable tempTable1 = new FromJoinTable();
                tempTable1.setNameAlias(new String[]{tableName1,alias1});
                selectStmt.addTable(tempTable1);
            }
        }
        //仅存在一张表
        else if(ctx.from_table_clause().from_table_name_clause() != null){
            tableName1 = ctx.from_table_clause().from_table_name_clause().table_name().getText();
            if(ctx.from_table_clause().from_table_name_clause().from_alias_clause() != null){
                alias1 = ctx.from_table_clause().from_table_name_clause().from_alias_clause().ident().getText();
            }
            else{
                alias1 = null;
            }
            aliasTableName.put(alias1,tableName1);

            //TODO:修复过程构建table token
            FromJoinTable tempTable1 = new FromJoinTable();
            tempTable1.setNameAlias(new String[]{tableName1,alias1});
            selectStmt.addTable(tempTable1);
        }

        tableName = ctx.getStop().getText();
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

        //TODO:修复构造from_join_clause
        if(ctx.from_table_clause().from_table_name_clause() != null) {
            String tableName = ctx.from_table_clause().from_table_name_clause().table_name().getText();
            String tableAlias;
            if (ctx.from_table_clause().from_table_name_clause().from_alias_clause() != null) {
                tableAlias = ctx.from_table_clause().from_table_name_clause().from_alias_clause().ident().getText();
            } else {
                tableAlias = null;
            }
            FromJoinTable tempTable = new FromJoinTable();
            tempTable.setNameAlias(new String[]{tableName, tableAlias});
            tempTable.setJoinType(ctx.from_join_type_clause().getText());
            tempTable.setOnCondition(ctx.bool_expr().getText());
            selectStmt.addTable(tempTable);

            //TODO:修复两表顺序
            if(selectStmt.getDataImbalanced() != null) {
                selectStmt.switchJoinTables();
            }
        }
        else if(ctx.from_table_clause().from_subselect_clause() != null){
            String subSelectAlias;
            if (ctx.from_table_clause().from_subselect_clause().from_alias_clause() != null) {
                subSelectAlias = ctx.from_table_clause().from_subselect_clause().from_alias_clause().ident().getText();
            } else {
                subSelectAlias = null;
            }
            FromJoinTable tempTable = new FromJoinTable();
            SelectStmt currSelectStmt = new SelectStmt();
            selectStmtList.add(currSelectStmt);
            tempTable.setSubSelectStmt(currSelectStmt);
            tempTable.setSubSelectAlias(subSelectAlias);
            tempTable.setSubSelect(true);
            tempTable.setJoinType(ctx.from_join_type_clause().getText());
            tempTable.setOnCondition(ctx.bool_expr().getText());
            selectStmt.addTable(tempTable);
            selectStmt = currSelectStmt;
        }



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
                returnMessageEntity.addSuggestion("不要在谓词中使用函数");
            }

            //判断是否在on后的布尔表达式中进行了四则运算
            if (leftSymbol.T_ADD() == null && leftSymbol.T_SUB() == null && leftSymbol.T_MUL() == null && leftSymbol.T_DIV() == null
                    && rightSymbol.T_ADD() == null && rightSymbol.T_SUB() == null && rightSymbol.T_MUL() == null && rightSymbol.T_DIV() == null) {
                //什么都不干
            } else {
                System.out.println("不要在join子句中进行运算");
                returnMessageEntity.addSuggestion("不要在join子句中进行运算");
            }

            //TODO:处理别名的情况
            //判断是否将不同数据类型字段进行join
            if(!MysqlUtil.compareParamType(leftSymbol.getChild(0).getText(), rightSymbol.getChild(0).getText())){
                System.out.println("不要将不同数据类型字段进行join");
                returnMessageEntity.addSuggestion("不要将不同数据类型字段进行join");
            }

            //判断是否存在数据倾斜
            String[] table1 = leftSymbol.getChild(0).getText().split("\\.");
            String[] table2 = rightSymbol.getChild(0).getText().split("\\.");
            if(table1.length >= 2 || table2.length >= 2){
                String tableName1 = table1[0];
                String column1 = table1[1];
                String tableName2 = table2[0];
                String column2 = table2[1];
                tableName1 = aliasTableName.get(tableName1) == null?tableName1:aliasTableName.get(tableName1);
                tableName2 = aliasTableName.get(tableName2) == null?tableName2:aliasTableName.get(tableName2);
                List<String> joinParams = new ArrayList<>();
                joinParams.add(tableName1);
                joinParams.add(column1);
                joinParams.add(tableName2);
                joinParams.add(column2);
                returnMessageEntity.setJoinParams(joinParams);
//                if(HiveUtil.isDataImbalanced(tableName1, column1, tableName2, column2)){
//                    System.out.println("可能存在数据倾斜！");
//                    returnMessageEntity.addSuggestion("可能存在数据倾斜！");
//                }
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

        //TODO:修复构建where条件部分
        selectStmt.setWhereCondition(boolBinaryContext.getText());

        HplsqlParser.ExprContext leftSymbol = boolBinaryContext.expr(0);
        HplsqlParser.ExprContext rightSymbol = boolBinaryContext.expr(1);

        //判断是否在where后的布尔表达式中使用了函数
        if(leftSymbol.expr_func() == null && leftSymbol.expr_agg_window_func() == null
                && rightSymbol.expr_func() == null && rightSymbol.expr_agg_window_func() == null){
            //什么都不干
        }
        else{
            System.out.println("不要在谓词中使用函数");
            returnMessageEntity.addSuggestion("不要在谓词中使用函数");
        }

        //判断是否在where后的布尔表达式中进行了四则运算
        if(leftSymbol.T_ADD() == null && leftSymbol.T_SUB() == null && leftSymbol.T_MUL() == null && leftSymbol.T_DIV() == null
                && rightSymbol.T_ADD() == null && rightSymbol.T_SUB() == null && rightSymbol.T_MUL() == null && rightSymbol.T_DIV() == null){

        }
        else{
            System.out.println("不要在where子句中进行运算");
            returnMessageEntity.addSuggestion("不要在where子句中进行运算");
        }
    }
    // 使用select *
    @Override
    public void enterSelect_list_asterisk(HplsqlParser.Select_list_asteriskContext ctx){
        System.out.println("Be careful! Using \"select *\" will cause poor performance! Please select " +
                "specific column.");
        returnMessageEntity.addSuggestion("Be careful! Using \"select *\" will cause poor performance! Please select " +
                "specific column.");
        selectStmt.addColumn("column1",null);
        selectStmt.addColumn("...",null);
        selectStmt.addColumn("columnN",null);
    }

    // 使用having进行过滤
    @Override
    public void enterNon_reserved_words(HplsqlParser.Non_reserved_wordsContext ctx){
        if(ctx.getText().equalsIgnoreCase("having")){
            System.out.println("Be careful! Using \"having\" will cause poor performance! Please use \"where\".");
            returnMessageEntity.addSuggestion("Be careful! Using \"having\" will cause poor performance! Please use \"where\".");
        }
    }

    public String getChildString(ParseTree tree) {
        if (tree.getChildCount() == 0) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();

            for(int i = 0; i < tree.getChildCount(); ++i) {
                builder.append(tree.getChild(i).getText());
                builder.append(" ");
            }

            return builder.toString();
        }
    }

    @Override
    public void enterHaving_clause(HplsqlParser.Having_clauseContext ctx){
        if(groupByFlag.size() == 0){
            selectStmt.setWhereCondition(ctx.bool_expr().getText());
            System.out.println("Be careful! Using \"having\" will cause poor performance! Please use \"where\".");
            returnMessageEntity.addSuggestion("Be careful! Using \"having\" will cause poor performance! Please use \"where\".");
        }else{
            selectStmt.setHavingCondition(getChildString(ctx.bool_expr().getChild(0).getChild(0)));
        }
    }

    // 使用order by
    @Override
    public void enterOrder_by_clause(HplsqlParser.Order_by_clauseContext ctx){
        System.out.println("Be careful! Using \"order by\" will cause poor performance! Please use \"sort by\".");
        returnMessageEntity.addSuggestion("Be careful! Using \"order by\" will cause poor performance! Please use \"sort by\".");

        //TODO:修复过程构造Order by
        List<ParseTree> exprContext = ctx.children;
        //去除order 和 by
        for(int i = 0;i<2;i++){
            exprContext.remove(0);
        }
        OrderByCondition condition = new OrderByCondition();
        for(ParseTree tree : exprContext){
            if(tree.getText().equalsIgnoreCase("asc") || tree.getText().equalsIgnoreCase("desc")){
                condition.setOrder(tree.getText());
                selectStmt.addOrderByCondition(condition);
            }
            else if(!tree.getText().equalsIgnoreCase(",")){
                if(condition.getOrder() == null){
                    selectStmt.addOrderByCondition(condition);
                }
                condition = new OrderByCondition();
                condition.setExpr(tree.getText());
            }
        }
        if(condition.getOrder() == null){
            selectStmt.addOrderByCondition(condition);
        }
    }

    // 在date_sub()中使用interval
    private boolean intervalInDatesub = false;

    @Override
    public void enterExpr_interval(HplsqlParser.Expr_intervalContext ctx){
        intervalInDatesub = true;
    }

    // group by不和聚集函数搭配使用
    // anti-pattern: select的列未在group by中
    // 使用having进行过滤: 若搭配group by，则OK
    private boolean isGather = false;

    @Override
    public void enterGroup_by_clause(HplsqlParser.Group_by_clauseContext ctx){
        if(!isGather){
            System.out.println("Be careful! \"group by\" should be used with aggregate function!");
            returnMessageEntity.addSuggestion("Be careful! \"group by\" should be used with aggregate function!");
        }
        groupByFlag.add(1);
        List<HplsqlParser.ExprContext> exprContext = ctx.expr();
        //遍历group后的expr列表并将其中元素从selectItemList中删除
        for(HplsqlParser.ExprContext expr:exprContext){

            //TODO:修复语句构造group by条件
            selectStmt.addGroupByCondition(expr.getText());

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
            returnMessageEntity.addSuggestion("Be careful! Using \"count(distinct ...)\" may cause poor performance! " +
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
                returnMessageEntity.addSuggestion("Be careful! Data type after \"then\" and \"else\" is different!");
            }
        }
    }

    // 建多个相同的表
    HashSet<String> colName = new HashSet<>();
    String createTableName = null;
    
    @Override
    public void enterTable_name(HplsqlParser.Table_nameContext ctx) {
        createTableName = ctx.getText();
    }
    
    @Override
    public void enterCreate_table_columns_item(HplsqlParser.Create_table_columns_itemContext ctx){
        colName.add(ctx.getStart().getText());
    }

    @Override
    public void exitCreate_table_stmt(HplsqlParser.Create_table_stmtContext ctx){
        String table = MysqlUtil.hasSameTable(createTableName, colName);
        if(table != null){
            String tip = "Creating table \""+createTableName+"\" is similar to existed table \""+table+"\", please check again.";
            System.out.println(tip);
            returnMessageEntity.addSuggestion(tip);
        }
    }

    // 在有分区的表上没有使用分区查询
    private boolean hasWhere=false;
    List<String> whereItemList = new ArrayList<>();

    private String tableName;
    @Override
    public void exitSubselect_stmt(HplsqlParser.Subselect_stmtContext ctx){
        HashSet<String> partCol = MysqlUtil.partitionCheck(tableName, whereItemList);
        if(partCol != null){
            System.out.print("Warning! Please utilize partition in the query. Partition: ");
            returnMessageEntity.addSuggestion("Warning! Please utilize partition in the query.");
            String whereCd = selectStmt.getWhereCondition();
            StringBuilder whereCondition = new StringBuilder(whereCd==null ? "" : whereCd);
            boolean isFirst = true;
            for(String part : partCol){
                if(isFirst){
                    System.out.print(part);
                    isFirst = false;
                }else{
                    System.out.print(", "+part);
                }
                if(whereCondition.length() != 0){
                    whereCondition.append(" and ");
                }
                whereCondition.append(part).append("?");
            }
            selectStmt.setWhereCondition(whereCondition.toString());
        }
    }

    @Override
    public void enterBool_expr_atom(HplsqlParser.Bool_expr_atomContext ctx){
        if(hasWhere){
            whereItemList.add(ctx.getStart().getText());
        }
    }

    @Override
    public void enterFrom_subselect_clause(HplsqlParser.From_subselect_clauseContext ctx){
    }

    @Override
    public void exitFrom_subselect_clause(HplsqlParser.From_subselect_clauseContext ctx){
        selectStmtList.remove(selectStmtList.size()-1);
        if(selectStmtList.size()>0){
            selectStmt = selectStmtList.get(selectStmtList.size()-1);
        }
    }
}
