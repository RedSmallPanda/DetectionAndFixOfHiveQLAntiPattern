package bias_check;

public class Main {
    public static void main(String [] args){
//        /*mlp训练数据生成*/
//        String[] tableNameSuf = new String[13];
//        for(int i=0; i<13; i++){
//            tableNameSuf[i] = "mlp"+(i+1);
//        }
//        GridSearch gs = new GridSearch();
//        String[] tableNameSufTest = {"50", "500"};
//        gs.mlpTrainingDataGen(tableNameSuf);

////        int threshold = new ReduceTaskBaselineTest().test();
        int threshold = 2500000;

        /*reduce num check*/
        ReduceNumCheck rnCheck = new ReduceNumCheck(threshold);
        rnCheck.testDemo();
    }
}
