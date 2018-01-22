import java.io.IOException;

/**
 * Created by prajpoot on 30/5/16.
 */
public class Prep {

    // get_word_feature o;
    public static void main(String args[]) throws IOException {
        String folder = "";
        int i;
        for(i=0;i<args.length;i++){
            folder=folder+args[i];
        }

        //String classes[]={"appear-48.1.1","assuming_position-50","calibratable_cos-45.6","escape-51.1","spatial_configuration-47.6"};
       String classes[]={"calibratable_cos-45.6"};



        // System.out.print(folder);
        get_word_feature o = new get_word_feature();
        Lemmatizer l = new Lemmatizer();
         System.out.print(l.lemmatize("Hello running people"));

}}
