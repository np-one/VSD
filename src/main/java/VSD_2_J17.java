import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.POS;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by prajpoot on 14/7/17.
 */
public class VSD_2_J17 {
    public static String file;
    static String path;
    static URL url;
    public static Dictionary dict;
    public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException {
        VB_tools p = new VB_tools();
        Map<String, ArrayList<String>> map = new HashMap<>();
        map = p.Create_map2("/home/prajpoot/Downloads/new_VN/");
        Map<String, ArrayList<String>> Verbmap = new HashMap<>();
        Verbmap=LookUpMap(map,"kill");


        path = "/home/prajpoot/Downloads/WordNet-3.0"+ File.separator + "dict";
        url = new URL("file", null, path);
        // construct the dictionary object and open it
        dict = new Dictionary(url);
        dict.open();


        BufferedReader bufr = new BufferedReader(new FileReader("/home/prajpoot/Downloads/VSD_PART2/sol2"));
        String sCurrentLine;
        int linecount=0;
        int onecount=0;
        int verbcount=0;
        int monoverbcount=0;
        while ((sCurrentLine = bufr.readLine()) != null) {
            int singleverb=0;
            linecount+=1;
            Boolean atleastone=Boolean.FALSE;
            String temp[]=sCurrentLine.trim().split(" ");

            for(int j=0;j<temp.length;j++){
//                System.out.println(temp[j]);

                if (temp[j].contains("/vb")) {
                    if(LookUpMap(map,temp[j].split("/")[0]).size()==1){
                        singleverb=1;
                    }

                }
            }

            if(singleverb==0)
            for(int i=0;i<temp.length;i++){
//                System.out.println(temp[i]);

                if (temp[i].contains("/vb")) {
                     if(LookUpMap(map,temp[i].split("/")[0]).size()==1){
                         monoverbcount+=1;
                     }
                    verbcount+=1;
                }
                else{
                    if(dict.getIndexWord(temp[i].trim(),POS.NOUN)==null){
                    continue;
                 }
                      if(dict.getIndexWord(temp[i].trim(),POS.NOUN).getTagSenseCount()==1){
                     atleastone=Boolean.TRUE;
                }
                }
            }
            if(atleastone){
                onecount+=1;
            }

            System.out.println(linecount);
            System.out.println(onecount);
            System.out.println("\n\n\n\n");

        }
        System.out.println(linecount);
        System.out.println(onecount);
        System.out.println(verbcount);
        System.out.println(monoverbcount);

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

}
