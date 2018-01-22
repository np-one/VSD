import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;


/**
 * Created by prajpoot on 30/6/16.
 */
public class get_WN_words {
    public static String file;
    static String path;
    static URL url;
    static  HashMap<String,String> map = new HashMap<String, String>();
    public static Dictionary dict;
    static HashMap<String,ArrayList<String>> membermap = new HashMap<>();
    private static int MEANING_LIMIT = 7;

    public static void init() throws IOException, ParserConfigurationException, SAXException {
        file  = "/home/prajpoot/WordNetExtraction/SenseKeyToSynsetIDMapping.txt";
        path = "/home/prajpoot/Downloads/WordNet-3.0"+File.separator + "dict";
        url = new URL("file", null, path);
        // construct the dictionary object and open it
        dict = new Dictionary(url);
        dict.open();
        map= getmap(file);



        ///////////////////////////////////////////////////////////////////////////
        Map<String ,ArrayList<String>> resultmap= new HashMap<>();
        String Dir = "/home/prajpoot/Downloads/new_vn/";
        File folder = new File(Dir);
        ArrayList<String> result = null;
        result=new ArrayList<>();
        File[] listOfFiles = folder.listFiles();
        for (int k = 0; k < listOfFiles.length; k++) {

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


/*
            String ID = Members.item(s).getParentNode().getParentNode().getAttributes().item(0).toString().replace("ID=","").replace("\"","");
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
            }*/

            for(int s=0; s<Members.getLength() ; s++){
                String name=Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","");
                if (membermap.containsKey(name.trim())){
                    ArrayList<String> temp=membermap.get(name.trim());
                    temp.add(String.valueOf(Members.item(s).getAttributes().getNamedItem("wn").getNodeValue()));
                    membermap.put(name.trim(),temp);
                }

                else{
                    ArrayList<String> temp=new ArrayList<>();
                    temp.add(String.valueOf(Members.item(s).getAttributes().getNamedItem("wn").getNodeValue()));
                    membermap.put(name.trim(),temp);
                }
                    //membermap.put(name.trim(),String.valueOf(Members.item(s).getAttributes().getNamedItem("wn").getNodeValue()));

            }
        }
        ///System.out.println(membermap.get("rise"));
        //////////////////////////////////////////////////////////////////////////////


    }



