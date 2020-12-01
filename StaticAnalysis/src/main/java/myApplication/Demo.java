package myApplication;

public class Demo {
    public static void main(String[] args){
        // 使用select *
        String s1 = "select * from a";
        // group by不和聚集函数搭配使用
        String s2 = "select t3.name, t3.age, avg(t3.age) from  t3 group by t3.name,t3.age;";
//        MergedTest.astCheck(s2);

        //配置项检测
        MergedTest.configCheck();
    }
}
