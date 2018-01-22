import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by prajpoot on 21/4/17.
 */
public class prepare_KSRL_data2 {
    public static void main(String args[]) throws IOException {
        Scanner stdin = new Scanner(new File("/home/prajpoot/Desktop/KsRL/train+test/tags"));
        Lemmatizer l = new Lemmatizer();
        BufferedWriter bw = new BufferedWriter(new FileWriter("/home/prajpoot/Desktop/KsRL/train+test"+"/tags2"));
        String line="";
        while(stdin.hasNextLine()){
            String res="";
            line=stdin.nextLine();
            String subline[]=line.split(" ");
            for(int i=0;i<subline.length;i++){
                if(i==0)
                    continue;
                else
                if(subline[i].split(":").length==2)
//                    if(subline[i].split(":")[1].split("-").length==2){
                {
                    System.out.println(subline[i]+" "+line);
                    if (subline[i].split(":")[1].split("-").length>0)
                        if(!subline[i].contains("frame:"))
                            res=res+" "+subline[i].split(":")[0].trim()+":"+l.lemmatize(subline[i].split(":")[1].split("-")[0]).trim().replace(" ","");
                        else
                            res=res+" "+subline[i].split(":")[0].trim()+":"+(subline[i].split(":")[1].split("-")[0]).trim().replace(" ","");


                }

                if(subline[i].split(":").length==3)
//                    if(subline[i].split(":")[1].split("-").length==2){
                {
                    System.out.println(subline[i]+" "+line);
                    if (subline[i].split(":")[2].split("-").length>0)
                        res=res+" "+subline[i].split(":")[1].trim()+":"+(subline[i].split(":")[2].split("-")[0]).trim().replace(" ","");


                }
//                    else{
//                        res=res+" "+subline[i];
//                    }

            }
            bw.write(subline[0]+" "+res);
            bw.newLine();

        }
        bw.close();
    }
}