    public static void init2() throws IOException, ParserConfigurationException, SAXException {
        file  = "/home/prajpoot/WordNetExtraction/SenseKeyToSynsetIDMapping.txt";
        path = "/home/prajpoot/Downloads/WordNet-3.0"+File.separator + "dict";
        url = new URL("file", null, path);
        // construct the dictionary object and open it
        dict = new Dictionary(url);
        dict.open();


        map= getmap(file);


        ///////////////////////////////////////////////////////////////////////////
        Map<String ,ArrayList<String>> resultmap= new HashMap<>();
        String Dir = "/home/prajpoot/Downloads/new_vn/";
        File folder = new File(Dir);
        ArrayList<String> result = null;
        result=new ArrayList<>();
        File[] listOfFiles = folder.listFiles();
        for (int k = 0; k < listOfFiles.length; k++) {

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


            /*
            String ID = Members.item(s).getParentNode().getParentNode().getAttributes().item(0).toString().replace("ID=","").replace("\"","");
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
            }*/


/*            for(int s=0; s<Members.getLength() ; s++){
                String name=Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","");
                if (membermap.containsKey(Members.item(s).getParentNode().getParentNode().getAttributes().item(0).toString().replace("ID=","").replace("\"",""))){
                    ArrayList<String> temp=membermap.get(Members.item(s).getParentNode().getParentNode().getAttributes().item(0).toString().replace("ID=","").replace("\"",""));
                    temp.add(name.trim()+"="+String.valueOf(Members.item(s).getAttributes().getNamedItem("wn").getNodeValue()));
                    membermap.put(Members.item(s).getParentNode().getParentNode().getAttributes().item(0).toString().replace("ID=","").replace("\"",""),temp);
                }

                else{
                    ArrayList<String> temp=new ArrayList<>();
                    temp.add(name.trim()+"="+String.valueOf(Members.item(s).getAttributes().getNamedItem("wn").getNodeValue()));

                    membermap.put(Members.item(s).getParentNode().getParentNode().getAttributes().item(0).toString().replace("ID=","").replace("\"",""),temp);
                }*/


            for(int s=0; s<Members.getLength() ; s++){
                String name=Members.item(s).getAttributes().item(1).toString().replace("name=","").replace("\"","");
                String temp1[]=Members.item(s).getParentNode().getParentNode().getAttributes().item(0).toString().replace("ID=","").replace("\"","").split("-");
                String ID = temp1[1].trim()/*+"-"+temp1[1].trim()*/;
                //old//String ID = listOfFiles[k].getName().trim().replace(".xml","");
                if (membermap.containsKey(ID)){
                    ArrayList<String> temp=membermap.get(ID);
                    temp.add(name.trim()+"="+String.valueOf(Members.item(s).getAttributes().getNamedItem("wn").getNodeValue()));
//                    membermap.put(listOfFiles[k].getName().trim().replace(".xml",""),temp);
                    membermap.put(ID,temp);
                }

                else{
                    ArrayList<String> temp=new ArrayList<>();
                    temp.add(name.trim()+"="+String.valueOf(Members.item(s).getAttributes().getNamedItem("wn").getNodeValue()));

//                    membermap.put(listOfFiles[k].getName().trim().replace(".xml",""),temp);
                    membermap.put(ID,temp);
                }
                //membermap.put(name.trim(),String.valueOf(Members.item(s).getAttributes().getNamedItem("wn").getNodeValue()));

            }
        }
        ///System.out.println(membermap.get("rise"));
        //////////////////////////////////////////////////////////////////////////////


    }


    public static void main(String args[]) throws IOException, SAXException, ParserConfigurationException {
        init2();
       // System.out.println(membermap);
        System.out.println(getwN("refer"));

    }

    public static List<String> getwN(String wordtest) throws ParserConfigurationException, SAXException, IOException {


        //getmap(file);

        ArrayList<String> result= run(wordtest);
        //System.out.println(result);

        ArrayList<ISynsetID> relates = new ArrayList<>();


        for(int i=0;i<result.size();i++){

            if(result.get(i)=="")
                continue;
            String temp[]=result.get(i).split(" ");
            for(int k=0;k<temp.length;k++){
                //System.out.println(temp[k]+" "+map.get(temp[k]));

                if(map.get(temp[k])==null)
                    continue;
               // System.out.println(map.get(temp[k]));
                List<IWord> iWords = dict.getSynset(SynsetID.parseSynsetID(map.get(temp[k]))).getWords();
                ISynset synSet;
                LinkedList<IWordID> list = new LinkedList<IWordID>();
                //queue.addAll(iWords);

                for (IWord iWord : iWords) {
                    IWordID word = iWord.getID();
                    list.add(word);
                }

        ////-----------------------------------------can be added here--------------------------------------------------------------------------------------------------------------------------------------------------------------------////////////////////

                for (IWordID wordId: list) {
                    synSet = dict.getWord(wordId).getSynset();
                    relates.add(synSet.getID());
                    List<ISynsetID>   hypernyms= synSet.getRelatedSynsets(Pointer.HYPERNYM);
                    List<ISynsetID> hyponyms= synSet.getRelatedSynsets(Pointer.HYPONYM);
                    List<ISynsetID> antnyms= synSet.getRelatedSynsets(Pointer.ANTONYM);
                    for(int lk=0;lk<hypernyms.size();lk++){
                        if(!relates.contains(hypernyms.get(lk)))
                            relates.add(hypernyms.get(lk));
                    }
                    for(int lk=0;lk<hyponyms.size();lk++){
                        if(!relates.contains(hyponyms.get(lk)))
                            relates.add(hyponyms.get(lk));
                    }
                  //  System.out.println(hyponyms.size());
                    for(int m=0;m<antnyms.size();m++){
                        if(!relates.contains(antnyms.get(m)))
                        relates.add(antnyms.get(m));
                    }


                }
                //---------------------------------------------------------------------------------------------------------------------------------------------------------------------///////////////////////
            }

        }

        List<String> finalres = new LinkedList<String>();
        for(int t=0;t<relates.size();t++){
            List<IWord> temp=dict.getSynset(SynsetID.parseSynsetID((relates.get(t).toString()))).getWords();
            for(int o=0;o<temp.size();o++){
                if(!finalres.contains(temp.get(o).toString().split("-")[temp.get(o).toString().split("-").length-1]))
                    finalres.add(temp.get(o).toString().split("-")[temp.get(o).toString().split("-").length-1]);
            }
        }
        return finalres;
        //System.out.println(finalres);
       // System.out.println(relates);
    }


//    public static void List<String> getpossibleVerbsSense(String){
//
//    }

