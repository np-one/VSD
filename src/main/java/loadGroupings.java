import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by prajpoot on 1/3/17.
 */
public class loadGroupings {

    static ArrayList<String>  cointNullGroup;

    public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException {
        cointNullGroup= new ArrayList<>();

        String dir="/home/prajpoot/vn_group/verbs.colorado.edu/html_groupings_txt";
        File folder = new File(dir+"/");
        HashMap<String, HashMap<String, ArrayList>> verbGroup = new HashMap<>();
        File[] listOfFiles = folder.listFiles();
        for (int i=0; i<listOfFiles.length;i++) {
            if(listOfFiles[i].getName().contains("-v.txt")) {
                BufferedReader br = new BufferedReader(new FileReader(listOfFiles[i]));
                String line;
                String vname = listOfFiles[i].getName().replace("-v.txt","").trim();
                verbGroup.put(vname,new HashMap<String, ArrayList>());
                Boolean collectFlag = false;
                Boolean keyGiven = false;
                HashMap<String, ArrayList> verbSense = new HashMap<>();
                ArrayList<String> examples = new ArrayList<>();
                String key="";
                while ( (line=br.readLine()) != null) {

                    if(line.startsWith("Sense Number")){
                        key = line.split(":")[0].replace("Sense Number","").trim();
                        keyGiven = true;
                    }
                    else {
                        if (line.startsWith("Example")) {
                        collectFlag = true;
                    }
                        else {
                            if(line.contains(":") && keyGiven && collectFlag) {
                                collectFlag = false;
                                ArrayList<String> temp = examples;
                                verbSense.put(key,new ArrayList(temp));
                                examples.clear();
                                keyGiven = false;
                            }
                            else {
                                if(keyGiven && !line.contains(":") && !line.trim().equals(""))
                                    if(!line.contains("("))
                                        if(!line.contains("[")){
                                            if(!line.contains("NP1")){
                                                if(!line.toLowerCase().trim().equals("no examples found"))
                                                    if(!line.contains("\""))
                                                        if(!line.contains("_"))
                                                            if(!line.contains("-"))
                                                    examples.add(line);
                                            }
                                        }
                            }

                    }
                    }
                }
                verbGroup.put(vname,verbSense);
                br.close();
            }
        }
        System.out.println("Done");

      //  Map<String ,ArrayList<String>> resultmap = Create_map("/home/prajpoot/Documents/Python_codes/newest_vn/");
        Map<String ,ArrayList<String>> resultmap2 = Create_map2("/home/prajpoot/Documents/Python_codes/newest_vn/");
        ArrayList<String> classes=getClasses("/home/prajpoot/Documents/Python_codes/newest_vn/");

        HashMap<String,Integer> clascount = new HashMap<>();
        for(String key :resultmap2.keySet()) {
            for(String word: resultmap2.get(key)) {
                if(clascount.containsKey(word.split("#")[0].toLowerCase().trim()))
                    clascount.put((word.split("#")[0]).toLowerCase().trim(),clascount.get(word.split("#")[0].toLowerCase().trim())+1);
                else
                    clascount.put((word.split("#")[0].toLowerCase().trim()),1);
            }
        }



        for(String clas:classes) {
            if(!resultmap2.containsKey(clas.trim()))
                System.out.println(clas.trim());
        }

        for(String classV:resultmap2.keySet()) {
            System.out.println(classV);
            BufferedWriter bw = new BufferedWriter(new FileWriter("/home/prajpoot/Documents/Python_codes/VB_SEEDS_new/"+classV));
            for(String key:resultmap2.get(classV)) {
                String member = key.split("#")[0];
                if(clascount.containsKey(member))
                    if(clascount.get(member).equals(1))
                        continue;

                String[] temp =key.split("#");
                for(int k=1;k<temp.length;k++) {
                    String[] temp2=temp[k].split(",");
                    for(int t=0;t<temp2.length;t++){
                        ArrayList<String> examples=new ArrayList<>();
                        if(verbGroup.containsKey(member))
                        examples= verbGroup.get(member).get(temp2[t].trim());
                        System.out.println("@@@@@"+member);
                        bw.write("@@@@@"+member+"\n");
                        if(examples!=null)
                    for(int e=0;e<examples.size();e++) {
                        System.out.println(examples.get(e));
                        bw.write(examples.get(e)+"\n");
                        }
                    }
                    System.out.println("#################################");
                }
            }
            bw.flush();
            bw.close();
        }

        ArrayList<String> correctverb=new ArrayList<>();
        for(String verb:cointNullGroup) {
            if(clascount.containsKey(verb.split("#")[0]))
            if(!clascount.get(verb.split("#")[0]).equals(1))
                if(!correctverb.contains(verb))
                     correctverb.add(verb);
        }

//        BufferedWriter depWriter = new BufferedWriter(new FileWriter("/home/prajpoot/noexamples"));
//        for(String kk:correctverb) {
//            depWriter.write(kk+"\n");
//        }
//        depWriter.close();
//        System.out.println("Done");
    }


