/**
 * Created by prajpoot on 29/5/16.
 */

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class get_word_feature {

         public static final ArrayList<String> params = new ArrayList<String>();
            static {
            params.add("SID-05145753-N");       //value
            params.add("SID-13296311-N");       //outgo
            params.add("SID-13352213-N");       //sum_of_money
            params.add("SID-00033914-N");       //measure
            params.add("SID-11439518-N");       //physical_phenomenon
            params.add("SID-13839738-N");       //percentage
            params.add("SID-13625961-N");       //monetory_unit
            params.add("SID-00004258-N");       //living_thing
            params.add("SID-00022119-N");       //artifact
            params.add("SID-00027365-N");       //location
            params.add("SID-00002137-N");       //abstract entity
            params.add("SID-00001930-N");       //physical_entity
        }

    public static String wordNetDictPath= "/home/prajpoot/WordNet-3.0";
    //static Dictionary dict;

//    public static void init1() throws MalformedURLException {
//        String path = wordNetDictPath + File.separator + "dict";
//      //  ArrayList<String> words = null;
//        //  try {
//        URL url = new URL("file", null, path);
//        // construct the dictionary object and open it
//         dict = new Dictionary(url);
//        dict.open();
//
//    }


    public static ArrayList<Boolean> run(String word,String pos) throws IOException {


        List<String> Answer = new LinkedList<>();
        Answer= getAllHypernyms(word,pos);
        if(word.contains("value")){Answer.add("SID-05145753-N");}
        if(word.contains("outgo")){Answer.add("SID-13296311-N");}
        if(word.contains("sum_of_money")){Answer.add("SID-13352213-N");}
        if(word.contains("measure")){Answer.add("SID-00033914-N");}
        if(word.contains("physical_phenomenon")){Answer.add("SID-11439518-N");}
        if(word.contains("entity")){Answer.add("SID-00001740-N");}
        if(word.contains("percentage")){Answer.add("SID-13839738-N");}
        if(word.contains("living_thing")){Answer.add("SID-00004258-N");}
        if(word.contains("artifact")){Answer.add("SID-00022119-N");}
        if(word.contains("location")){Answer.add("SID-00027365-N");}
        if(word.contains("physical_entity")){Answer.add("SID-00001930-N");}
        if(word.contains("monetory_unit")){Answer.add("SID-13625961-N");}
        if(word.equals("null")){Answer.clear();}





        /*ArrayList<String> params =  new ArrayList<String>();
        params.add("SID-05145753-N");       //value
        params.add("SID-13296311-N");       //outgo
        params.add("SID-13352213-N");       //sum_of_money
        params.add("SID-00033914-N");       //measure
        params.add("SID-11439518-N");       //physical_phenomenon
        params.add("SID-13839738-N");       //percentage
        params.add("SID-13625961-N");       //monetory_unit
        params.add("SID-00004258-N");       //living_thing
        params.add("SID-00022119-N");       //artifact
        params.add("SID-00027365-N");       //location
        params.add("SID-00002137-N");       //abstract entity
        params.add("SID-00001930-N");       //physical_entity*/

        ArrayList<Boolean> values= new ArrayList<Boolean>();


        for(String check:params){
            values.add(Answer.contains(check));
        }
        // System.out.println(Answer);
        // System.out.println(values);
        //System.out.println(params);

        return values;

    }

public static void main(String args[]) throws IOException {
    System.out.println(getIndexedWordNEW("take",getPosType("VB")).get(0).getSynsetID());
}


    public static IIndexWord getIndexedWord( String word,  POS pos) throws IOException {
        IIndexWord idxWord;


        String path = wordNetDictPath + File.separator + "dict";
        ArrayList<String> words = null;
        //  try {
        URL url = new URL("file", null, path);
        // construct the dictionary object and open it
        Dictionary dict = new Dictionary(url);
        dict.open();

        try {
            idxWord = dict.getIndexWord(word, pos);
            dict.close();
        } catch (NullPointerException e) {
            return null;
        }

        return idxWord;
       // dict.close();
    }



    public static List<IWordID> getIndexedWordNEW(String word, POS pos) throws IOException {
        IIndexWord idxWord;


        String path = wordNetDictPath + File.separator + "dict";
        ArrayList<String> words = null;
        //  try {
        URL url = new URL("file", null, path);
        // construct the dictionary object and open it
        Dictionary dict = new Dictionary(url);
        dict.open();

        try {
            idxWord = dict.getIndexWord(word, pos);
            dict.close();
        } catch (NullPointerException e) {
            return null;
        }

        return idxWord.getWordIDs();
        // dict.close();
    }


    public static POS getPosType( String pos) {
        switch (pos) {
            case "NNS":
            case "NNP":
            case "NN":
            case "NNPS":
                return POS.NOUN;
            case "JJ":
            case "JJR":
            case "JJS":
                return POS.ADJECTIVE;
            case "RB":
            case "RBR":
            case "RBS":
                return POS.ADVERB;
            case "VB":
            case "VBD":
            case "VBG":
            case "VBN":
            case "VBP":
            case "VBZ":
                return POS.VERB;
            default:
                return POS.NOUN;
        }
    }

    public static List<String> getAllHypernyms(String word, String posType) throws IOException {

        ArrayList<String> params =  new ArrayList<String>();
        params.add("SID-05145753-N");       //value
        params.add("SID-13296311-N");       //outgo
        params.add("SID-13352213-N");       //sum_of_money
        params.add("SID-00033914-N");       //measure
        params.add("SID-11439518-N");       //physical_phenomenon
        params.add("SID-13839738-N");       //percentage
        params.add("SID-13625961-N");       //monetory_unit
        params.add("SID-00004258-N");       //living_thing
        params.add("SID-00022119-N");       //artifact
        params.add("SID-00027365-N");       //location
        params.add("SID-00002137-N");       //abstract entity
        params.add("SID-00001930-N");       //physical_entity
        params.add("SID-00007347-N");

        String path = wordNetDictPath + File.separator + "dict";
        ArrayList<String> words = null;
        //   try {
        URL url = new URL("file", null, path);
        // construct the dictionary object and open it
        Dictionary dict = new Dictionary(url);
        dict.open();

        List<String> hyperNymsID = new LinkedList();
        List<IWordID> hyperNyms = new LinkedList();
        IIndexWord idxWord = getIndexedWord(word, getPosType(posType));
        if (idxWord == null) {
            dict.close();
            return hyperNymsID;
        }
        ISynset synSet;
        Queue<IWordID> queue = new LinkedList<IWordID>();
        queue.addAll(idxWord.getWordIDs());

        Queue<Integer> sentinel = new LinkedList<Integer>();
        for (int i = 0; i < idxWord.getWordIDs().size(); i++) {
            sentinel.add(0);
        }
        sentinel.add(1);

        while (!queue.isEmpty()) {
            IWordID wordId = queue.poll();
            int temp = sentinel.poll();
            if (temp == 1) {
                sentinel.poll();
            }

            synSet = dict.getWord(wordId).getSynset();
            List<ISynsetID> hypernyms = synSet.getRelatedSynsets(Pointer.HYPERNYM);
            //System.out.print(hypernyms);
            for (ISynsetID sid : hypernyms) {
                for (IWord wordObj : dict.getSynset(sid).getWords()) {
                    if (!hyperNyms.contains(wordObj.getID()))
                    {
                        hyperNyms.add(wordObj.getID());
                        //  System.out.print(wordObj.getLexicalID());
                        queue.add(wordObj.getID());
                        if (temp == 1) {
                            sentinel.add(1);
                        }
                        sentinel.add(0);}
                    // }

                }
            }

        }
        for (IWordID id: hyperNyms){
            hyperNymsID.add(id.getSynsetID().toString());
        }
        dict.close();
        return hyperNymsID;
    }



}
