/**
 * Created by prajpoot on 5/7/16.
 */
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class LM_measure_MAX_total_withVfilter {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        BufferedReader bf = new BufferedReader(new FileReader("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/wn+old/result1_A3"));
        BufferedReader bframes = new BufferedReader(new FileReader("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/wn+old/final-input-frames"));
        File file = new File("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/wn+old/result_final_A3_MAX_TOTAL_WN+old_top2");
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
        String frame="";
        while ((sCurrentLine = bf.readLine()) != null) {
            lineno=lineno+1;
            frame=bframes.readLine();

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
                   testmem=wnf.getwN(sCurrentLine.split(" ")[1].trim());
                System.out.println(sCurrentLine);
                System.out.println(sCurrentLine.split(" ")[1].trim()+"=="+testmem);
                System.out.println(testmem);
                //bw.write(sCurrentLine);
                continue;
            }
            if(sCurrentLine.startsWith("$$$$$") && lineno!=0)
            {
                testmem.clear();
                // System.out.println(sCurrentLine.split(" ")[1].trim());
                System.out.println(sCurrentLine);
                testmem=wnf.getwN(sCurrentLine.split(" ")[1].trim());
                System.out.println(sCurrentLine.split(" ")[1].trim()+"=="+testmem);
                ind=0;
                scores.add(total);
                total=0;
                Double maxScore=0d;
                int maxIndex = 0;
                int sum=0;
                int usecond=0;
                for (int i=0; i < scores.size(); i++) {
                    if (scores.get(i) > maxScore) {
                        usecond = maxIndex;
                        maxScore = scores.get(i);
                        maxIndex = i;
                    }
                }

                //  highestclass=scores.;


                //bw.write(String.valueOf(score));
                for(int u =0; u<temp.length;u++)
                System.out.print(temp[u]+ " ");
                System.out.println("\n Max i "+maxIndex);
                if(maxIndex!=0){
                    bw.write(" 1Predicted_class: "+temp[maxIndex-1]);
                    if(usecond!=0)
                        bw.write("/"+temp[usecond-1]);
                }
                else
                    bw.write(" 1Predicted_class: "+"0null");

                //bw.write(String.valueOf(second));
                // bw.write(" Second Predicted_class: "+secondclas);
                bw.newLine();//bw.newLine();
                // score=-10000.0;
                // bw.write(sCurrentLine);
                scores.clear();
                continue;
            }

            if(!testmem.contains(frame.split(" ")[0].trim()))
            {
              //  System.out.println("fail"+"\n\n");
                continue;}
            System.out.println((frame.split(" ")[0].trim()));
            System.out.println(highclass+" pass "+lineno+"\n\n");
            total = total + Float.parseFloat(sCurrentLine);

            if(Float.parseFloat(sCurrentLine)>score){
                second=score;
                score=Float.parseFloat(sCurrentLine);
                secondclas=highestclass;
                highestclass=highclass;
            }

        //    lineno=lineno+1;
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
