package otherUtils;

public class TableOptAlias {
    public String name;
    public String alias;

    public TableOptAlias(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String[] getNameAlias() {
        return new String[]{name, alias};
    }

    public void setNameAlias(String[] nameAlias) {
        this.name = nameAlias[0];
        this.alias = nameAlias[1];
    }
}
