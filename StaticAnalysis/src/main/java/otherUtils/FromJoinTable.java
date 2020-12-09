package otherUtils;

public class FromJoinTable {
    public TableOptAlias tableNameAlias;
    public String joinType;
    public String onCondition;
    public Boolean isSubSelect;
    public SelectStmt subSelectStmt;
    public String subSelectAlias;

    public FromJoinTable(){
        tableNameAlias = new TableOptAlias();

    }

    public FromJoinTable(TableOptAlias tableNameAlias, String joinType, String onCondition, Boolean isSubSelect, SelectStmt subSelectStmt, String subSelectAlias) {
        this.tableNameAlias = tableNameAlias;
        this.joinType = joinType;
        if(joinType != null && !joinType.equals("")) {
            String revertedJoinType = joinType.substring(0, joinType.toLowerCase().indexOf("join")).toLowerCase();
            if(revertedJoinType.contains("outer")){
                revertedJoinType = revertedJoinType.substring(0,revertedJoinType.indexOf("outer"))+" outer";
            }
            this.joinType = revertedJoinType + " join";
        }
        this.onCondition = onCondition;
        this.isSubSelect = isSubSelect;
        this.subSelectStmt = subSelectStmt;
        this.subSelectAlias = subSelectAlias;
    }

    public String[] getNameAlias() {
        return tableNameAlias.getNameAlias();
    }

    public void setNameAlias(String[] nameAlias) {
        tableNameAlias.setNameAlias(nameAlias);
    }

    @Override
    public String toString() {
        return "FromJoinTable{" +
                "tableNameAlias=" + tableNameAlias +
                ", joinType='" + joinType + '\'' +
                ", onCondition='" + onCondition + '\'' +
                '}';
    }

    public TableOptAlias getTableNameAlias() {
        return tableNameAlias;
    }

    public void setTableNameAlias(TableOptAlias tableNameAlias) {
        this.tableNameAlias = tableNameAlias;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
        if(joinType != null && !joinType.equals("")) {
            String revertedJoinType = joinType.substring(0, joinType.toLowerCase().indexOf("join")).toLowerCase();
            if(revertedJoinType.contains("outer")){
                revertedJoinType = revertedJoinType.substring(0,revertedJoinType.indexOf("outer"))+" outer";
            }
            this.joinType = revertedJoinType + " join";
        }
    }

    public String getOnCondition() {
        return onCondition;
    }

    public void setOnCondition(String onCondition) {
        this.onCondition = onCondition;
    }

    public Boolean getSubSelect() {
        return isSubSelect;
    }

    public void setSubSelect(Boolean subSelect) {
        isSubSelect = subSelect;
    }

    public SelectStmt getSubSelectStmt() {
        return subSelectStmt;
    }

    public void setSubSelectStmt(SelectStmt subSelectStmt) {
        this.subSelectStmt = subSelectStmt;
    }

    public String getSubSelectAlias() {
        return subSelectAlias;
    }

    public void setSubSelectAlias(String subSelectAlias) {
        this.subSelectAlias = subSelectAlias;
    }
}
