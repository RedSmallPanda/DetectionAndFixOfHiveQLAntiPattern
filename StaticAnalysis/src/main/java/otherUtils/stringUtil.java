package otherUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class stringUtil {
    public static String join2innerJoin(String sql) throws Exception {
        StringBuffer convertedSql = new StringBuffer(sql);
        String sql2lower = sql.toLowerCase();
        String[] tokens = sql2lower.split("[ (),;]+");
        //第几个join
        List<Integer> joinCount = new ArrayList<>();
        //第几个token
        List<Integer> joinPos = new ArrayList<>();
        int count = 0;
        for(int i = 0;i < tokens.length;i++){
            if(tokens[i].equals("join")){
                joinCount.add(count);
                joinPos.add(i);
                count ++;
            }
            else if(tokens[i].contains("join")){
                count ++;
            }
        }
        for(int i = 0;i < joinCount.size();i++){
            if(joinPos.get(i) > 0){
                if(!(tokens[joinPos.get(i) - 1].equals("inner")||tokens[joinPos.get(i) - 1].equals("left")||
                tokens[joinPos.get(i) - 1].equals("right")||tokens[joinPos.get(i) - 1].equals("full")||
                tokens[joinPos.get(i) - 1].equals("outer"))){
                    String tempStr = convertedSql.toString().toLowerCase();
                    int insertPos = findNstPos(tempStr,"join",joinCount.get(i));
                    convertedSql.insert(insertPos,"inner ");
                }
            }
        }
        return(convertedSql.toString());
    }

    public static int findNstPos(String str,String subStr,int index){
        Pattern pattern = Pattern.compile(subStr);
        Matcher findMatcher = pattern.matcher(str);
        int number = 0;
        while(findMatcher.find()) {
            if(number == index){
                break;
            }
            number++;
        }
        int i = findMatcher.start();//“A”第二次出现的位置
        return(i);
    }
}
