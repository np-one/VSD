import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by prajpoot on 14/6/16.
 */
public class nn_gram_lema {
    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String serializedClassifier = "/home/prajpoot/Downloads/stanford-english-corenlp-2016-01-10-models/edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz";
        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);

      //  Lemmatizer l = new Lemmatizer();
        //return l.lemmatize(sentence);
        File folder = new File("/home/prajpoot/Downloads/new");
        //ArrayList<String> result;

        File[] listOfFiles = folder.listFiles();
        //System.out.print(listOfFiles);
        for (int k = 0; k < listOfFiles.length; k++) {
            //result = new ArrayList<>();
            //System.out.println(listOfFiles[k]);
            BufferedReader buf =new BufferedReader(new FileReader(listOfFiles[k]));
            File file = new File("/home/prajpoot/Downloads/new2/"+listOfFiles[k].getName());
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            String sCurrentLine="";
            while ((sCurrentLine = buf.readLine()) != null) {
                List<HasWord> tokens = new ArrayList();
                PTBTokenizer<Word> tokenizer = new PTBTokenizer(
                        new StringReader(sCurrentLine), new WordTokenFactory(), "");
                for (String word : sCurrentLine.split(" ")) {
                    tokens.add(new Word(word));
                }
                ArrayList<String> NER =new ArrayList<String>();


                String test2="";
                for(int wl=1;wl<tokens.size();wl++)
                {
                    if(tokens.get(wl).toString().split("/").length>2)
                        test2=test2+tokens.get(wl).toString().split("/")[0]+" ";

                }

                String NERResult=classifier.classifyToString(test2);

                List<HasWord> tokens2 = new ArrayList();
                for (String word : NERResult.split(" ")) {
                    tokens2.add(new Word(word));
                }

                int ind=0;

                System.out.println(sCurrentLine);
                System.out.println(test2);
                System.out.println(tokens2);

                for(int wl=0;wl<tokens.size();wl++)
                {
                    if(wl==0)
                    { bw.write(((String.valueOf(tokens.get(wl))).trim()+" "));}
                    else
                    {// String test[]=String.valueOf(tokens.get(wl)).trim().split("/");

                            if(tokens.get(wl).toString().split("/").length>2 && ind<tokens.size()-3 && tokens2.size()>ind){
                            //if(isInteger(test[ks]))
                               // if()
                                bw.write(String.valueOf(tokens.get(wl))+"/"+tokens2.get(ind).toString().split("/")[tokens2.get(ind).toString().split("/").length-1]+" ");
                               // else
                                 //   bw.write(String.valueOf(tokens.get(wl))+" ");

                            ind=ind+1;
                            }
                            else
                            {
                                bw.write(String.valueOf(tokens.get(wl))+" ");
                             //   ind=ind+1;
                            }
                        }}

                //System.out.print("\n");
                bw.newLine();
            }

            buf.close();
            bw.close();
        }

    }
}

