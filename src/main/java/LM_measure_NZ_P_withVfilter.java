/**
 * Created by prajpoot on 4/7/16.
 */
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class LM_measure_NZ_P_withVfilter {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        BufferedReader bf = new BufferedReader(new FileReader("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/wn+old/result1_B2"));
        BufferedReader bframes = new BufferedReader(new FileReader("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/wn+old/final-input-frames"));
        File file = new File("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/wn+old/result_final_B2_WN+old_top2");
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
        List<String> testmem= new LinkedList<String>();
        get_WN_words wnf = new get_WN_words();
        wnf.init();
        String frame="";
        while ((sCurrentLine = bf.readLine()) != null) {
            frame=bframes.readLine();

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
                testmem.clear();
                testmem=wnf.getwN(sCurrentLine.split(" ")[1].trim());
               // System.out.println(sCurrentLine.split(" ")[1].trim()+" "+testmem);
               // System.out.println(sCurrentLine.split(" ")[1].trim()+"=="+testmem);
              //  System.out.println(testmem);
                // bw.write(sCurrentLine);
                continue;
            }
            if(sCurrentLine.startsWith("$$$$$") && gLineNo!=0)
            {
                testmem.clear();
                testmem=wnf.getwN(sCurrentLine.split(" ")[1].trim());
              //  System.out.println(sCurrentLine.split(" ")[1].trim()+" "+testmem);
                scores.add(total/lineno);
                ind=0;
                int usecond=0;
                total=0;
                Double maxScore=0d;
                int maxIndex = 0;

                for (int i=0; i < scores.size(); i++) {
                    if (scores.get(i) > maxScore) {
                        usecond=maxIndex;
                        maxScore = scores.get(i);
                        maxIndex = i;
                        highestclass=scoresclass.get(i);


                    }
                }



                bw.write(" 1Predicted_class: "+maxIndex+scoresclass.get(maxIndex-1));

                    bw.write("/"+scoresclass.get(usecond-1));

                System.out.println(scoresclass+" TTT"+scores);

                bw.newLine();
                score=-10000.0;
                //bw.write(sCurrentLine);
                scores.clear();
                continue;
            }
            if(!testmem.contains(frame.split(" ")[0].trim()))
            {
                  System.out.println("fail"+frame+"^^^^"+"\n\n");
                continue;}
          //  System.out.println((frame.split(" ")[0].trim()));
            System.out.println(highclass+" pass "+lineno+"\n\n");
            if(Float.parseFloat(sCurrentLine)>0)
            {   total = total + 1;
//                highestclass=highclass;
            }

            lineno=lineno+1;
            gLineNo=gLineNo+1;
            prevLine = sCurrentLine;

        }

        bw.close();
        bframes.close();
        try {
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
