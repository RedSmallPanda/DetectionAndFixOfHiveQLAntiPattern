package bias_check;

import java.io.*;
import java.util.Random;

public class gen_partition {
    public static String alphab="abcdefghijklmnopqrstuvwxyz";
    public static void main(String[] args) {
        String[] locs={"A","B","C","D","E"};
        int[] lines={10000,10000,10000,10000,10000};
        String path = "E:/test/data/mapReduceNumberTest_50kAverage.txt";
        File file = new File(path);
        try {
            if (file.exists()) {
                //判断文件是否存在，如果不存在就新建一个txt
                file.createNewFile();
            }
            FileOutputStream fos=new FileOutputStream(file);
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter  bw=new BufferedWriter(osw);
            Random rand=new Random();
            for(int n=0; n<5; n++){
                for(int i=0;i<lines[n];i++){
                    String name=""+alphab.charAt(rand.nextInt(25))+alphab.charAt(rand.nextInt(25))+alphab.charAt(rand.nextInt(25))+alphab.charAt(rand.nextInt(25));
                    int age=rand.nextInt(55)+20;
                    bw.write(name+","+ age+"," +locs[n]+'\n');
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
