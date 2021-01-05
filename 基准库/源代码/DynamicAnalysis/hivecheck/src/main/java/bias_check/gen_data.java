package bias_check;

import java.io.*;
import java.util.Random;

public class gen_data {
    public static String alphab="abcdefghijklmnopqrstuvwxyz";
    public static void main(String[] args) {
        Random rand=new Random();
        for(int n=0; n<13; n++) {
            int lines = 5000 + 65000/13*n;
            String path = "E:/test/data/mrtest_mlp"+(n+1)+".txt";
            File file = new File(path);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                BufferedWriter bw = new BufferedWriter(osw);
                int city = rand.nextInt(25) + 3;
                for (int i = 0; i < lines; i++) {
                    String name = "" + alphab.charAt(rand.nextInt(26)) + alphab.charAt(rand.nextInt(26)) + alphab.charAt(rand.nextInt(26)) + alphab.charAt(rand.nextInt(26));
                    int age = rand.nextInt(90) + 1;
                    bw.write(name + "," + age + "," + rand.nextInt(city) + '\n');
                }
                System.out.println("" + lines + " " + city);
                bw.close();
                osw.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    }
