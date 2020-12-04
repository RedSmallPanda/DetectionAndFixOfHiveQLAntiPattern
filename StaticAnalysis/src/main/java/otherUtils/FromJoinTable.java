package otherUtils;

public class FromJoinTable {
    public TableOptAlias tableNameAlias;
    public String joinType;
    public String onCondition;

    public FromJoinTable(TableOptAlias tableNameAlias, String joinType, String onCondition) {
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
}
