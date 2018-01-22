import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by prajpoot on 27/6/16.
 */
public class Filter_one {

    public static void runFolder(String Folder) throws IOException {
        Scanner stdin= new Scanner(new File(Folder+"/final"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(Folder+"/final-input"));
        String line = "";


        Lemmatizer l =new Lemmatizer();
        while (stdin.hasNextLine()){
            String verb = "N";
            String nsubj = "N";
            String dobj = "N";
            String prep = "N";
            String pobj = "N";
            String prep1 = "N";
            String pobj1 = "N";
            String prep2 = "N";
            String pobj2 = "N";
            String xcomp = "N";
            String ccomp = "N";
            String advmod = "N";
            String iobj = "N";
            line=stdin.nextLine();
            verb=line.split("  ")[0].replace(" null","");
            if(verb.contains(":"))
                verb=line.split(" ")[0].replace(" null","");
            String toks[]=line.replace(verb,"").split(", ");
            HashMap<String,String> prepmap = new HashMap<String,String>();
            HashMap<String,String> pobjmap = new HashMap<String,String>();

            for(int i =0;i<toks.length;i++){
                if(toks[i].contains("nsubj"))
                {
                    if(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace(" ","").equals(""))
                        continue;
                    //System.out.println(line);
                    //System.out.println(toks[i]);
                    //System.out.println(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON")+"\n\n");
                    nsubj=l.lemmatize(toks[i].split(", ")[0].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON").trim().split(" ")[0].replace(" ","");
                }

                if(toks[i].contains("dobj"))
                {
                    if(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace(" ","").equals(""))
                        continue;
                    //System.out.println(line);
                    //System.out.println(toks[i]);
                  //  System.out.println(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON")+"\n\n");
                    dobj=l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON").trim().split(" ")[0].replace(" ","");
                }

                if(toks[i].contains("xcomp"))
                {

                    if(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace(" ","").equals(""))
                        continue;
                    //System.out.println(line);
                    //System.out.println(toks[i]);
                    //System.out.println(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON")+"\n\n");
                    xcomp=l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON").trim().split(" ")[0].replace(" ","");
                }


                if(toks[i].contains("ccomp"))
                {

                    if(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace(" ","").equals(""))
                        continue;
                    //System.out.println(line);
                    //System.out.println(toks[i]);
                    //System.out.println(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON")+"\n\n");
                    ccomp=l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON").trim().split(" ")[0].replace(" ","");
                }


                if(toks[i].contains("advmod"))
                {

                    if(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace(" ","").equals(""))
                        continue;
                    //System.out.println(line);
                    //System.out.println(toks[i]);
                   // System.out.println(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON")+"\n\n");
                    advmod=l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON").trim().split(" ")[0].replace(" ","");
                }


                if(toks[i].contains("iobj"))
                {

                    if(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace(" ","").equals(""))
                        continue;
                    //System.out.println(line);
                    //System.out.println(toks[i]);
                   // System.out.println(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON")+"\n\n");
                    iobj=l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON").trim().split(" ")[0].replace(" ","");
                }

                if(toks[i].contains("prep"))
                {

                    if(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace(" ","").equals(""))
                        continue;
                    //System.out.println(line);
                    //System.out.println(toks[i].split("-")[1]);
                    //System.out.println(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON")+"\n\n");
                    if(toks[i].split("-")[1]!="1"){
                        prepmap.put(toks[i].split("-")[1],l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON").trim().split(" ")[0].replace(" ",""));
                    }
                }
                if(toks[i].contains("pobj"))
                {

                    if(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace(" ","").equals(""))
                        continue;
                    //System.out.println(line);
                  //  System.out.println(toks[i].split("-")[1]);
                    //System.out.println(l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON")+"\n\n");
                    if(toks[i].split("-")[1]!="1"){
                        pobjmap.put(toks[i].split("-")[1],l.lemmatize(toks[i].split(": ")[1].split("-")[0]).replace("/organization","/ORGANIZATION").replace("/location","/LOCATION").replace("/person","/PERSON").trim().split(" ")[0].replace(" ",""));
                    }
                }
            }

            System.out.println(line);
            //System.out.println("Verb: "+verb+" nsubj: "+nsubj+" dobj: "+dobj+" xcomp: "+xcomp+" ccomp: "+ccomp+" iobj: "+iobj+" advmod: "+advmod);
    //        System.out.println("preps: "+prepmap);
      //      System.out.println("pobjs: "+pobjmap+"\n\n\n");

            String preps="";
            int count=0;
            for(HashMap.Entry<String,String> entry : prepmap.entrySet()) {
                if (entry.getValue().trim().equals("") || count>2)
                    continue;
                preps=preps+entry.getValue().split(" ")[0].trim()+" ";
                count=count+1;
            }

            if(count==0)
                preps=preps+"N N N";
            if(count==1)
                preps=preps+"N N";
            if(count==2)
                preps=preps+"N";

            count=0;

            String pobjs="";
            for(HashMap.Entry<String,String> entry : pobjmap.entrySet()) {
                if (entry.getValue().trim().equals("") || count>2)
                    continue;
                pobjs=pobjs+entry.getValue().split(" ")[0].trim()+" ";
                count=count+1;
            }


            if(count==0)
                pobjs=pobjs+"N N N";
            if(count==1)
                pobjs=pobjs+"N N";
            if(count==2)
                pobjs=pobjs+"N";


            System.out.println("0 Verb: "+verb+"1 nsubj: "+nsubj+"2 dobj: "+dobj+"3 xcomp: "+xcomp+"4 ccomp: "+ccomp+"5 iobj: "+iobj+"6 advmod: "+advmod+ "7 8 9 preps: "+preps+"10 11 12 pobjs: "+pobjs);
            System.out.println("preps: "+preps);
            System.out.println("pobjs: "+pobjs+"\n\n\n");
            bw.write(verb+" "+nsubj.split(" ")[0].trim()+" "+dobj.split(" ")[0].trim()+" "+xcomp.split(" ")[0].trim()+" "+ccomp.split(" ")[0].trim()+" "+iobj.split(" ")[0].trim()+" "+advmod.split(" ")[0].trim()+ " "+preps.trim()+" "+pobjs.trim());
            bw.newLine();


            prepmap.clear();
            pobjmap.clear();

        }
        bw.close();
    }


    public static void main(String args[]) throws IOException {
        runFolder("/home/prajpoot/Desktop/LM_Tests4_(VALID)");
    }


}
