package bias_check;

import java.io.*;
import java.util.Random;

public class gen_data {
    public static String alphab="abcdefghijklmnopqrstuvwxyz";
    public static void main(String[] args) {
        int lines=50000000;
        String path = "/Users/kayaetsuden/Desktop/研一上课程/高级软件工程/TestData/mrtest_200age_50m.txt";
        File file = new File(path);
        try {
            FileOutputStream fos=new FileOutputStream(file);
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter  bw=new BufferedWriter(osw);
            Random rand=new Random();
            for(int i=0;i<lines;i++){
                String name=""+alphab.charAt(rand.nextInt(26))+alphab.charAt(rand.nextInt(26))+alphab.charAt(rand.nextInt(26))+alphab.charAt(rand.nextInt(26));
                int age=rand.nextInt(200)+1;
                bw.write(name+","+ age+"," +rand.nextInt(5)+'\n');
            }
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }
