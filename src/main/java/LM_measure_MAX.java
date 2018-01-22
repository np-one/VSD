import java.io.*;
import java.util.ArrayList;

/**
 * Created by prajpoot on 9/6/16.
 */
public class LM_measure_MAX {

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/result1_A3_WN"));
        File file = new File("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/old_STAT_tests/TESTresult");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        double score=-10000.0;
        double second = -10000.0;
        int lineno=0;
        String prev="";
        String sCurrentLine;
        String highclass="";
        String highestclass="";
        String temp[]=new String[20];
        int ind=0;
        double total = 0;
        ArrayList<Double> scores = new ArrayList<>();
        while ((sCurrentLine = bf.readLine()) != null) {

            if(sCurrentLine.startsWith("#####") && prev.startsWith("$$$$$"))
            {
                prev=sCurrentLine;
                scores.clear();
                temp[ind]=sCurrentLine;
                ind=ind+1;
                continue;
            }
            if(sCurrentLine.startsWith("#####") && !prev.startsWith("$$$$$"))
                {
                    scores.add(score);
                    score=0;

                    temp[ind]=sCurrentLine;
                    ind=ind+1;
                    highclass=sCurrentLine;
                    total=0;
                    prev=sCurrentLine;
                    continue;
                }
            if(sCurrentLine.startsWith("$$$$$") && lineno==0)
                {
                    prev=sCurrentLine;
                    //bw.write(sCurrentLine);
                    continue;
                }
            if(sCurrentLine.startsWith("$$$$$") && lineno!=0)
            {

                scores.add(score);
                //temp[ind]=sCurrentLine;
                ind=0;
                total=0;
                Double maxScore=0d;
                int maxIndex = 0;
                for (int i=0; i < scores.size(); i++) {
                    if (scores.get(i) > maxScore) {
                        maxScore = scores.get(i);
                        maxIndex = i+1;
                    }
                }

              //  highestclass=scores.;


                //bw.write(String.valueOf(score));
                if(maxIndex!=0)
                bw.write(" 1Predicted_class: "+temp[maxIndex-1]);
                else
                    bw.write(" 1Predicted_class: "+"0null");

                //bw.write(String.valueOf(second));
               // bw.write(" Second Predicted_class: "+secondclas);
                bw.newLine();//bw.newLine();
               // score=-10000.0;
               // bw.write(sCurrentLine);
                scores.clear();
                score=0;
                prev=sCurrentLine;
                continue;
            }

            total = total + Float.parseFloat(sCurrentLine);

            if(Float.parseFloat(sCurrentLine)>score){
                second=score;
                score=Float.parseFloat(sCurrentLine);
              //  secondclas=highestclass;
                highestclass=highclass;
            }

            lineno=lineno+1;
            prev=sCurrentLine;

    }

        bw.close();
        try {
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
