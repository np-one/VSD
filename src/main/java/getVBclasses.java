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
 * Created by asshriva on 10/2/17.
 */
public class getVBclasses {
    static Map<String, ArrayList<String>> map = new HashMap<>();

    public getVBclasses() throws ParserConfigurationException, SAXException, IOException {
        VB_tools p = new VB_tools();
       // map = p.Create_map("/home/prajpoot/Downloads/new_vn/");  //old
        map = p.Create_map2("/home/prajpoot/Downloads/new_VN/"); //new
    }

    public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException {
        VB_tools p = new VB_tools();
///home/prajpoot/VSDBootStrap/output/final_output/semlink_test_3

        Scanner stdin = new Scanner(new File("/home/prajpoot/vn_3.2.5_annot/MF/test"));
        String line="";
        BufferedWriter bw = new BufferedWriter(new FileWriter("/home/prajpoot/vn_3.2.5_annot/MF/test_file_possible_classes"));
        map = p.Create_map2("/home/prajpoot/Downloads/new_VN/");
        int max=0;
        while (stdin.hasNextLine()){
            line=stdin.nextLine();
            String verb =line.split(" ")[0];
            ArrayList<String> Verbmap = new ArrayList<>();
            Verbmap=LookUpMap2(verb);
            if(Verbmap.size()==1){
                max+=1;
            }

            bw.write(String.valueOf(Verbmap)+"\n");
        }
        bw.close();
        stdin.close();
        System.out.println(max);



    }



    public  ArrayList<String> LookUpMap(String word){

        ArrayList<String> Verbmap = new ArrayList<>();
        //Check which classes verb belongs
        for(HashMap.Entry<String,ArrayList<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            // System.out.println(entry.getValue()+"------------------------"+word.trim());
            if(entry.getValue().contains(word.trim()))
            {  //System.out.println(key);
                Verbmap.add(key);
            }
            // ...
        }

        return Verbmap;

    }

    public static ArrayList<String> LookUpMap2(String word){

        ArrayList<String> Verbmap = new ArrayList<>();
        //Check which classes verb belongs
        for(HashMap.Entry<String,ArrayList<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            // System.out.println(entry.getValue()+"------------------------"+word.trim());
            if(entry.getValue().contains(word.trim()))
            {  //System.out.println(key);
                //Verbmap.add(key.replace(key.split("-")[0]+"-",""));  //withsub
                Verbmap.add(key.replace(key.split("-")[0]+"-","").split("-")[0]);
            }
            // ...
        }

        return Verbmap;

    }


}
