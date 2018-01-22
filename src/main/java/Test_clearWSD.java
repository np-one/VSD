import edu.colorado.clear.wsd.classifier.VerbNetClassifier;
import edu.colorado.clear.wsd.util.ClearNLPInterface;
import jdk.internal.org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static edu.colorado.clear.wsd.run.VNClassify.classify;

/**
 * Created by prajpoot on 10/10/16.
 */
public class Test_clearWSD {

    public static VerbNetClassifier VerbNetClassifier(String vnData){
        try{
            return new VerbNetClassifier(new File(vnData),false);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    static ClearNLPInterface cnlp;static VerbNetClassifier verbNetClassifier;
    static boolean done = false;
    static Map<String,String> framesroles = new HashMap<>();
    static Map<String,String> framesvn = new HashMap<>();

    public static void main(String args[]) throws org.xml.sax.SAXException, ParserConfigurationException, SAXException, IOException {
        if(!done) {
            String vnDataPath = "/home/prajpoot/ClearWSD-master/data/vndata";
            System.out.println("Loading ClearNLP");
            cnlp = new ClearNLPInterface();
            System.out.println("Loading VBNet Classifier");
            verbNetClassifier = VerbNetClassifier(vnDataPath);
            done = true;
            loadmaps();
        }
        String[] sentences = {"The man eats Bombay food.","The mother speaks and the child smiles.","He is playing football.","John killed Josh."};

        Lemmatizer l = new Lemmatizer();

        Map<String, Map<String, String>> vbMap = classify(sentences,verbNetClassifier,cnlp);
        for(String test:vbMap.keySet()){
            for (String val : vbMap.get(test).keySet()){
//                System.out.println("Key: "+l.lemmatize(val)+"\n Val: "+vbMap.get(test).get(val).split("-")[0]); //vn-fn-roles.xml
//                System.out.println(vbMap.get(test).get(val).split("-")[0]+"#"+); //vn-fn-roles.xml

                System.out.println("Verb: "+l.lemmatize(val)+"\n class: "+vbMap.get(test).get(val));   //vn-fn.xml
              //  System.out.println(vbMap.get(test).get(val)+"#"+l.lemmatize(val).trim());   //vn-fn.xml
                if(framesvn.containsKey(vbMap.get(test).get(val)+"#"+l.lemmatize(val).trim())){
                    if( !framesvn.get(vbMap.get(test).get(val)+"#"+l.lemmatize(val).trim()).equals("NA") && !framesvn.get(vbMap.get(test).get(val)+"#"+l.lemmatize(val).trim()).equals("DS") ){
                System.out.println(framesvn.get(vbMap.get(test).get(val)+"#"+l.lemmatize(val).trim()));
                System.out.println(framesroles.get(vbMap.get(test).get(val).split("-")[0]+"#"+framesvn.get(vbMap.get(test).get(val)+"#"+l.lemmatize(val).trim()).trim()));
                System.out.println();
                    }
                    else{
                        System.out.println("No Frame FOUND DS/NA\n");
                    }
                }
                else{
                    System.out.println("No Verb Entry Found\n");
                }

            }
        }

       // System.out.println(vbMap);
       // check();
    }



    public static void loadmaps() throws ParserConfigurationException, IOException, SAXException, org.xml.sax.SAXException {
        File inputFile = new File("/home/prajpoot/Downloads/1.2.2c/vn-fn/vn-fn-roles.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

      //  Map<String,String> frames = new HashMap<>();

        NodeList vnclassNodes = doc.getElementsByTagName("vncls");
        System.out.println(vnclassNodes.getLength());
        for(int i=0; i < vnclassNodes.getLength(); i++) {
            int lenroles = vnclassNodes.item(i).getChildNodes().item(1).getChildNodes().getLength();

            String res="";

            for(int j=0; j < lenroles;j++){
                if(vnclassNodes.item(i).getChildNodes().item(1).getChildNodes().item(j).getNodeName().equals("role"))
                    res= res +" | "+(vnclassNodes.item(i).getChildNodes().item(1).getChildNodes().item(j).getAttributes().getNamedItem("fnrole")+" "+vnclassNodes.item(i).getChildNodes().item(1).getChildNodes().item(j).getAttributes().getNamedItem("vnrole"));
            }

            if (!framesroles.containsKey(vnclassNodes.item(i).getAttributes().getNamedItem("class").getNodeValue()+"#"+vnclassNodes.item(i).getAttributes().getNamedItem("fnframe").getNodeValue()))
                framesroles.put(vnclassNodes.item(i).getAttributes().getNamedItem("class").getNodeValue()+"#"+vnclassNodes.item(i).getAttributes().getNamedItem("fnframe").getNodeValue(),res);
        }





        ///////////////////////

        inputFile = new File("/home/prajpoot/Downloads/1.2.2c/vn-fn/vn-fn.xml");
        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        //  Map<String,String> frames = new HashMap<>();
        vnclassNodes = doc.getElementsByTagName("vncls");
        System.out.println(vnclassNodes.getLength());
        for(int i=0; i < vnclassNodes.getLength(); i++) {

            if (!framesvn.containsKey(vnclassNodes.item(i).getAttributes().getNamedItem("class").getNodeValue()+"#"+vnclassNodes.item(i).getAttributes().getNamedItem("vnmember").getNodeValue()))
                framesvn.put(vnclassNodes.item(i).getAttributes().getNamedItem("class").getNodeValue()+"#"+vnclassNodes.item(i).getAttributes().getNamedItem("vnmember").getNodeValue(),vnclassNodes.item(i).getAttributes().getNamedItem("fnframe").getNodeValue());
        }

    }


}
