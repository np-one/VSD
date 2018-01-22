import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by prajpoot on 17/3/17.
 */
public class Verboceanparse {

    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/home/prajpoot/Downloads/verbOP/vop_entails.pl"));
        String line = br.readLine();
        while (line!=null){
            line = br.readLine();
            String verb1 = line.replace("entails(","").replace(")","").split(",")[0].trim();
            String verb2 = line.replace("entails(","").replace(")","").split(",")[1].trim();

        }

    }
}
