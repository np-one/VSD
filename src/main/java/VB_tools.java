import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by prajpoot on 8/6/16.
 */
public class VB_tools {

    public static Map<String,ArrayList<String>> Create_map(String Dir) throws IOException, SAXException, ParserConfigurationException {
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

        for(int s=0; s<Members.getLength() ; s++){
            //System.out.print(Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","")+" ");
           //VB_3.2
            String name=Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","");
            //VB_2.3
        //    String name=Members.item(s).getAttributes().item(0).toString().replace("name=","").replace("\"","");
            if(!result.contains(name))
                result.add(name);
        }
           // System.out.println(listOfFiles[k].getName()+" "+result);
            resultmap.put(listOfFiles[k].getName().replace(".xml",""),result);

                    //.put(listOfFiles[k].getName(),);
    }
        //System.out.print(resultmap);
return resultmap;
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

            for(int s=0; s<Members.getLength() ; s++){

                String temp[]=Members.item(s).getParentNode().getParentNode().getAttributes().item(0).toString().replace("ID=","").replace("\"","").split("-");
                String ID = temp[1].trim();
                //System.out.print(Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","")+" ");
                String name=Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","");
                if (resultmap.containsKey(ID)) {
                    ArrayList<String> res_new =resultmap.get(ID);
                    res_new.add(name);
                    resultmap.put(ID,res_new);
                }
                else {
                    ArrayList<String> res_new = new ArrayList<>();
                    res_new.add(name);
                    resultmap.put(ID,res_new);
                }
            }
            // System.out.println(listOfFiles[k].getName()+" "+result);
            //resultmap.put(listOfFiles[k].getName().replace(".xml",""),result);

            //.put(listOfFiles[k].getName(),);
        }
        //System.out.print(resultmap);
        return resultmap;
    }



    public static Map<String,ArrayList<String>> Create_mapoldVB(String Dir) throws IOException, SAXException, ParserConfigurationException {
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

            for(int s=0; s<Members.getLength() ; s++){

                String temp[]=Members.item(s).getParentNode().getParentNode().getAttributes().item(0).toString().replace("ID=","").replace("\"","").split("-");
                String ID = temp[1].trim();
                //System.out.print(Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","")+" ");
                String name=Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","");
                if (resultmap.containsKey(ID)) {
                    ArrayList<String> res_new =resultmap.get(ID);
                    res_new.add(name);
                    resultmap.put(ID,res_new);
                }
                else {
                    ArrayList<String> res_new = new ArrayList<>();
                    res_new.add(name);
                    resultmap.put(ID,res_new);
                }
            }
            // System.out.println(listOfFiles[k].getName()+" "+result);
            //resultmap.put(listOfFiles[k].getName().replace(".xml",""),result);

            //.put(listOfFiles[k].getName(),);
        }
        //System.out.print(resultmap);
        return resultmap;
    }


    public static Map<String,ArrayList<String>> Create_map2_sublevel(String Dir) throws IOException, SAXException, ParserConfigurationException {
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

            for(int s=0; s<Members.getLength() ; s++){

                String temp=Members.item(s).getParentNode().getParentNode().getAttributes().item(0).toString().replace("ID=","").replace("\"","");
                String ID = temp;
                //System.out.print(Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","")+" ");
                String name=Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","");
                if (resultmap.containsKey(ID)) {
                    ArrayList<String> res_new =resultmap.get(ID);
                    res_new.add(name);
                    resultmap.put(ID,res_new);
                }
                else {
                    ArrayList<String> res_new = new ArrayList<>();
                    res_new.add(name);
                    resultmap.put(ID,res_new);
                }
            }
            // System.out.println(listOfFiles[k].getName()+" "+result);
            //resultmap.put(listOfFiles[k].getName().replace(".xml",""),result);

            //.put(listOfFiles[k].getName(),);
        }
        //System.out.print(resultmap);
        return resultmap;
    }




    public static Map<String,ArrayList<String>> get_rev_map(String Dir) throws IOException, SAXException, ParserConfigurationException {
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

            for(int s=0; s<Members.getLength() ; s++){

                String temp[]=Members.item(s).getParentNode().getParentNode().getAttributes().item(0).toString().replace("ID=","").replace("\"","").split("-");
                String ID =temp[1].trim();
                //System.out.print(Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","")+" ");
                String name=Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","");
                if (resultmap.containsKey(ID)) {
                    ArrayList<String> res_new =resultmap.get(ID);
                    res_new.add(name);
                    resultmap.put(ID,res_new);
                }
                else {
                    ArrayList<String> res_new = new ArrayList<>();
                    res_new.add(name);
                    resultmap.put(ID,res_new);
                }
            }
            // System.out.println(listOfFiles[k].getName()+" "+result);
            //resultmap.put(listOfFiles[k].getName().replace(".xml",""),result);

            //.put(listOfFiles[k].getName(),);
        }
        //System.out.print(resultmap);
        return resultmap;
    }



    public static void main(String args[]) {

        try {
            Map<String,ArrayList<String>> map = Create_map2("/home/prajpoot/Downloads/new_VN/");
            Map<String,ArrayList<String>> map2 = Create_map("/home/prajpoot/Downloads/new_vn/");
            Map<String,ArrayList<String>> map3 = get_rev_map("/home/prajpoot/Downloads/new_VN/");
            System.out.println("TEST");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        System.out.println("TEST");
    }




    public Map<String,ArrayList<String>> trim_map(Map<String,ArrayList<String>> map){
        Map<String,ArrayList<String>> pp= new HashMap<String,ArrayList<String>>();
        Map<String,Integer> test = new HashMap<String,Integer>();

        for(HashMap.Entry<String,ArrayList<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            for(int i=0;i<entry.getValue().size();i++){
                if(test.containsKey(entry.getValue().get(i)))
                    test.put(entry.getValue().get(i),test.get(entry.getValue().get(i))+1);
                else
                    test.put(entry.getValue().get(i),1);
            }

       // System.out.println("################\n"+test);
         //   System.out.print(test.size());

    }

        for(HashMap.Entry<String,ArrayList<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            ArrayList<String> val= new ArrayList<String>();
            for(int i=0;i<entry.getValue().size();i++) {
                if(test.get(entry.getValue().get(i))==1)
                    val.add(entry.getValue().get(i).toString());

            }
            pp.put(key,val);
            //}
            // ...
        }
/*
    public void getClasses(String word) throws ParserConfigurationException, SAXException, IOException {
        String classes[]={"appear-48.1.1", "assuming_position-50", "calibratable_cos-45.6", "escape-51.1", "spatial_configuration-47.6"};

        for(int i=0;i<classes.length;i++)
        // VB_tools p = new VB_tools("/home/prajpoot/Downloads/new_vn/", "escape-51.1.xml");
        //VB_tools p = new VB_tools();
        {
            if(Get_Members("/home/prajpoot/Downloads/new_vn/", classes[i]+".xml").contains(word))
                System.out.println(classes[i]);
        }
              //return appear;
    }*/
        return pp;
}
}
