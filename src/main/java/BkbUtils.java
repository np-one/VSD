//import edu.mit.jwi.item.*;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * Created by prajpoot on 28/5/17.
// */
//public class BkbUtils {
//    private static int MEANING_LIMIT = 7;
//    Map<String, String> wsdDomainIdMap;
//    Map<String, List<String>> wordnet3to2Map;
//    PyDictionary wordToSenseId;
//    Map<String, String> senseIdToSynsetIdMap;
//
//
//    public BkbUtils() {
//        wsdDomainIdMap = new HashMap<>();
//        wordnet3to2Map = new HashMap<>();
//        senseIdToSynsetIdMap = new HashMap<>();
//        loadWsdDomainMap();
//        loadWordSynsetIdMap();
//    }
//
//
//
//    public List<String> getHypernames(ISentenceAnalysis sentenceAnalysis) {
//        List<String> hyperNameList = new ArrayList<>();
//        for (IDepNode depNode : sentenceAnalysis.depNodes()) {
//            String result = "NA";
//
//            if (depNode.cpos().toString().equals("NOUN")) {
//
//                String synsetId = getIndexedWordFromWordNet(depNode).split("-")[0];
//                if(!synsetId.isEmpty()) {
//                    List<String> hypList = getHypernymsFromWordId(synsetId,
//                            depNode.pos().toString(), depNode.lemma());
//                    if (!hypList.isEmpty()) {
//                        result = hypList.get(0);
//                        for (int index = 1; index < hypList.size(); index++) {
//                            result = result + ", " + hypList.get(index);
//                        }
//                    }
//                }
//
//                hyperNameList.add(depNode.lemma() + ": " + result);
//            }
//        }
//        return hyperNameList;
//    }
//
//    public String getIndexedWordFromWordNet(IDepNode depNode) {
//        String synsetId = getIndexedWordFromWordNet(depNode.lemma(), depNode.pos().toString());
//        return synsetId.replace("SID-","").toLowerCase();
//    }
//
//
//    private IIndexWord getIndexedWord( String word, POS pos) {
//        IIndexWord idxWord;
//        try {
//            idxWord = dict.getIndexWord(word, pos);
//        } catch (NullPointerException e) {
//            return null;
//        }
//        return idxWord;
//    }
//
//    private POS getPosType(String pos) {
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
//    private void loadWsdDomainMap() {
//
//        try {
//            BufferedReader stdin = new BufferedReader(new FileReader(this.getClass().getClassLoader()
//                    .getResource("wn-domains-3.2-20070223").getPath()));
//            String sentence;
//            while ((sentence = stdin.readLine()) != null) {
//                String[] data = sentence.split("\\t");
//                wsdDomainIdMap.put(data[0], data[1]);
//            }
//
//            stdin = new BufferedReader(new FileReader(BkbUtils.class.getClassLoader()
//                    .getResource("wordnet_2-3_map").getPath()));
//            while ((sentence = stdin.readLine()) != null) {
//                String[] data = sentence.split("\\t");
//                List<String> wordList = new ArrayList<>();
//
//                if (wordnet3to2Map.containsKey(data[1])) {
//                    wordList.addAll(wordnet3to2Map.get(data[1]));
//                    wordList.add(data[0]);
//                    wordnet3to2Map.put(sentence.split("\\t")[1], wordList);
//                } else {
//                    wordList.add(data[0]);
//                    wordnet3to2Map.put(sentence.split("\\t")[1], wordList);
//                }
//            }
//            stdin.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void loadWordSynsetIdMap() {
//
//        PyFile picklefile;
//        picklefile = new PyFile(BkbUtils.class.getClassLoader().getResourceAsStream("word_to_wordnet.pkl"));
//        wordToSenseId = (PyDictionary) cPickle.load(picklefile);
//
//        try {
//            BufferedReader bReader = new BufferedReader(new FileReader(BkbUtils.class.getClassLoader()
//                    .getResource("senseKey_to_synsetId.txt").getPath()));
//            String sentence;
//
//            int line = 0;
//            while ((sentence = bReader.readLine()) != null) {
//                line = line + 1;
//                String[] data = sentence.split(",SID-");
//                if (data.length == 2) {
//                    senseIdToSynsetIdMap.put(data[0], data[1].toLowerCase());
//                }
//            }
//            bReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public String getIndexedWordFromWordNet( String word,  String pos) {
//        IIndexWord indexedWord = getIndexedWord(word, getPosType(pos));
//        if(indexedWord != null) {
//            return indexedWord.getWordIDs().get(0).getSynsetID().toString();
//        }
//        return "";
//    }
//
//    public List<String> getHypernymsFromWordId ( String id,  String posType,  String word) {
//        IIndexWord idxWord = dict.getIndexWord(word, getPosType(posType));
//        int actualId = Integer.parseInt(id);
//        List<String> hyperNyms = new LinkedList<>();
//        for (IWordID wordId : idxWord.getWordIDs()) {
//            if (wordId.getSynsetID().getOffset() == actualId) {
//                ISynset synSet = dict.getWord(wordId).getSynset();
//                List<ISynsetID> hypernyms = synSet.getRelatedSynsets(Pointer.HYPERNYM);
//                for (ISynsetID sid : hypernyms) {
//                    hyperNyms.addAll(dict.getSynset(sid).getWords().stream().map(IWord::getLemma).collect(Collectors.toList()));
//                }
//                return hyperNyms;
//            }
//        }
//        return null;
//    }
//}
