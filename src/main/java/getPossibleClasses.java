import edu.mit.jwi.item.POS;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by prajpoot on 29/8/17.
 */
public class getPossibleClasses {
    public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException {
        VB_tools p = new VB_tools();
        // p.getClasses("rise");
        String line="";
        Scanner stdin = new Scanner(new File("/home/prajpoot/Semlink_EXPERIMENT/testdata/verbs"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("/home/prajpoot/Semlink_EXPERIMENT/testdata/possible_classes"));
        Map<String, ArrayList<String>> map = new HashMap<>();
        map = p.Create_map("/home/prajpoot/Downloads/new_vn/");
        Map<String, ArrayList<String>> Verbmap = new HashMap<>();

        while (stdin.hasNextLine()){
            line=stdin.nextLine();
            Verbmap=LookUpMap(map,line.trim());
            for(String cl: Verbmap.keySet()){
                bw.write(cl.split("-")[1]+" ");
            }
            bw.newLine();

        }
        bw.close();
        stdin.close();


    }



    public static Map<String,ArrayList<String>> LookUpMap(Map<String, ArrayList<String>> map, String word){

        Map<String, ArrayList<String>> Verbmap = new HashMap<>();
        //Check which classes verb belongs
        for(HashMap.Entry<String,ArrayList<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            // System.out.println(entry.getValue()+"------------------------"+word.trim());
            if(entry.getValue().contains(word.trim()))
            {  //System.out.println(key);
                Verbmap.put(key,entry.getValue());
            }
            // ...
        }

        return Verbmap;

    }
    public static POS getPosType(String pos) {
        switch (pos) {
            case "NNS":
            case "NNP":
            case "NN":
            case "NNPS":
                return POS.NOUN;
            case "JJ":
            case "JJR":
            case "JJS":
                return POS.ADJECTIVE;
            case "RB":
            case "RBR":
            case "RBS":
                return POS.ADVERB;
            case "VB":
            case "VBD":
            case "VBG":
            case "VBN":
            case "VBP":
            case "VBZ":
                return POS.VERB;
            default:
                return POS.NOUN;
        }
    }
}
