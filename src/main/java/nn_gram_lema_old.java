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
public class nn_gram_lema_old {
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

    public static void main(String args[]) throws IOException {
        Lemmatizer l = new Lemmatizer();
        //return l.lemmatize(sentence);
        File folder = new File("/home/prajpoot/Downloads/nn-gram-test/");
        //ArrayList<String> result;

        File[] listOfFiles = folder.listFiles();
        //System.out.print(listOfFiles);
        for (int k = 0; k < listOfFiles.length; k++) {
            //result = new ArrayList<>();
            //System.out.println(listOfFiles[k]);
            BufferedReader buf =new BufferedReader(new FileReader(listOfFiles[k]));
            File file = new File("/home/prajpoot/Downloads/new/"+listOfFiles[k].getName());
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            String sCurrentLine="";
            while ((sCurrentLine = buf.readLine()) != null) {
                List<HasWord> tokens = new ArrayList();
                PTBTokenizer<Word> tokenizer = new PTBTokenizer(
                        new StringReader(sCurrentLine), new WordTokenFactory(), "");
                for (Word label; tokenizer.hasNext(); ) {
                    tokens.add(tokenizer.next());
                }
                for(int wl=0;wl<tokens.size();wl++)
                {
                    if(wl==0)
                    { bw.write((l.lemmatize(String.valueOf(tokens.get(wl))).trim()+" ").replace("/$/$","/$").replace("/&/&","/&"));}
                    else
                    { String test[]=String.valueOf(tokens.get(wl)).trim().split("/");
                        for(int ks=0;ks<test.length;ks++)
                        {
                            if(isInteger(test[ks]))
                                bw.write((l.lemmatize(test[ks]).trim().replace("/$/$","/$").replace("/&/&","/&")+" "));
                            else
                                bw.write(l.lemmatize(test[ks]).trim().replace("/$/$","/$").replace("/&/&","/&")+"/");
                        }}}

                //System.out.print("\n");
                bw.newLine();
            }

            buf.close();
            bw.close();
        }

    }
}

