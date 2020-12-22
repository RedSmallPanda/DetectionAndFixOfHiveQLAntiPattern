package myApplication;

import webAPI.ReturnMessageEntity;
import webAPI.StaticCheckImp;

import java.util.HashMap;
import java.util.List;

public class TestCase {
    public static void main(String[] args) {
        String[][][] testCases = {
                {{"select t1.name,t2.age from t1 inner join t2 on t1.id = t2.id;"}, {}},
                {{"select t3.subname,t4.age from (select t1.subname,t2.age from t1 inner join t2 on t1.id = t2.id) as t3 inner join t4 on t4.name = t3.name;"}, {"1"}},
                {{"select t3.subname,t4.age from (select t1.subname,t2.age from t1 left join t2 on t1.id = t2.id) as t3 inner join t4 on t4.name = t3.name;"}, {"1"}},
                {{"select * from (select userId from table_a where dt=20160731) a join (select userId from table_b where dt=20160731) b  on a.userId=b.userId join (select userId from table_c where dt=20160731) c on a.userId=c.userId"}, {"1","3"}},
                {{"select t3.subname,t4.age from (select t1.subname,t2.age from t1 inner join t2 on t1.id = t2.id) as t3 right join t4 on t4.name = t3.name;"}, {"1"}},
                {{"select t3.subname,t4.age from (select t1.subname,t2.age from t1 join t2 on t1.id = t2.id) as t3 join t4 on t4.name = t3.name;"}, {"1"}},
                {{"select e.ename,d.deptno,l.loc_name from e join d ON d.deptno = e.deptno join l on d.loc = l.loc;"}, {"1"}},
                {{"select t1.a from t1 join t2 on t1.b = t2.b join t3 on t1.b=t3.b join t4 on t1.b=t4.b"}, {"1"}},
                {{"select t1.name,t2.age from t1  join t2 on t1.id = t2.id;"}, {}},
                {{"SELECT t1.name,t2.age\nFROM t1  inner Join t2\nON t1.id = t2.id;"}, {}},
                {{"select t3.subname,t4.age \nfrom (\nselect t1.subname,t2.age \nfrom t1 inner join t2 \non t1.id = t2.id\n) as t3 inner join t4 \non t4.name = t3.name;"}, {"1"}},
                {{"SELECT t1.name,t2.age\nFROM t1  inner Join t2\nON t1.id = t2.id;"}, {}},
                {{"select t1.col1, t2.col2 from table1 as t1 join table2 as t2 on t1.col1  = t2.col2;"}, {}},
                {{"select c from t1 where c>1"}, {}},
                {{"select t1.col1, t2.col2 from table1 as t1 join (select t3.col3 from t3 where t3.age - 3 > 18) as t2 on t1.col1  = t2.col2;"}, {"2.1"}},
                {{"select t1.col1, t2.col2 from table1 as t1 join table2 as t2 on (t1.col1 - 100 = t2.col2);"}, {"2.2"}},
                {{"select t1.col1, t2.col2 from table1 as t1 join table2 as t2 on t1.col1 - 100 = t2.col2"}, {"2.2"}},
                {{"select t1.col1, t2.col2 from table1 as t1 join table2 as t2 on t1.col1 + 100 = t2.col2"}, {"2.2"}},
                {{"select t1.col1, t2.col2 from table1 as t1 join table2 as t2 on t1.col1 * 100 = t2.col2"}, {"2.2"}},
                {{"select t1.col1, t2.col2 from table1 as t1 join table2 as t2 on t1.col1 / 100 = t2.col2"}, {"2.2"}},
                {{"select t1.col1, t2.col2 from table1 as t1 join table2 as t2 on t1.col1 / 100 = t2.col2"}, {"2.2"}},
                {{"select t1.col1, t2.col2 from table1 as t1 join (select t3.col3,t4.col2 from t3 join t4 on t3.id = t4.id + 100) as t2 on t1.col1  = t2.col2;"}, {"1","2.2"}},
                {{"select t1.col1, t2.col2 from table1 as t1 join (select t3.col3 from t3 where t3.age + 3 > 18) as t2 on t1.col1  = t2.col2;"}, {"2.1"}},
                {{"select t1.col1,t1.col2,t1.col3 from t1;"}, {}},
                {{"select t1.col1,t1.col2,t1.col3,t2.col4,t2.col5 from t1 inner join t2 on t1.id = t2.id;"}, {}},
                {{"select t1.col1,t2.col2 from t1 join (select count (*) from t3 where t3.age > 18) as t2;"}, {}},
                {{"select * from t1;"}, {"3"}},
                {{"select t1.col1,t2.col2 from t1 join (select * from t3 where t3.age > 18) as t2;"}, {"3"}},
                {{"select * from t1 join (select t3.name,t3.sex from t3 where t3.age > 18) as t2;"}, {"3"}},
                {{"select * from t1 order by age;"}, {"3","4"}},
                {{"select * from t1 sort by age;"}, {"3"}},
                {{"select t1.name,t2.age from t1 join t2 on t1.id = t2.id order by t2.age;"}, {"4"}},
                {{"select t1.name,avg(t1.score),t1.age from t1 group by t1.name order by t1.age asc;"}, {"4","7"}},
                {{"select t1.name,t2.age from t1 inner join t2 on t1.id = t2.id order by t2.age;"}, {"4"}},
                {{"select t1.name,t2.age from t1 inner join (select t3.sex,t4.age from t3 join t4 on t3.name = t4.name order by t3.name) as t2 on t1.id = t2.id"}, {"1","4"}},
                {{"SELECT C.CustomerID, C.Name, Count(S.SalesID) FROM Customers as C INNER JOIN Sales as S ON C.CustomerID = S.CustomerID GROUP BY C.CustomerID, C.Name HAVING S.LastSaleDate BETWEEN ‘1/1/2019’ AND ‘12/31/2019’;"}, {}},
                {{"select deptno, avg(sal) avg_sal from emp group by deptno having avg_sal > 2000;"}, {}},
                {{"select col1,col2 from (select t1.col1,t1.col2,t2.col3 from t1 join t2 on t1.id = t2.id having t1.col1 >100) as t3"}, {"5"}},
                {{"select t1.col1, t2.col2 from t1 join t2 on t1.id = t2.id group by t1.col1 having t2.col2 > 2000;"}, {"7","10"}},
                {{"select t1.col1, t2.col2, sum(t1.col1) from t1 join t2 on t1.id = t2.id where t2.col2 > 2000 group by t1.col1,t2.col2;"}, {}},
                {{"select t1.col1, t2.col2 from table1 as t1\njoin table2 as t2\non upper(t1.col1) = t2.col2;"}, {"6"}},
                {{"select t1.col1, t2.col2 from table1 as t1 join table2 as t2 on upper(t1.col1) = t2.col2;"}, {"6"}},
                {{"select t1.col1, t2.col2 from t1 join t2 on upper(t1.col1) = t2.col2;"}, {"6"}},
                {{"select t1.col1, t2.col2 from table1 as t1 join table2 as t2 on count(t1.col1)=t2.col2;"}, {"6"}},
                {{"select t1.col1, t2.col2 from t1 inner join t2 on abs(t1.col1) = t2.col2;"}, {"6"}},
                {{"select col1,col2 from t1 group by col1,col2;"}, {"10"}},
                {{"select col1,col2 from t1 group by col1;"}, {"10","7"}},
                {{"select col1,col2,sum(col3) from t1 group by col1,col2;"}, {}},
                {{"select sum(col1), col2, distinct col3 from t1 group by col1, col2"}, {"7"}},
                {{"select t1.col1, t1.col2, t2.col3 from t1 join t2 on t1.col1 = t2.col1 group by t2.col1, t1.col2, t2.col3"}, {"10","7"}},
                {{"select col1,col2 from (select t1.col1,t2.col2 from t1 join t2 on t1.id = t2.id) as t3 group by col1;"}, {"10","7"}},
                {{"select t3.col1,t3.col2,sum(t3.col1) from (select t1.col1,t2.col2 from t1 join t2 on t1.id = t2.id) as t3 group by t3.col1,t3.col2;"}, {}},
                {{"select t3.col1,t3.col2,sum(t3.col1) from (select t1.col1,t2.col2 from t1 join t2 on t1.id = t2.id) as t3 group by t3.col2;"}, {"7"}},
                {{"select date_sub('2020-9-16', interval 10 days) from a;"}, {"8"}},
                {{"select date_sub('2020-9-16', 2) from a;"}, {}},
                {{"select date_sub('2020-9-16', interval 10 day) from a join b on a.id = b.id;"}, {"8"}},
                {{"select add_months(date_sub(current_date,1),-3) from a join b on a.id = b.id;"}, {}},
                {{"select current_timestamp() - INTERVAL 10 second from a join b on a.id = b.id;"}, {}},
                {{"SELECT ID,CASE WHEN col_a = 0 THEN 0 ELSE (col_b / col_a) - col_a END AS math_is_fun FROM t1;"}, {"9"}},
                {{"SELECT ID,CASE WHEN col_a = 0 THEN 0 ELSE 2.2 END AS math_is_fun FROM t1;"}, {"9"}},
                {{"SELECT ID,CASE WHEN col_a = 2.2 THEN 0 ELSE col_a END AS math_is_fun FROM (select t3.col_a,t2.col_b from t3 join t2 on t3.id = t2.id) as t1;"}, {"9"}},
                {{"SELECT ID,CASE WHEN col_a = 0 THEN 0 ELSE 2 END AS math_is_fun FROM (select t3.col_a,t2.col_b from t3 join t2 on t3.id = t2.id) as t1;"}, {}},
                {{"select sum(col1),col2 from (select t1.col1,t1.col2,t2.col3 from t1 join t2 on t1.id = t2.id group by t1.col1) as t1 group by col1,col2;"}, {"7","10"}},
                {{"select count( distinct cookie ) from weblogs where dt <= ${today} and dt >= ${90daysAgo};"}, {"11"}},
                {{"select count(cookie) from weblogs where dt <= ${today} and dt >= ${90daysAgo};"}, {}},
                {{"select count(col1) from (select distinct col1 from t2 where col2 > 100) as t1"}, {}},
                {{"select count(distinct col1) from (select col2,col1 from t2 where col2 >100) as t1"}, {"11"}},
                {{"select col1 from (select count(distinct col2) from t2 where col2 > 100) as t1"}, {"11"}},
                {{"select col1，col2 from table1 where col1 like 'p%'"}, {}},
                {{"selct * from t1"}, {"13"}},
                {{""}, {"13"}},
                {{"123"}, {"13"}},
                {{"啊啦啦啦"}, {"13"}},
                {{"select t1.a from t2 where t3.b = t4.b"}, {"13"}}
        };
        HashMap<String, String> apMap = new HashMap<String, String>();
        apMap.put("1", "不要过多使用join");
        apMap.put("2.1", "不要在where子句中进行运算");
        apMap.put("2.2", "不要在join子句中进行运算");
        apMap.put("3", "Be careful! Using \"select *\" will cause poor performance! Please select specific column.");
        apMap.put("4", "Be careful! Using \"order by\" will cause poor performance! Please use \"sort by\".");
        apMap.put("5", "Be careful! Using \"having\" will cause poor performance! Please use \"where\".");
        apMap.put("6", "不要在谓词中使用函数");
        apMap.put("7", "Warning! Column selected should be concluded in group by");
        apMap.put("8", "Be careful! Using \"interval\" in \"date_sub()\" will cause error!");
        apMap.put("9", "Be careful! Data type after \"then\" and \"else\" is different!");
        apMap.put("10", "Be careful! \"group by\" should be used with aggregate function!");
        apMap.put("11", "Be careful! Using \"count(distinct ...)\" may cause poor performance! Please use \"sum...group by\"");
        apMap.put("13", "异常输入.");

        int testCaseNum = testCases.length;
        int testWrongNum = 0;
        StringBuilder errorLog = new StringBuilder();

        for(String[][] testCase : testCases){
            String hiveql = testCase[0][0];
            String[] fixedSuggestionsException = testCase[1];
            List<String> fixedSuggestions= StaticCheckImp.staticCheckRun(hiveql).fixedSuggestions;
            int fixedSuggestionsNum = fixedSuggestions.size();
            boolean isWrong = false;
            if(fixedSuggestionsNum != fixedSuggestionsException.length){
                isWrong = true;
            }else{
                for(String sugNum : fixedSuggestionsException){
                    String sug = apMap.get(sugNum);
                    if(fixedSuggestions.contains(sug)){
                        fixedSuggestionsNum--;
                    }
                }
                if(fixedSuggestionsNum != 0){
                    isWrong = true;
                }
            }
            if(isWrong){
                testWrongNum++;
                errorLog.append("*********************\n");
                errorLog.append("-HiveQL: ").append(hiveql).append("\n");
                errorLog.append("-Should suggest:\n");
                for(String sugNum : fixedSuggestionsException){
                    errorLog.append(apMap.get(sugNum)).append("\n");
                }
                errorLog.append("-Actual suggest:\n");
                for(String sug : fixedSuggestions){
                    errorLog.append(sug).append("\n");
                }
            }
        }
        System.out.print(errorLog.toString());
        System.out.println("*********************");
        System.out.printf("Right/total: %d/%d, accuracy: %.2f%n",
                testCaseNum-testWrongNum, testCaseNum, 1-(float)testWrongNum/testCaseNum);
    }
}
