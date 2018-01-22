import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import java.io.InputStream;

/**
 * Created by prajpoot on 24/10/16.
 */
public class jenaRead {
    public static void main(String args[]){
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = FileManager.get().open( "framenet.rdf" );
        if (in == null) {
            throw new IllegalArgumentException(
                    "File: " + "" + " not found");
        }

// read the RDF/XML file
        model.read(in, null);

// write it to standard out
        model.write(System.out);
    }
}
