import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.POS;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by prajpoot on 18/7/17.
 */
public class WnTest {
        public static String file;
        static String path;
        static URL url;
        public static Dictionary dict;
        public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException {
            path = "/home/prajpoot/Downloads/WordNet-3.0"+ File.separator + "dict";
            url = new URL("file", null, path);
            // construct the dictionary object and open it
            dict = new Dictionary(url);
            dict.open();


            String line="";
            Scanner stdin = new Scanner(new File("/home/prajpoot/Semcor_WSD_Exp/senseeval3/SensEval3.0VerbSenseData/verbs"));
            BufferedWriter bw = new BufferedWriter(new FileWriter("/home/prajpoot/Semcor_WSD_Exp/senseeval3/SensEval3.0VerbSenseData/possible_classes"));
            Map<String, ArrayList<String>> map = new HashMap<>();
            Map<String, ArrayList<String>> Verbmap = new HashMap<>();

            while (stdin.hasNextLine()){
                line=stdin.nextLine();
                System.out.println(line);
                if (dict.getIndexWord(line.trim(), POS.VERB)==null){
                    bw.write("1\n");
                    continue;
                }
                 bw.write(dict.getIndexWord(line.trim(), POS.VERB).getWordIDs().size()+"\n");

            }
            bw.close();
            stdin.close();


        }
}
