package bias_check;

import java.io.*;
import java.util.Random;

public class gen_partition {
    public static String alphab="abcdefghijklmnopqrstuvwxyz";
    public static void main(String[] args) {
        int lines=5000;
        String path = "E:/test/data/mrtest_300c.txt";
        File file = new File(path);
        try {
            FileOutputStream fos=new FileOutputStream(file);
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter  bw=new BufferedWriter(osw);
            Random rand=new Random();
            for(int i=0;i<lines;i++){
                String name=""+alphab.charAt(rand.nextInt(26))+alphab.charAt(rand.nextInt(26))+alphab.charAt(rand.nextInt(26))+alphab.charAt(rand.nextInt(26));
                int age=rand.nextInt(55)+20;
                bw.write(name+","+ age+"," +rand.nextInt(300)+'\n');
            }
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }
