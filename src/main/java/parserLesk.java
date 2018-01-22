import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.HashSet;



/**
 * Created by prajpoot on 15/6/16.
 */
public class parserLesk {

    public static HashSet<String> ngrams(int n, String str) {
        HashSet<String> ngrams = new HashSet<>();
        String[] words = str.split(" ");
        for (int i = 0; i < words.length - n + 1; i++)
            ngrams.add(concat(words, i, i+n));
        return ngrams;
    }

    public static String concat(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append((i > start ? " " : "") + words[i]);
        return sb.toString();
    }


    public static void main(String args[]) throws IOException, ParseException {
         HashSet<String> set1 =ngrams(2,"John killed Mary.");
         HashSet<String> set2 =ngrams(2,"Peter also killed Mary.");
         set1.retainAll(set2);
         System.out.println(set1);
    }


}


