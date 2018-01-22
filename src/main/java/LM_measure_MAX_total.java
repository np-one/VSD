import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by prajpoot on 29/6/16.
 */
public class LM_measure_MAX_total {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        BufferedReader bf = new BufferedReader(new FileReader("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/testWN/result1_B2_WN"));
        File file = new File("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/testWN/result_final_B2_WN_MAX_TOTAL");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        double score=-10000.0;
        double second = -10000.0;
        int lineno=0;
        String secondclas="";
        String sCurrentLine;
        String highclass="";
        String highestclass="";
        String temp[]=new String[20];
        int ind=0;
        double total = 0;
        ArrayList<Double> scores = new ArrayList<>();
        List<String> testmem= new LinkedList<String>();
        get_WN_words wnf = new get_WN_words();
        wnf.init();
        while ((sCurrentLine = bf.readLine()) != null) {
            lineno=lineno+1;

            if(sCurrentLine.startsWith("#####"))
            {
                scores.add(total);
                temp[ind]=sCurrentLine;
                ind=ind+1;
                highclass=sCurrentLine;
                total=0;
                continue;
            }
            if(sCurrentLine.startsWith("$$$$$") && lineno==0)
            {
                testmem.clear();
             //   testmem=wnf.getwN(sCurrentLine.split(" ")[1].trim());
                //bw.write(sCurrentLine);
                continue;
            }
            if(sCurrentLine.startsWith("$$$$$") && lineno!=0)
            {
                testmem.clear();
               // System.out.println(sCurrentLine.split(" ")[1].trim());
                //testmem=wnf.getwN(sCurrentLine.split(" ")[1].trim());
                ind=0;
                scores.add(total);
                total=0;
                Double maxScore=0d;
                int maxIndex = 0;
                int sum=0;

                for (int i=0; i < scores.size(); i++) {
                    if (scores.get(i) >= maxScore && scores.get(i)!=0) {
                        maxScore = scores.get(i);
                        maxIndex = i;
                    }
                }

                //  highestclass=scores.;


                //bw.write(String.valueOf(score));
                if(maxIndex!=0)
                    bw.write(" 1Predicted_class: "+temp[maxIndex-1]);
                else
                    if(lineno!=1)
                        bw.write(" 1Predicted_class: "+"0null");

                //bw.write(String.valueOf(second));
                // bw.write(" Second Predicted_class: "+secondclas);
                if(lineno!=1)
                bw.newLine();//bw.newLine();
                // score=-10000.0;
                // bw.write(sCurrentLine);
                scores.clear();
                continue;
            }
            //System.out.println(sCurrentLine.split(" ")[0]);

            total = total + Float.parseFloat(sCurrentLine);

            if(Float.parseFloat(sCurrentLine)>score){
                second=score;
                score=Float.parseFloat(sCurrentLine);
                secondclas=highestclass;
                highestclass=highclass;
            }

            lineno=lineno+1;
        }

        bw.close();
        try {
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
