package otherUtils;

public class OrderByCondition {
    public String order;
    public String expr;

    public OrderByCondition() {

    }

    public OrderByCondition(String order, String expr) {
        this.order = order;
        this.expr = expr;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }
}
