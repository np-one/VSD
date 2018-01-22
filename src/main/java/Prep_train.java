/**
 * Created by prajpoot on 3/6/16.
 */
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Prep_train {

    // get_word_feature o;
    public static void main(String args[]) throws IOException {

        ArrayList<String> prep_loc= new ArrayList<String>();
        ArrayList<String> prep_pat= new ArrayList<String>();
        ArrayList<String> prep_ext= new ArrayList<String>();


        String prep_loc1[]={"in","on","at","above","across","against","along","among","around","behind","below","beneath","beside","between","from","inside","near","toward","under","within"};
        String prep_pat1[]={"from","to","through","via","by"};
        String prep_ext1[]={"by","to"};
        int i_ind=0;
        for(i_ind=0;i_ind<prep_loc1.length;i_ind++){
            prep_loc.add(prep_loc1[i_ind]);
        }


        for(i_ind=0;i_ind<prep_pat1.length;i_ind++){
            prep_pat.add(prep_pat1[i_ind]);
        }

        for(i_ind=0;i_ind<prep_ext1.length;i_ind++){
            prep_ext.add(prep_ext1[i_ind]);
        }

     //   System.out.print(prep_loc);
       // System.out.print(prep_pat);
        //System.out.print(prep_ext);


        String folder = "";
        int i;
        for(i=0;i<args.length;i++){
            folder=folder+args[i];
        }

        //String classes[]={"appear-48.1.1","assuming_position-50","calibratable_cos-45.6","escape-51.1","spatial_configuration-47.6"};
        String classes[]={"test7"};



        // System.out.print(folder);
        get_word_feature o = new get_word_feature();
        Lemmatizer l = new Lemmatizer();
        // System.out.print(l.lemmatize("Hello running people"));
        String TAGGER_MODEL = "/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger";

        for(i=0;i<classes.length;i++){
            //   String path2=folder +"aa";
            BufferedWriter featWriter = new BufferedWriter(new FileWriter(folder+classes[i]+"-features_test7"));
            boolean tokenize = false;
            File text = new File(folder+classes[i]);
            MaxentTagger tagger = new MaxentTagger(TAGGER_MODEL);
            Scanner stdin = new Scanner(text);
            while (stdin.hasNextLine()) {

                String line2 = stdin.nextLine();
                if (line2.startsWith("####")){
                    System.out.print(line2+"\n");
                    featWriter.write(line2+"\n");
                }

                else{
                    String line = l.lemmatize(line2);

                    //System.out.print(line+"###");
                    // System.out.print(line2+"%%%");

                    // String postagged=posTag.tagString(line);        //write posfile
                    //  posWriter.write(postagged);
                    //  posWriter.newLine();


                    List<HasWord> tokens = new ArrayList();
                    if (tokenize) {
                        PTBTokenizer<Word> tokenizer = new PTBTokenizer(
                                new StringReader(line), new WordTokenFactory(), "");
                        for (Word label; tokenizer.hasNext(); ) {
                            tokens.add(tokenizer.next());
                        }
                    } else {
                        for (String word : line.split(" ")) {
                            tokens.add(new Word(word));
                        }
                    }


                    // System.out.print(l.lemmatize(valStr));

                    List<TaggedWord> tagged = tagger.tagSentence(tokens);
                    int count = 0;
                    for (TaggedWord word : tagged) {
                        if (count != 0) {
                            System.out.print(word.toString() + " ");
                            if (word.toString().split("/").length == 2)
                                // System.out.print("SSSS");
                                //  System.out.print("#"+word.toString().split("/")[0].split("-")[0]+"#");
                                if (word.toString().split("/")[0].split("-")[0].equals("pr")) {
                                    System.out.print(word.toString().split("/")[0].split("-")[1]);
                                    int done=0;
                                    String res_pre="[";
                                    if (prep_loc.contains(word.toString().split("/")[0].split("-")[1])) {
                                        res_pre=res_pre+"1, ";
                                        done = 1;
                                    }
                                    else{
                                        res_pre=res_pre+"0, ";
                                        done = 1;
                                    }

                                    if (prep_pat.contains(word.toString().split("/")[0].split("-")[1])) {
                                        res_pre=res_pre+"1, ";
                                        done = 1;
                                    }
                                    else{
                                        res_pre=res_pre+"0, ";
                                        done = 1;
                                    }

                                    if (prep_ext.contains(word.toString().split("/")[0].split("-")[1])) {
                                        res_pre=res_pre+"1]";
                                        done = 1;
                                    }
                                    else{
                                        res_pre=res_pre+"0]";
                                        done = 1;
                                    }

                                    System.out.print(res_pre);
                                    featWriter.write(res_pre);
                                           }
                                else {
                                    //}
                                    System.out.print(o.run(word.toString().split("/")[0], word.toString().split("/")[1]).toString().replace("true", "1").replace("false", "0") + " ");

                                    featWriter.write(o.run(word.toString().split("/")[0], word.toString().split("/")[1]).toString().replace("true", "1").replace("false", "0") + " ");
                                }
                        }
                        count = count + 1;
                        System.out.print("\n");

                    }//System.out.print("\n");
                    featWriter.newLine();

                }}
            featWriter.close();
        }

        //String coms[]={"appear.py",  "assume.py",  "calibrate.py",  "escape.py",  "spatial.py"};
        String coms[]={};
//      String command = "python /home/prajpoot/Desktop/VERBNET_DATA/Input/test.py";

        int k;
        for(k=0;k<coms.length;k++){
            String command = "python" +" "+folder+coms[k]+" "+folder;
            System.out.println(command);
            try {
                Process process = Runtime.getRuntime().exec(command);

                // exhaust input stream
                BufferedInputStream in = new BufferedInputStream(process.getInputStream());
                byte[] bytes = new byte[4096];
                while (in.read(bytes) != -1) {}

                // wait for completion
                try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }
}