    public static List<String> getwN3(String wordtest, IWordID classname) throws ParserConfigurationException, SAXException, IOException {

        List<String> res = new ArrayList<>();
        //ISynset synSet;
        //synSet = dict.getWord(classname).getSynset();

      //  IWord wordObj = dict.getWord(classname);
        ISynset synset = dict.getSynset(classname.getSynsetID());
        if(synset!=null)
        for (IWord w : synset.getWords()) {
            res.add(w.getLemma());
        }
        else {
        res.add("AAAAA");
        res.add("BBBBB");
        }
        if (res.size()==1){
            List<ISynsetID>   hypernyms= synset.getRelatedSynsets(Pointer.HYPERNYM);
            List<ISynsetID>   hyponyms= synset.getRelatedSynsets(Pointer.HYPONYM);

//            for(ISynsetID id:hypernyms){
//                for (IWord w : dict.getSynset(id).getWords()) {
//                    res.add(w.getLemma().toString().split("_")[0]);
//                }
//
////                res.add(dict.getSynset(id).getWord(0).getLemma());
//            }
            for(ISynsetID id:hyponyms){
                for (IWord w : dict.getSynset(id).getWords()) {
                    if(!res.contains(w.getLemma().toString().split("_")[0]))
                    res.add(w.getLemma().toString().split("_")[0]);
                }
            }
        }
       return res;
    }



    public static HashMap<String,String> getmap(String file) throws IOException {
        HashMap<String,String> map= new HashMap<String,String>();
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String sCurrentLine="";
        String temp[];
        int line=0;
        while ((sCurrentLine = bf.readLine()) != null) {
            line=line+1;
            temp=sCurrentLine.split("::,");
            //System.out.println(temp.length+" line: "+line);
            if(temp.length==2)
            map.put(temp[0],temp[1]);
        }
        bf.close();
        return map;
    }

    public static ArrayList<String> run2(String word, String classname){
        String temp="";
        ArrayList<String> test= new ArrayList<>();
        classname=classname.split("-")[1];
        if(membermap.containsKey(classname)){
            //System.out.println("aaaya");
            ArrayList<String> mems= new ArrayList<>();
            mems=   membermap.get(classname);
            for(int i=0;i<mems.size();i++){
                temp=mems.get(i);
               // System.out.println(temp + " "+mems.size());
                //3.2
                if(temp.split("=")[0].trim().equals(word))
                    if(temp.split("=").length==2){
                        String t1[]= temp.split("=")[1].split(" ");
                        for(int k=0;k<t1.length;k++){
                            test.add(t1[k]);
                        }
                        break;
                    }
//                2.3
/*                if(temp.split("=").length>1)
                if(temp.split("=")[1].contains(word)) {
                    String t1[]= temp.split("=")[1].split(" ");
                    for(int k=0;k<t1.length;k++){
                        test.add(t1[k]);
                    }
                    break;
                }*/

            }
        }
        return test;
    }

