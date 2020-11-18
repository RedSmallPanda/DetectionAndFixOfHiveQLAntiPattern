package bias_check;

public class Main {
    public static void main(String [] args){
//        int threshold = new ReduceTaskBaselineTest().test();
        int threshold = 2500000;

        /*reduce num check*/
        ReduceNumCheck rnCheck = new ReduceNumCheck(threshold);
        rnCheck.testDemo();
    }
}