    public static ArrayList <String> getClasses(String Dir) throws IOException, SAXException, ParserConfigurationException {
        Map<String ,ArrayList<String>> resultmap= new HashMap<>();
        ArrayList<String> classes=new ArrayList<>();
        File folder = new File(Dir);
        ArrayList<String> result;
        File[] listOfFiles = folder.listFiles();
        for (int k = 0; k < listOfFiles.length; k++) {
            result=new ArrayList();
            if(listOfFiles[k].getName().contains("dtd"))
                continue;

            Document doc;
            // Make an  instance of the DocumentBuilderFactory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the
            // XML file
            doc = db.parse(new File(Dir+listOfFiles[k].getName()));

            doc.getDocumentElement().normalize();



            NodeList Members = doc.getElementsByTagName("VNCLASS");

            int totalMembers = Members.getLength();
            // System.out.println("Total no of people : " + totalMembers);
           // String parent="";
            for(int s=0; s<Members.getLength() ; s++){
                classes.add(Members.item(s).getAttributes().getNamedItem("ID").getNodeValue().split("-")[1]);
            }

        }
        return classes;
    }







    public static Map<String,ArrayList<String>> Create_map2(String Dir) throws IOException, SAXException, ParserConfigurationException {
        Map<String ,ArrayList<String>> resultmap= new HashMap<>();

        File folder = new File(Dir);
        ArrayList<String> result;
        File[] listOfFiles = folder.listFiles();
        for (int k = 0; k < listOfFiles.length; k++) {
            result=new ArrayList();
            if(listOfFiles[k].getName().contains("dtd"))
                continue;

            Document doc;
            // Make an  instance of the DocumentBuilderFactory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the
            // XML file
            doc = db.parse(new File(Dir+listOfFiles[k].getName()));

            doc.getDocumentElement().normalize();



            NodeList Members = doc.getElementsByTagName("MEMBER");
            int totalMembers = Members.getLength();
            // System.out.println("Total no of people : " + totalMembers);
            String parent="";
            for(int s=0; s<Members.getLength() ; s++){
                //System.out.print(Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","")+" ");
                //VB_3.2
                String name=Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","");
                if(!Members.item(s).getAttributes().getNamedItem("grouping").getNodeValue().equals(""))
                    if(!Members.item(s).getAttributes().getNamedItem("grouping").getNodeValue().contains(name))
                       name=name+"#";
                    else
                        name=name+"#"+Members.item(s).getAttributes().getNamedItem("grouping").getNodeValue().replace(name,"").replace(".0","").replace(" ",", ");
                else {
                    String p =       parent=Members.item(s).getParentNode().getParentNode().getAttributes().getNamedItem("ID").getNodeValue()
                            .replace(Members.item(s).getParentNode().getParentNode().getAttributes().getNamedItem("ID").getNodeValue().split("-")[0]+"-","").split("-")[0];
                    if(!cointNullGroup.contains(name+"#"+p))
                    cointNullGroup.add(name+"#"+p);
                }

                parent=Members.item(s).getParentNode().getParentNode().getAttributes().getNamedItem("ID").getNodeValue()
                            .replace(Members.item(s).getParentNode().getParentNode().getAttributes().getNamedItem("ID").getNodeValue().split("-")[0]+"-","").split("-")[0];
                if(resultmap.containsKey(parent)) {
                    ArrayList<String> temp=resultmap.get(parent);
                    temp.add(name);
                    resultmap.put(parent,temp);
                }
                else {
                    ArrayList<String> temp=new ArrayList<>();
                    temp.add(name);
                    resultmap.put(parent,temp);
                }
            }
                //VB_2.3
                //    String name=Members.item(s).getAttributes().item(0).toString().replace("name=","").replace("\"","");
            // System.out.println(listOfFiles[k].getName()+" "+result);

            //.put(listOfFiles[k].getName(),);
        }
        System.out.print(cointNullGroup);
        return resultmap;
    }


}