    public static ArrayList<String> run(String word) throws IOException, SAXException, ParserConfigurationException {

        ArrayList<String> res=new ArrayList<>();
       // res=membermap.get(word);
        if(membermap.containsKey(word))
            return(membermap.get(word));
        else
            return res;
    }


    public static List<String> getwN2(String wordtest, String classname) throws ParserConfigurationException, SAXException, IOException {


        //getmap(file);

        ArrayList<String> result= run2(wordtest,classname);
        System.out.println(result);
        if(result.size()==0){
            System.out.println(" NO WN ");
        }

        ArrayList<ISynsetID> relates = new ArrayList<>();


        for(int i=0;i<result.size();i++){

            if(result.get(i)=="")
                continue;
            String temp[]=result.get(i).split(" ");
            for(int k=0;k<temp.length;k++){
                //System.out.println(temp[k]+" "+map.get(temp[k]));

                if(map.get(temp[k])==null)
                    continue;
                // System.out.println(map.get(temp[k]));
                List<IWord> iWords = dict.getSynset(SynsetID.parseSynsetID(map.get(temp[k]))).getWords();
                ISynset synSet;
                LinkedList<IWordID> list = new LinkedList<IWordID>();
                //queue.addAll(iWords);

                for (IWord iWord : iWords) {
                    IWordID word = iWord.getID();
                    list.add(word);
                }

                ////-----------------------------------------can be added here--------------------------------------------------------------------------------------------------------------------------------------------------------------------////////////////////

                for (IWordID wordId: list) {
                    synSet = dict.getWord(wordId).getSynset();
                    if(!synSet.getPOS().toString().trim().equals("verb")){

                        System.out.println("fail");
                        continue;
                    }

                    System.out.println(synSet.getPOS());
                    relates.add(synSet.getID());
                    List<ISynsetID>   hypernyms= synSet.getRelatedSynsets(Pointer.HYPERNYM);
                    List<ISynsetID> hyponyms= synSet.getRelatedSynsets(Pointer.HYPONYM);
//                    List<ISynsetID> antnyms= synSet.getRelatedSynsets(Pointer.VERB_GROUP);
                    for(int lk=0;lk<hypernyms.size();lk++){
                        if(!relates.contains(hypernyms.get(lk)))
                            relates.add(hypernyms.get(lk));
                    }
                    for(int lk=0;lk<hyponyms.size();lk++){
                        if(!relates.contains(hyponyms.get(lk)))
                            relates.add(hyponyms.get(lk));
                    }
                    //  System.out.println(hyponyms.size());
//                    for(int m=0;m<antnyms.size();m++){
//                        if(!relates.contains(antnyms.get(m)))
//                            relates.add(antnyms.get(m));
//                    }


                }
                //---------------------------------------------------------------------------------------------------------------------------------------------------------------------///////////////////////
            }

        }

        List<String> finalres = new LinkedList<String>();
        for(int t=0;t<relates.size();t++){
            List<IWord> temp=dict.getSynset(SynsetID.parseSynsetID((relates.get(t).toString()))).getWords();
            for(int o=0;o<temp.size();o++){
                if(!finalres.contains(temp.get(o).toString().split("-")[temp.get(o).toString().split("-").length-1]))
                    finalres.add(temp.get(o).toString().split("-")[temp.get(o).toString().split("-").length-1]);
            }
        }
        return finalres;
        //System.out.println(finalres);
        // System.out.println(relates);
    }







































































//    private List<ISynset> getAllSynsets( List<IWordID> wordIds) {
//        return wordIds.stream().map(id -> dict.getWord(id).getSynset()).collect(Collectors.toList());
//    }
//
//    private IIndexWord getIndexedWord( String word,  POS pos) {
//        IIndexWord idxWord;
//        try {
//            idxWord = dict.getIndexWord(word, pos);
//        } catch (NullPointerException e) {
//            return null;
//        }
//        return idxWord;
//    }
//
//    private List<IWord> getWords(ISynsetID sid) {
//        return dict.getSynset(sid).getWords();
//    }
//
//    private IWord getWord(IWordID wid) {
//        return dict.getWord(wid);
//    }
//
//    private POS getPosType( String pos) {
//        switch (pos) {
//            case "NNS":
//            case "NNP":
//            case "NN":
//            case "NNPS":
//                return POS.NOUN;
//            case "JJ":
//            case "JJR":
//            case "JJS":
//                return POS.ADJECTIVE;
//            case "RB":
//            case "RBR":
//            case "RBS":
//                return POS.ADVERB;
//            case "VB":
//            case "VBD":
//            case "VBG":
//            case "VBN":
//            case "VBP":
//            case "VBZ":
//                return POS.VERB;
//            default:
//                return POS.NOUN;
//        }
//    }
//
//    /**
//     * @param word    string.
//     * @param posType part of speech tag.
//     * @return Synonyms
//     */
//    @Override
//    public List<String> getSynonyms( String word,  String posType) {
//        Set<String> synonyms = new LinkedHashSet<>();
//        IIndexWord idxWord = dict.getIndexWord(word, getPosType(posType));
//        if (idxWord == null) {
//            return new ArrayList<>(synonyms);
//        }
//        int numMeanings = 0;
//        for (IWordID wordId : idxWord.getWordIDs()) {
//            IWord wordObj = dict.getWord(wordId);
//            ISynset synset = wordObj.getSynset();
//            for (IWord w : synset.getWords()) {
//                if (word.equals(w.getLemma())) {
//                    continue;
//                }
//                synonyms.add(w.getLemma());
//            }
//            if (++numMeanings == 7) {
//                break;
//            }
//        }
//        return new ArrayList<>(synonyms);
//    }
//
//
//    @Override
//    public List<String> getHypernyms( String word,  String posType) {
//        List<String> hyperNyms = new LinkedList<>();
//        IIndexWord idxWord = getIndexedWord(word, getPosType(posType));
//        if (idxWord == null) {
//            return hyperNyms;
//        }
//        ISynset synSet;
//        for (IWordID wordId : idxWord.getWordIDs()) {
//            synSet = dict.getWord(wordId).getSynset();
//            List<ISynsetID> hypernyms = synSet.getRelatedSynsets(Pointer.HYPERNYM);
//            for (ISynsetID sid : hypernyms) {
//                for (IWord wordObj : dict.getSynset(sid).getWords()) {
//                    hyperNyms.add(wordObj.getLemma());
//                }
//            }
//        }
//        return hyperNyms;
//    }
//
//
//
//
//
//
//    public List<String> getHyponyms( String word,  String posType) {
//        List<String> hyperNyms = new LinkedList<>();
//        IIndexWord idxWord = dict.getIndexWord(word, getPosType(posType));
//        if (idxWord == null) {
//            return hyperNyms;
//        }
//        ISynset synSet;
//        for (IWordID wordId : idxWord.getWordIDs()) {
//            synSet = dict.getWord(wordId).getSynset();
//            List<ISynsetID> hypernyms = synSet.getRelatedSynsets(Pointer.HYPONYM);
//            for (ISynsetID sid : hypernyms) {
//                hyperNyms.addAll(dict.getSynset(sid).getWords().stream().map(IWord::getLemma).collect(Collectors.toList()));
//            }
//        }
//        return hyperNyms;
//    }
//
//
//    public List<String> getSuperSenses( String word,  String pos) {
//        POS posWord = getPosType(pos.toUpperCase());
//        IIndexWord idxWord = getIndexedWord(word, posWord);
//        List<String> superSenses = new ArrayList<String>();
//        if (idxWord != null) {
//            for (IWordID wordId : idxWord.getWordIDs()) {
//                IWord indexWord = dict.getWord(wordId);
//                superSenses.add(indexWord.getSynset().getLexicalFile().getName().split("\\.")[1]);
//            }
//        }
//        return superSenses;
//    }
//
//
//
//
//
//    public List<String> getEntailments( String word,  String posType) {
//        List<String> entailments = new LinkedList<>();
//        IIndexWord idxWord = getIndexedWord(word, getPosType(posType));
//        if (idxWord == null) {
//            return entailments;
//        }
//        ISynset synSet;
//        for (IWordID wordId : idxWord.getWordIDs()) {
//            synSet = dict.getWord(wordId).getSynset();
//            List<ISynsetID> entailedWords = synSet.getRelatedSynsets(Pointer.ENTAILMENT);
//            for (ISynsetID sid : entailedWords) {
//                for (IWord wordObj : dict.getSynset(sid).getWords()) {
//                    entailments.add(wordObj.getLemma());
//                }
//            }
//        }
//        return entailments;
//    }
//
//    public boolean verbExists( String word) {
//        IIndexWord idxWord = dict.getIndexWord(word, POS.VERB);
//        if (idxWord == null) {
//            return false;
//        }
//        return true;
//    }
//
//    public boolean nounExists( String word) {
//        IIndexWord idxWord = dict.getIndexWord(word, POS.NOUN);
//        if (idxWord == null) {
//            return false;
//        }
//        return true;
//    }
//
//
//
//
//    public List<String> getDerivationallyRelatedTerms( String word,  String posType) {
//        List<String> derivedTerms = new LinkedList<>();
//        if (posType == null) {
//            return derivedTerms;
//        }
//        POS posTypeEnum = getPosType(posType);
//        IIndexWord idxWord = dict.getIndexWord(word, posTypeEnum);
//        if (idxWord == null) {
//            return derivedTerms;
//        }
//        int numMeanings = 0;
//        Word wnWord;
//        for (IWordID wordId : idxWord.getWordIDs()) {
//            if (++numMeanings == 7) {
//                break;
//            }
//            if (wordId == null) {
//                continue;
//            }
//            IWord wordObj = dict.getWord(wordId);
//            IWord tempWord;
//            Map<IPointer, List<IWordID>> relatedMap = wordObj.getRelatedMap();
//            for (IPointer temp : relatedMap.keySet()) {
//                if (temp.equals(Pointer.DERIVATIONALLY_RELATED)) {
//                    for (IWordID wordid : relatedMap.get(temp)) {
//                        derivedTerms.add(dict.getWord(wordid).getLemma());
//                    }
//                }
//            }
//        }
//        return derivedTerms;
//    }
//
//
//    public boolean isHypernym( String word1,  String word2,  String type) {
//        try {
//            if (type == null) {
//                return false;
//            }
//            //IIndexWord idxWord = dict.getIndexWord (word1,type) ;
//            IIndexWord idxWord = getIndexedWord(word1, getPosType(type));
//            if (idxWord == null) {
//                return false;
//
//            }
//            List<IWordID> wordId = idxWord.getWordIDs();
//            List<ISynset> synsets = getAllSynsets(wordId);
//            for (ISynset syn : synsets) {
//                if (containsWord(syn.getWords(), word2)) {
//                    return true;
//                } else {
//                    List<IWord> words;
//                    List<ISynsetID> hypernyms = syn.getRelatedSynsets(Pointer.HYPERNYM);
//                    for (ISynsetID sid : hypernyms) {
//                        //words = dict.getSynset(sid).getWords() ;
//                        words = getWords(sid);
//                        if (containsWord(words, word2)) {
//                            return true;
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
//
//
//
//
//
//    private boolean containsWord(List<IWord> array, String word) {
//        if ((word != null) && (array.size() != 0)) {
//            for (IWord temp : array) {
//                if (temp.getLemma().trim().equalsIgnoreCase(word.trim())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//
//
//
//
//
//
//
//
//
//
//











}
