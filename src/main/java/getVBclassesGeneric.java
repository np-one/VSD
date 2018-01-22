import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by prajpoot on 10/5/17.
 */
public class getVBclassesGeneric {
    static Map<String, ArrayList<String>> map = new HashMap<>();
    public getVBclassesGeneric() {
        VB_tools p = new VB_tools();
        // map = p.Create_map("/home/prajpoot/Downloads/new_vn/");  //old
        try {
            map = p.Create_map2("/home/prajpoot/Downloads/new_VN/"); //new
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {

        VB_tools p = new VB_tools();
        // map = p.Create_map("/home/prajpoot/Downloads/new_vn/");  //old
        try {
            map = p.Create_map2("/home/prajpoot/Downloads/new_VN/"); //new
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Scanner stdin = new Scanner(new File("/home/prajpoot/Desktop/KsRL/wh_SRL/new_ann_3"));
        String line="";
        BufferedWriter bw = new BufferedWriter(new FileWriter("/home/prajpoot/Desktop/KsRL/WH_Asish_input4"));
        while (stdin.hasNextLine()){
            line=stdin.nextLine();
            if(map.containsKey(line.split(" ")[0].trim())){
                System.out.println(line+" "+map.get(line.split(" ")[0].trim()).toString().replace("]","").replace("[",""));
                bw.write(line+"\t"+map.get(line.split(" ")[0].trim()).toString().replace("]","").replace("[","").replace(", "," ")+"\n");
            }
            else {
                System.out.println(line);
                bw.write(line+"\n");
            }
        }
        bw.close();
        stdin.close();
    }
}
