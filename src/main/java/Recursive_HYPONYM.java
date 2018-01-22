/**
 * Created by prajpoot on 20/5/16.
 */

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class Recursive_HYPONYM {

    public static String wordNetDictPath = "/home/prajpoot/WordNet-3.0_new";


    public static void main(String args[]) {

        //nitialize("SID-00004258-N");

        List<String> Answer = new LinkedList<String>();
        Answer = getSynonyms("development");
        // System.out.println(ArrayList.toString(Answer));

        for (String d : Answer) {
            System.out.println(d);
            // prints [Tommy, tiger]
        }

    }

    static POS getPosType(String pos) {
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


    public static List<String> initialize(String synsetId) {

        List<String> hypoNyms = new LinkedList<String>();

        String path = wordNetDictPath + File.separator + "dict";
        ArrayList<String> words = null;
        try {
            URL url = new URL("file", null, path);
            // construct the dictionary object and open it
            Dictionary dict = new Dictionary(url);
            dict.open();

            // words = new ArrayList<String>();
            List<IWord> iWords = dict.getSynset(SynsetID.parseSynsetID(synsetId)).getWords();

            // to get hyponyms
            //  List<IWord> Hyp = dict.getSynset(SynsetID.parseSynsetID(synsetId)).getRelatedSynsets(Pointer.HYPONYM);


            ISynset synSet;
            Queue<IWordID> queue = new LinkedList<IWordID>();
            //queue.addAll(iWords);

            for (IWord iWord : iWords) {
                IWordID word = iWord.getID();
                queue.add(word);
            }


            Queue<Integer> sentinel = new LinkedList<Integer>();
            for (int i = 0; i < queue.size(); i++) {
                sentinel.add(0);
            }
            sentinel.add(1);
            int height = 0;
            while (!queue.isEmpty()) {
                IWordID wordId = queue.poll();
                int temp = sentinel.poll();
                if (temp == 1) {
                    height++;
                    sentinel.poll();
                }

                synSet = dict.getWord(wordId).getSynset();
                List<ISynsetID> hypernyms = synSet.getRelatedSynsets(Pointer.HYPERNYM);
                for (ISynsetID sid : hypernyms) {
                    for (IWord wordObj : dict.getSynset(sid).getWords()) {
                        if (!hypoNyms.contains(wordObj.getLemma())) {
                            hypoNyms.add(wordObj.getLemma());
                            //if (!seen.contains(wordObj.getID())) {
                            queue.add(wordObj.getID());
                            if (temp == 1) {
                                sentinel.add(1);
                            }
                            sentinel.add(0);
                        }
                        // }

                    }
                }

            }
            //return hypoNyms;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return hypoNyms;
    }

    public static List<String> getSynonyms(String word1) {

        List<String> Synonyms = new LinkedList<String>();


        String path = wordNetDictPath + File.separator + "dict";
        ArrayList<String> words = null;
        try {
            URL url = new URL("file", null, path);
            // construct the dictionary object and open it
            Dictionary dict = new Dictionary(url);
            dict.open();

            IIndexWord idxWord = dict.getIndexWord(word1, getPosType("NN"));

            if (idxWord == null) {
                return new ArrayList<>(words);
            }
            IWord wordObj1 = dict.getWord(idxWord.getWordIDs().get(0)); // mostfreq
            ISynset synSet = wordObj1.getSynset();

            List<ISynsetID> hypernyms = synSet.getRelatedSynsets(Pointer.DERIVATIONALLY_RELATED);
//            List<ISynsetID> hyponyms = synSet.getRelatedSynsets(Pointer.HYPONYM);


            for (ISynsetID sid : hypernyms) {
                for (IWord wordObj : dict.getSynset(sid).getWords()) {
                    if (!Synonyms.contains(wordObj.getLemma())) {
                        Synonyms.add(wordObj.getLemma());

                    }

                }

            }

//            for (ISynsetID sid : hyponyms) {
//                for (IWord wordObj : dict.getSynset(sid).getWords()) {
//                    if (!Synonyms.contains(wordObj.getLemma())) {
//                        Synonyms.add(wordObj.getLemma());
//
//                    }
//
//                }
//
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Synonyms;
    }


    public static List<String> initialize2(String word1) {

        List<String> hypoNyms = new LinkedList<String>();


        String path = wordNetDictPath + File.separator + "dict";
        ArrayList<String> words = null;
        try {
            URL url = new URL("file", null, path);
            // construct the dictionary object and open it
            Dictionary dict = new Dictionary(url);
            dict.open();

            IIndexWord idxWord = dict.getIndexWord(word1, getPosType("NN"));

            if (idxWord == null) {
                return new ArrayList<>(words);
            }

            for (int in = 0; in < idxWord.getWordIDs().size(); in++) {
                IWord wordObj1 = dict.getWord(idxWord.getWordIDs().get(in)); // mostfreq
                ISynset synset = wordObj1.getSynset();

                List<IWord> iWords = dict.getSynset(synset.getID()).getWords();

                ISynset synSet;
                Queue<IWordID> queue = new LinkedList<IWordID>();

                for (IWord iWord : iWords) {
                    IWordID word = iWord.getID();
                    queue.add(word);
                }


                Queue<Integer> sentinel = new LinkedList<Integer>();
                for (int i = 0; i < queue.size(); i++) {
                    sentinel.add(0);
                }
                sentinel.add(1);
                int height = 0;
                while (!queue.isEmpty()) {
                    IWordID wordId = queue.poll();
                    int temp = sentinel.poll();
                    if (temp == 1) {
                        height++;
                        sentinel.poll();
                    }

                    synSet = dict.getWord(wordId).getSynset();
                    List<ISynsetID> hypernyms = synSet.getRelatedSynsets(Pointer.HYPERNYM);
                    for (ISynsetID sid : hypernyms) {
                        for (IWord wordObj : dict.getSynset(sid).getWords()) {
                            if (!hypoNyms.contains(wordObj.getLemma())) {
                                hypoNyms.add(wordObj.getLemma());
                                queue.add(wordObj.getID());
                                if (temp == 1) {
                                    sentinel.add(1);
                                }
                                sentinel.add(0);
                            }

                        }
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return hypoNyms;
    }


}
