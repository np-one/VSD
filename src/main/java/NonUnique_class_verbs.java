import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by prajpoot on 21/6/16.
 */



public class NonUnique_class_verbs {



    public static Map<String,ArrayList<String>> LookUpMap(Map<String, ArrayList<String>> map, String word){

        Map<String, ArrayList<String>> Verbmap = new HashMap<>();
        //Check which classes verb belongs
        for(HashMap.Entry<String,ArrayList<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(entry.getValue().contains(word))
            {  //System.out.println(key);
                Verbmap.put(key,entry.getValue());
            }
            // ...
        }

        return Verbmap;

    }




    public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException {

        VB_tools p = new VB_tools();
        // p.getClasses("rise");
        Map<String, ArrayList<String>> map = new HashMap<>();
        map = p.Create_map("/home/prajpoot/Downloads/new_vn/");

        System.out.println(map);

        int count =0;
        for(HashMap.Entry<String,ArrayList<String>> entry : map.entrySet()) {

            for (int i = 0; i < entry.getValue().size(); i++) {
                count=count+1;
                System.out.println(LookUpMap(map,entry.getValue().get(i)).size()+": "+entry.getValue().get(i));
            }
        }

       // System.out.println(LookUpMap(map,"stare"));

    }

}
