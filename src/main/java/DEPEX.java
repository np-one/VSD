/**
 * Created by prajpoot on 23/5/16.
 */
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.WordTokenFactory;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.StringUtils;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class DEPEX {

    public static final String TAGGER_MODEL = "/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger";
    public static final String PARSER_MODEL = "/home/prajpoot/Downloads/stanford-english-corenlp-2016-01-10-models/edu/stanford/nlp/models/parser/nndep/english_UD.gz";

    public static void main(String[] args) throws Exception {
        Properties props = StringUtils.argsToProperties(args);
        if (!props.containsKey("tokpath") ||
                !props.containsKey("parentpath") ||
                !props.containsKey("relpath")) {
            System.err.println(
                    "usage: java DependencyParse -tokenize - -tokpath <tokpath> -parentpath <parentpath> -relpath <relpath>");
            System.exit(1);
        }

        boolean tokenize = false;
        if (props.containsKey("tokenize")) {
            tokenize = true;
        }

        String tokPath = props.getProperty("tokpath");
        String parentPath = props.getProperty("parentpath");
        String relPath = props.getProperty("relpath");

        BufferedWriter tokWriter = new BufferedWriter(new FileWriter("/home/prajpoot/Desktop/test/WSJ_TESTS/TestTags/SE/toks"));
        BufferedWriter parentWriter = new BufferedWriter(new FileWriter("/home/prajpoot/Desktop/test/WSJ_TESTS/TestTags/SE/parent"));
        BufferedWriter relWriter = new BufferedWriter(new FileWriter("/home/prajpoot/Desktop/test/WSJ_TESTS/TestTags/SE/rel"));
        BufferedWriter depWriter = new BufferedWriter(new FileWriter("/home/prajpoot/Desktop/test/WSJ_TESTS/TestTags/SE/deps"));
        BufferedWriter posWriter = new BufferedWriter(new FileWriter("/home/prajpoot/Desktop/test/WSJ_TESTS/TestTags/SE/pos"));
        BufferedWriter lemWriter = new BufferedWriter(new FileWriter("/home/prajpoot/Desktop/test/WSJ_TESTS/TestTags/SE/lem"));
        File text=new File("/home/prajpoot/Desktop/test/WSJ_TESTS/TestTags/SE/test_SEXPRE");
        MaxentTagger tagger = new MaxentTagger(TAGGER_MODEL);
        DependencyParser parser = DependencyParser.loadFromModelFile(PARSER_MODEL);
        MaxentTagger posTag = new MaxentTagger("/home/prajpoot/Downloads/stanford-english-corenlp-2016-01-10-models/edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
        Scanner stdin = new Scanner(text);
        int count = 0;
        long start = System.currentTimeMillis();
        Lemmatizer l = new Lemmatizer();
        while (stdin.hasNextLine()) {
            String line = stdin.nextLine();
            if(line.contains("#####") | line.contains("$$$$$")){
                tokWriter.write(line);
                parentWriter.write(line);
                relWriter.write(line);
                depWriter.write(line);
                posWriter.write(line);
                lemWriter.write(line);

                tokWriter.newLine();
                parentWriter.newLine();
                relWriter.newLine();
                depWriter.newLine();
                posWriter.newLine();
                lemWriter.newLine();
                continue;
            }

            //String line = l.lemmatize(line2);
           // String postagged=posTag.tagString(line);        //write posfile
          //  posWriter.write(postagged);
          //  posWriter.newLine();
            List<HasWord> tokens = new ArrayList();

                PTBTokenizer<Word> tokenizer = new PTBTokenizer(
                        new StringReader(line), new WordTokenFactory(), "");
                for (Word label; tokenizer.hasNext(); ) {
                    tokens.add(tokenizer.next());
                }


            List<TaggedWord> tagged = tagger.tagSentence(tokens);
            String line1 = l.lemmatize(line);
            lemWriter.write(line1);
            lemWriter.newLine();


            int len = tagged.size();
            Collection<TypedDependency> tdl = parser.predict(tagged).typedDependencies();
            depWriter.write(tdl.toString());
            depWriter.newLine();


            List<TaggedWord> tdl2 = posTag.apply(tagged);
            posWriter.write(tdl2.toString());
            posWriter.newLine();


            int[] parents = new int[len];
            for (int i = 0; i < len; i++) {
                // if a node has a parent of -1 at the end of parsing, then the node
                // has no parent.
                parents[i] = -1;
            }

            String[] relns = new String[len];
            for (TypedDependency td : tdl) {
                // let root have index 0
                int child = td.dep().index();
                int parent = td.gov().index();
                relns[child - 1] = td.reln().toString();
                parents[child - 1] = parent;
            }

            // print tokens
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len - 1; i++) {
                if (tokenize) {
                    sb.append(PTBTokenizer.ptbToken2Text(tokens.get(i).word()));
                } else {
                    sb.append(tokens.get(i).word());
                }
                sb.append(' ');
            }
            if (tokenize) {
                sb.append(PTBTokenizer.ptbToken2Text(tokens.get(len - 1).word()));
            } else {
                sb.append(tokens.get(len - 1).word());
            }
            sb.append('\n');
            tokWriter.write(sb.toString());

            // print parent pointers
            sb = new StringBuilder();
            for (int i = 0; i < len - 1; i++) {
                sb.append(parents[i]);
                sb.append(' ');
            }
            sb.append(parents[len - 1]);
            sb.append('\n');
            parentWriter.write(sb.toString());

            // print relations
            sb = new StringBuilder();
            for (int i = 0; i < len - 1; i++) {
                sb.append(relns[i]);
                sb.append(' ');
            }
            sb.append(relns[len - 1]);
            sb.append('\n');
            relWriter.write(sb.toString());

            count++;
            if (count % 1000 == 0) {
                double elapsed = (System.currentTimeMillis() - start) / 1000.0;
                System.err.printf("Parsed %d lines (%.2fs)\n", count, elapsed);
            }
        }

        long totalTimeMillis = System.currentTimeMillis() - start;
        System.err.printf("Done: %d lines in %.2fs (%.1fms per line)\n",
                count, totalTimeMillis / 1000.0, totalTimeMillis / (double) count);
        tokWriter.close();
        parentWriter.close();
        relWriter.close();
        depWriter.close();
        posWriter.close();
        lemWriter.close();
    }
}