package bias_check;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class remove_test {
    public static void main(String [] args){
        String filename="E:\\Hive_antipattern\\DynamicAnalysis\\hivecheck\\src\\main\\java\\bias_check\\joinMlpTrainData.txt";

        String test="E:\\Hive_antipattern\\DynamicAnalysis\\hivecheck\\src\\main\\java\\bias_check\\joinMlpTrainTest.txt";
        String output="E:\\Hive_antipattern\\DynamicAnalysis\\hivecheck\\src\\main\\java\\bias_check\\joinMlpTrainTrainData_notest.txt";
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> testarr=new ArrayList<>();
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
        try {
            File file1 = new File(test);
            InputStreamReader input = new InputStreamReader(new FileInputStream(file1));
            BufferedReader bf = new BufferedReader(input);
            // 按行读取字符串
            String str;
            while ((str = bf.readLine()) != null) {
                testarr.add(str);
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

            for(String i:arrayList){
                if(!testarr.contains(i)){
                    bw.write(i+"\n");
                }
            }
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

