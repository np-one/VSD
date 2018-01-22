import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by prajpoot on 9/6/16.
 */
public class LM_measure_NZ_P {

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/testWN/result1_A3_WN"));
        File file = new File("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/testWN/result_final_A3_WN_NZP");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        double score=-10000.0;
        double second = -10000.0;
        int lineno=0;
        String secondclas="";
        String sCurrentLine;
        String highclass="";
        String highestclass="";
        double total = 0;
        ArrayList<Double> scores = new ArrayList<>();
        HashMap<Integer,String> scoresclass = new HashMap<>();
        String prevLine = "";
        int gLineNo=0;
        int ind=0;
        while ((sCurrentLine = bf.readLine()) != null) {

            if(sCurrentLine.startsWith("#####") && !prevLine.startsWith("$$$$$"))
            {
                scores.add(total/lineno);

                highclass=sCurrentLine;

                scoresclass.put(ind,sCurrentLine);
                //System.out.println(" "+scoresclass+" "+scores);
                ind=ind+1;
                total=0;
                lineno=0;
                continue;
            }
            if(sCurrentLine.startsWith("$$$$$") && gLineNo==0)
            {
               // bw.write(sCurrentLine);
                continue;
            }
            if(sCurrentLine.startsWith("$$$$$") && gLineNo!=0)
            {
                scores.add(total/lineno);
                ind=0;

                total=0;
                Double maxScore=0d;
                int maxIndex = 0;
                for (int i=0; i < scores.size(); i++) {
                    if (scores.get(i) > maxScore) {
                        maxScore = scores.get(i);
                        maxIndex = i;
                        highestclass=scoresclass.get(i);


                    }
                }
                //highestclass=scores.;
//                if (maxIndex == 1) {
//                    highestclass = "calibrate";
//                } else if (maxIndex == 2) {
//                    highestclass = "meander";
//                } else if (maxIndex == 3) {
//                    highestclass = "assuming_position";
//                } else if (maxIndex == 4) {
//                    highestclass = "appear";
//                } else if (maxIndex == 5) {
//                    highestclass = "spatial_configuration";
//                } else if (maxIndex == 6) {
//                    highestclass = "escape";
//                }
//                else if (maxIndex==0){
//                    highestclass = "DEF";
//                }


                //bw.write(String.valueOf(score));
                bw.write(" 1Predicted_class: "+maxIndex+scoresclass.get(maxIndex-1));
                System.out.println(scoresclass+" TTT"+scores);

                bw.newLine();
                score=-10000.0;
                //bw.write(sCurrentLine);
                scores.clear();
                continue;
            }
            if(Float.parseFloat(sCurrentLine)>0)
            {   total = total + 1;
//                highestclass=highclass;
            }

            lineno=lineno+1;
            gLineNo=gLineNo+1;
            prevLine = sCurrentLine;

        }

        bw.close();
        try {
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
