/**
 * Created by prajpoot on 6/7/16.
 */
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by prajpoot on 29/6/16.
 */
public class get_wNlists {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        BufferedReader bf = new BufferedReader(new FileReader("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/result1_B2"));
        File file = new File("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)/WN");
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



            if(sCurrentLine.startsWith("$$$$$"))
            {
                lineno=lineno+1;
                testmem.clear();
                // System.out.println(sCurrentLine.split(" ")[1].trim());
                testmem=wnf.getwN(sCurrentLine.split(" ")[1].trim());
                bw.write(sCurrentLine.split(" ")[1].trim()+"==="+lineno+" "+testmem);
                bw.newLine();

            }
        }

        bw.close();
        try {
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
