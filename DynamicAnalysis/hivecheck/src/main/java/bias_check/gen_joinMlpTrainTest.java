package bias_check;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class gen_joinMlpTrainTest {
    public static void main(String[] args){
        String filename="E:\\Hive_antipattern\\DynamicAnalysis\\hivecheck\\src\\main\\java\\bias_check\\joinMlpTrainData_L.txt";
        String output="E:\\Hive_antipattern\\DynamicAnalysis\\hivecheck\\src\\main\\java\\bias_check\\joinMlpTrainTest_L.txt";
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            File file = new File(filename);
            InputStreamReader input = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(input);
            // 按行读取字符串
            String str;
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(output);
        try {
            FileOutputStream fos=new FileOutputStream(file);
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter  bw=new BufferedWriter(osw);
            Random rand=new Random();
            int size=arrayList.size();
            int gap=5;
            for(int i=0;i<size;i=i+gap){
                bw.write(arrayList.get(i+rand.nextInt(5))+"\n");
            }
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
