package otherUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class SelectStmt {
    public List<ColumnOptAlias> columns;
    public List<FromJoinTable> tables;
    public String whereCondition;
    public List<String> groupByConditions;
    public String havingCondition;
    public List<OrderByCondition> orderByConditions;

    public SelectStmt(){
        columns = new ArrayList<>();
        tables = new ArrayList<>();
        groupByConditions = new ArrayList<>();
        orderByConditions = new ArrayList<>();
    }

    public SelectStmt(List<ColumnOptAlias> columns, List<FromJoinTable> tables) {
        this.columns = columns;
        this.tables = tables;
    }


    public void addColumn(String columnName,String alias){
        columns.add(new ColumnOptAlias(columnName,alias));
    }

    public void addTable(String tableName,String alias,String joinType,String onCondition){
        tables.add(new FromJoinTable(new TableOptAlias(tableName,alias),joinType,onCondition));
    }

    public void addGroupByCondition(String condition){
        groupByConditions.add(condition);
    }

    public void addOrderByCondition(OrderByCondition condition){
        orderByConditions.add(condition);
    }

    public List<ColumnOptAlias> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnOptAlias> columns) {
        this.columns = columns;
    }

    public List<FromJoinTable> getTables() {
        return tables;
    }

    public void setTables(List<FromJoinTable> tables) {
        this.tables = tables;
    }

    public String getWhereCondition() {
        return whereCondition;
    }

    public void setWhereCondition(String whereCondition) {
        this.whereCondition = whereCondition;
    }

    public List<String> getGroupByConditions() {
        return groupByConditions;
    }

    public void setGroupByConditions(List<String> groupByConditions) {
        this.groupByConditions = groupByConditions;
    }

    public String getHavingCondition() {
        return havingCondition;
    }

    public void setHavingCondition(String havingCondition) {
        this.havingCondition = havingCondition;
    }

    public List<OrderByCondition> getOrderByConditions() {
        return orderByConditions;
    }

    public void setOrderConditions(List<OrderByCondition> orderByConditions) {
        this.orderByConditions = orderByConditions;
    }

    public void switchJoinTables(){
        String[] temp = tables.get(tables.size()-1).getNameAlias();
        tables.get(tables.size()-1).setNameAlias(tables.get(tables.size()-2).getNameAlias());
        tables.get(tables.size()-2).setNameAlias(temp);
    }
}
