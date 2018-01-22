/**
 * Created by prajpoot on 7/4/17.
 */
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by asshriva on 10/2/17.
 */

public class testDAN {

    public static void main(String args[]) {
        try {
            run("/home/prajpoot/Desktop/testDAN/13_3layer_new/");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void run(String path) throws IOException, SAXException, ParserConfigurationException {
        getVBclasses getClasses = new getVBclasses();
//        Process p = Runtime.getRuntime().exec("python yourapp.py");
//        System.out.println(getClasses.LookUpMap("keep"));
//        System.out.println(getClasses.LookUpMap("volunteer"));
//        System.out.println(getClasses.LookUpMap("get"));

//        for(String key:getClasses.map.keySet()){
//            System.out.println(key.replace(key.split("-")[0]+"-","").split("-")[0]+" "+getClasses.map.get(key).size());
//        }
        String datapath=path+"results.txt";
        String verbpath=path+"test_DAN";
        String verbListPath = path+"Verbnet32_verblist";
//        Scanner stdin3 = new Scanner(new File(verbpath));
        Scanner stdin3 = new Scanner(new File(verbListPath));
        ArrayList<String> verbList = new ArrayList<>();

//        String vline="";
//        int count=0;
//        while (stdin3.hasNextLine()){
//            vline=stdin3.nextLine();
//            String temp[]=vline.split("\t");
//            System.out.println(temp[1]);
//            if(getClasses.map.containsKey(temp[2]))
//          if(!getClasses.map.get(temp[2]).contains(temp[1]))
//              count+=1;
//
//        }


        while (stdin3.hasNextLine()) {
            verbList.add(stdin3.nextLine().trim());

        }


        BufferedWriter bw = new BufferedWriter(new FileWriter(path+"final-all-top5000-allquery"));



        Scanner stdin = new Scanner(new File(datapath));
        Scanner stdin2 = new Scanner(new File(verbpath));

        String ScurrentLine="";
        Lemmatizer l = new Lemmatizer();
        while (stdin.hasNextLine()){
            ScurrentLine = stdin.nextLine();
            String verb = stdin2.nextLine().split("\t")[1];

            String temp[] = ScurrentLine.split(" ");
            HashMap<String,Integer> collect = new HashMap<>();
            for (String key: temp) {
                if(verbList.contains(key.trim())){
//                    for(String val:getClasses.LookUpMap2(key.trim())) { //new
                    for(String val:getClasses.LookUpMap2(key.trim())) {
                        if (collect.containsKey(val)) {
                            collect.put(val,collect.get((val))+1);
                        }
                        else {
                            collect.put((val),1);
                        }
                    }
                }
                else{
                    for(String val:getClasses.LookUpMap2(l.lemmatize(key.trim()))) {
                        if (collect.containsKey(val)) {
                            collect.put(val,collect.get((val))+1);
                        }
                        else {
                            collect.put((val),1);
                        }
                    }
                }
            }
            ArrayList<String> verbclasses= getClasses.LookUpMap2(verb);
            HashMap<String,Integer> result=new HashMap<>();
            for (String key: verbclasses) {
                if( collect.containsKey(key)) {
                    result.put(key,collect.get(key));
//                    result = result+" "+ key+":"+collect.get(key);
                }
            }

//
//            for(String key:result.keySet()){
//                int val=result.get(key)*100/getClasses.map.get(key).size();
//                result.put(key,val);
//            }
//


            if(result.size()>0) {
                ValueComparator1 bvc = new ValueComparator1(result);
                TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
                sorted_map.putAll(result);
                bw.write(String.valueOf(sorted_map));
                bw.newLine();
            }
            else {
                bw.newLine();
            }



            System.out.println(collect);
        }
        bw.close();

    }

}


class ValueComparator1 implements Comparator<String> {
    Map<String, Integer> base;

    public ValueComparator1(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}

