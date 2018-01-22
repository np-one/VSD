/**
 * Created by prajpoot on 1/3/17.
 */
import java.io.*;

public class parseHTML {

    public static void main (String[] args) throws Exception{
        StringBuilder sb;
        String dir="/home/prajpoot/vn_group/verbs.colorado.edu/html_groupings";
        File folder = new File(dir+"/");
        File[] listOfFiles = folder.listFiles();
        for (int k = 0; k < listOfFiles.length; k++) {
            String filename=listOfFiles[k].getName();
            sb= new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(dir+"/"+filename));
            BufferedWriter bw = new BufferedWriter(new FileWriter(dir+"_txt/"+filename.replace("html","txt")));
            String line;
            while ( (line=br.readLine()) != null) {
                sb.append(line);
                // or
                //  sb.append(line).append(System.getProperty("line.separator"));
            }
            String nohtml = sb.toString().replaceAll("\\<.*?>","\n");
            //System.out.println(nohtml.trim());
            bw.write(nohtml.trim().replace("\n\n","\n").replace("\n\n","\n").replace("\n\n","\n").replace("\nSense ","\n\nSense "));
            bw.close();
        }


    }


}