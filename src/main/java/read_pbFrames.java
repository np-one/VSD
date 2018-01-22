import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by prajpoot on 24/5/17.
 */
public class read_pbFrames {
    public static void main(String args[]) {
        System.out.println(getRoles("abandon.01"));
    }

    public static Map<String, String> getRoles(String group) {
        String Dir = "/home/prajpoot/Desktop/pb_frames/";
        Map<String, String> result = new HashMap<String, String>();
        Document doc;
        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // use the factory to take an instance of the document builder
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        // parse using the builder to get the DOM mapping of the
        // XML file
        try {
            doc = db.parse(new File("/home/prajpoot/Desktop/Analysis/pb_frames.xml"));
            doc.getDocumentElement().normalize();
            NodeList Members = doc.getElementsByTagName("roleset");

            for (int s = 0; s < Members.getLength(); s++) {
                if (Members.item(s).getAttributes().getNamedItem("id").getNodeValue().equals(group)) {
//                 System.out.println("sssssssssss");
                    NodeList roles = Members.item(s).getChildNodes().item(1).getChildNodes();
                    for (int n = 0; n < roles.getLength(); n++) {
                        if (roles.item(n).getAttributes() != null) {
                            if (roles.item(n).getAttributes().getLength() == 2) {
                                String value = (roles.item(n).getAttributes().getNamedItem("descr").getNodeValue());
                                String key = ("A" + roles.item(n).getAttributes().getNamedItem("n").getNodeValue());
                                result.put(key, value);
                            }

                            if (roles.item(n).getAttributes().getLength() == 3) {
                                String value = (roles.item(n).getAttributes().getNamedItem("descr").getNodeValue());
                                String key = ("A" + roles.item(n).getAttributes().getNamedItem("n").getNodeValue() + "-" + roles.item(n).getAttributes().getNamedItem("f").getNodeValue());
                                result.put(key, value);
//                        System.out.println(roles.item(n).getAttributes().getNamedItem("f").getNodeValue());
                            }
                        }
                    }
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
