import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by prajpoot on 16/6/16.
 */
public class test_Lucene {
    public static String trim(String val){
        String answer="";
        char last = 0;
        for(int i=0;i<val.length();i++){
            //System.out.println("val i: "+val.charAt(i)+" last: "+last);
            if(last=='+' && val.charAt(i)=='+')
            {//System.out.println("if");
                continue;}
            else
            { //System.out.println("else");
                // System.out.println(String.valueOf(val.charAt(i)).toString());
                answer=answer+String.valueOf(val.charAt(i));
                //System.out.println("answer: "+answer);
                last=val.charAt(i);
                //System.out.println("answer: "+answer);
            }

        }
        if(answer.startsWith("+"))
            answer=answer.substring(1);
        if(answer.endsWith("+"))
            return answer.substring(0,answer.length()-1);
        else
            return answer;
    }


    public static int getQuerycount(IndexSearcher searcher, String quer, QueryParser parser) throws IOException, ParseException {

        if(quer.contains(": n AND") || quer.contains(": n") )
            return 0;

        if(quer.contains("nsubj: ORG")||quer.contains("nsubj: PER")||quer.contains("nsubj: LOC"))
        {quer=quer.replace("nsubj: ","nsubjNER: ");
            //System.out.println("##### "+quer);
        }
        if(quer.contains("dobj: ORG")||quer.contains("dobj: PER")||quer.contains("dobj: LOC"))
            quer=quer.replace("dobj","dobjNER");
        if(quer.contains("pobj: ORG")||quer.contains("pobj: PER")||quer.contains("pobj: LOC"))
            quer=quer.replace("pobj","pobjNER");
        if(quer.contains("pobj1: ORG")||quer.contains("pobj1: PER")||quer.contains("pobj1: LOC"))
            quer=quer.replace("pobj1","pobj1NER");
        if(quer.contains("pobj2: ORG")||quer.contains("pobj2: PER")||quer.contains("pobj2: LOC"))
            quer=quer.replace("pobj2","pobj2NER");
        if(quer.contains("iobj: ORG")||quer.contains("iobj: PER")||quer.contains("iobj: LOC"))
            quer=quer.replace("iobj","iobjNER");
        // System.out.println("AAAA "+quer);
        //quer=quer.replace("AND nsubj: null","").replace("AND dobj: null","").replace("AND prep: null","").replace("AND pobj: null","").replace("AND prep1: null","").replace("AND pobj1: null","").replace("AND prep2: null","").replace("AND pobj2: null","");
        // System.out.println(quer);
        quer=quer.replace("Verbname","verb");
        Query query = parser.parse(quer);

        int count=0;
        int hits= searcher.search(query,1000).totalHits;
        if(hits==0)
            return 0;
        ScoreDoc[] t = (searcher.search(query, hits)).scoreDocs;
        //  System.out.println(t.length+" "+hits);
        for (int i = 0; i < hits; i++){
            //count= count+Integer.parseInt(searcher.doc(t[i].doc).getField("count").stringValue());
            count= count+1;
        }

        return count;
    }
    public static int getQuerycount_GOOGLE(IndexSearcher searcher, String quer, QueryParser parser) throws IOException, ParseException {

        if(quer.contains(": n AND") || quer.contains(": n") )
            return 0;

        if(quer.contains("nsubj: ORG")||quer.contains("nsubj: PER")||quer.contains("nsubj: LOC"))
        {quer=quer.replace("nsubj: ","nsubjNER: ");
            //System.out.println("##### "+quer);
        }
        if(quer.contains("dobj: ORG")||quer.contains("dobj: PER")||quer.contains("dobj: LOC"))
            quer=quer.replace("dobj","dobjNER");
        if(quer.contains("pobj: ORG")||quer.contains("pobj: PER")||quer.contains("pobj: LOC"))
            quer=quer.replace("pobj","pobjNER");
        if(quer.contains("pobj1: ORG")||quer.contains("pobj1: PER")||quer.contains("pobj1: LOC"))
            quer=quer.replace("pobj1","pobj1NER");
        if(quer.contains("pobj2: ORG")||quer.contains("pobj2: PER")||quer.contains("pobj2: LOC"))
            quer=quer.replace("pobj2","pobj2NER");
        if(quer.contains("iobj: ORG")||quer.contains("iobj: PER")||quer.contains("iobj: LOC"))
            quer=quer.replace("iobj","iobjNER");
        // System.out.println("AAAA "+quer);
        //quer=quer.replace("AND nsubj: null","").replace("AND dobj: null","").replace("AND prep: null","").replace("AND pobj: null","").replace("AND prep1: null","").replace("AND pobj1: null","").replace("AND prep2: null","").replace("AND pobj2: null","");
        // System.out.println(quer);
//        quer=quer.replace("Verbname","verb");
        Query query = parser.parse(quer);

        int count=0;
        int hits= searcher.search(query,1000).totalHits;
        if(hits==0)
            return 0;
        ScoreDoc[] t = (searcher.search(query, hits)).scoreDocs;
        //  System.out.println(t.length+" "+hits);
        for (int i = 0; i < hits; i++){
            count= count+Integer.parseInt(searcher.doc(t[i].doc).getField("count").stringValue());
//            count= count+1;
        }

        return count;
    }




    public static double computeWeightedScore(LinkedHashMap<Integer,String> weights, LinkedHashMap<String,Integer> values){

        double val= 0;
        double temp=0.0;
        double weightArray[] = {0.02,0.01,0.01,0.02,0.03,0.29,0.0,0.0,0.01,0.01,0.04,0.09,0.0,0.0,0.02,0.01,0.02,0.03,0.0,0.0,0.0,0.29,0.05,0.0,0.0,0.0,0.0,0.00,0.00,0.01,0.01};
//        double weightArray[] = {1,1,1,1,1,1,0.0,0.0,1,1,1,1,0.0,0.0,1,1,1,1,0.0,0.0,0.0,1,1, 0.0,0.0,0.0,0.0,1,1,1,1};

        //return 1;
        for (Integer key : weights.keySet()) {
            if(key==1)
                val=val+weightArray[0]*values.get(weights.get(key));

            if(key==2)
                val=val+weightArray[1]*values.get(weights.get(key));
          if(key==3)
                val=val+weightArray[2]*val+values.get(weights.get(key));
            if(key==4)
                val=val+weightArray[3]*values.get(weights.get(key));
            if(key==5)
                val=val+weightArray[4]*values.get(weights.get(key));
            if(key==6)
                val=val+weightArray[5]*values.get(weights.get(key));
            if(key==7)
                val=val+weightArray[6]*values.get(weights.get(key));
            if(key==8)
                val=val+weightArray[7]*values.get(weights.get(key));
            if(key==9)
                val=val+weightArray[8]*values.get(weights.get(key));
            if(key==10)
                val=val+weightArray[9]*values.get(weights.get(key));
            if(key==11)
                val=val+weightArray[10]*values.get(weights.get(key));
            if(key==12)
                val=val+weightArray[11]*values.get(weights.get(key));
            if(key==13)
                val=val+weightArray[12]*values.get(weights.get(key));
            if(key==14)
                val=val+weightArray[13]*values.get(weights.get(key));
            if(key==15)
                val=val+weightArray[14]*values.get(weights.get(key));
            if(key==16)
                val=val+weightArray[15]*values.get(weights.get(key));
            if(key==17)
                val=val+weightArray[16]*values.get(weights.get(key));
            if(key==18)
                val=val+weightArray[17]*values.get(weights.get(key));
            if(key==19)
                val=val+weightArray[18]*values.get(weights.get(key));
            if(key==20)
                val=val+weightArray[19]*values.get(weights.get(key));
            if(key==21)
                val=val+weightArray[20]*values.get(weights.get(key));
            if(key==22)
                val=val+weightArray[21]*values.get(weights.get(key));
            if(key==23)
                val=val+weightArray[22]*values.get(weights.get(key));
            if(key==24)
                val=val+weightArray[23]*values.get(weights.get(key));
            if(key==25)
                val=val+weightArray[24]*values.get(weights.get(key));
            if(key==26)
                val=val+weightArray[25]*values.get(weights.get(key));
            if(key==27)
                val=val+weightArray[26]*values.get(weights.get(key));
            if(key==28)
                val=val+weightArray[27]*values.get(weights.get(key));
            if(key==29)
                val=val+weightArray[28]*values.get(weights.get(key));
            if(key==30)
                val=val+weightArray[29]*values.get(weights.get(key));
            if(key==31)
                val=val+weightArray[30]*values.get(weights.get(key));

        }
        return val;

    }

    public static void run(String path) throws IOException, ParseException {


        IndexSearcher searcher = null;
        IndexSearcher searcher2 = null;
        QueryParser parser = null;
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/home/prajpoot/lucenedict/dictionary1/").toPath())));
        searcher2 = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/home/prajpoot/lucenedict/index-directory32/").toPath())));
        parser = new QueryParser("content", new WhitespaceAnalyzer());
       // $$$$$attach null null null null null null to null null filing null null @@@@@1 *****attach
        BufferedReader br = new BufferedReader(new FileReader(path+"/final-input-frames"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(path+"/result1_A3_OLD"));
        String sCurrentLine = "";
        LinkedHashMap<String,Integer> levelScore = new LinkedHashMap<String, Integer>();
        LinkedHashMap<Integer,String> levelWeights = new LinkedHashMap<Integer, String>();
        levelWeights.put(1,"nsubj+verb");
        levelWeights.put(2,"dobj+verb");
        levelWeights.put(3,"verb+prep");//new_1
        levelWeights.put(4, "verb+prep+pobj");
        levelWeights.put(5, "verb+prep+pobj+prep1");//new_2
        levelWeights.put(6, "verb+prep+pobj+prep1+pobj1");
        levelWeights.put(7, "verb+prep+pobj+prep1+pobj1+prep2"); //new_3
        levelWeights.put(8, "verb+prep+pobj+prep1+pobj1+prep2+pobj2");
        levelWeights.put(9, "verb+dobj+prep"); //new_4
        levelWeights.put(10, "verb+dobj+prep+pobj");
        levelWeights.put(11, "verb+dobj+prep+pobj+prep1");  //new_5
        levelWeights.put(12, "verb+dobj+prep+pobj+prep1+pobj1");
        levelWeights.put(13, "verb+dobj+prep+pobj+prep1+pobj1+prep2"); //new_6
        levelWeights.put(14, "verb+dobj+prep+pobj+prep1+pobj1+prep2+pobj2");
        levelWeights.put(15, "nsubj+verb+dobj");
        levelWeights.put(16, "nsubj+verb+prep");    //new_7
        levelWeights.put(17, "nsubj+verb+prep+pobj");
        levelWeights.put(18, "nsubj+verb+prep+pobj+prep1"); //new_8
        levelWeights.put(19, "nsubj+verb+prep+pobj+prep1+pobj1");
        levelWeights.put(20, "nsubj+verb+prep+pobj+prep1+pobj1+prep2"); //new_9
        levelWeights.put(21, "nsubj+verb+prep+pobj+prep1+pobj1+prep2+pobj2");
        levelWeights.put(22, "nsubj+verb+dobj+prep"); //new_10
        levelWeights.put(23, "nsubj+verb+dobj+prep+pobj");
        levelWeights.put(24, "nsubj+verb+dobj+prep+pobj+prep1"); //new_11
        levelWeights.put(25, "nsubj+verb+dobj+prep+pobj+prep1+pobj1");
        levelWeights.put(26, "nsubj+verb+dobj+prep+pobj+prep1+pobj1+prep2");    //new_12
        levelWeights.put(27, "nsubj+verb+dobj+prep+pobj+prep1+pobj1+prep2+pobj2");
        levelWeights.put(28,"verb+xcomp");                                  //new_13
        levelWeights.put(29,"verb+ccomp");                                  //new_!4
        levelWeights.put(30,"verb+advmod");                                 //new_!5
        levelWeights.put(31,"verb+iobj");                                   //new_16

        ArrayList<String> pronouns = new ArrayList<String>();
        pronouns.add("i");
        pronouns.add("you");
        pronouns.add("he");
        pronouns.add("she");
        pronouns.add("it");
        pronouns.add("they");
        pronouns.add("we");
        pronouns.add("me");
        pronouns.add("you");
        pronouns.add("him");
        pronouns.add("her");
        pronouns.add("us");
        pronouns.add("them");
        pronouns.add("they");

        while ((sCurrentLine = br.readLine()) != null) {


            if(sCurrentLine.contains("$$$$$")){
                bw.write(sCurrentLine);
                System.out.println(sCurrentLine);
                bw.newLine();
                continue;
            }
            if(sCurrentLine.contains("#####")){
                bw.write(sCurrentLine);
                System.out.println(sCurrentLine);
                bw.newLine();
                continue;
            }
            // System.out.println(sCurrentLine);
            List<HasWord> tokens = new ArrayList();
            PTBTokenizer<Word> tokenizer = new PTBTokenizer(
                    new StringReader(sCurrentLine), new WordTokenFactory(), "");
            for (Word label; tokenizer.hasNext(); ) {
                tokens.add(tokenizer.next());
            }
            String Verbname="";
            String nsubj="";
            String dobj="";
            String prep="";
            String pobj="";
            String prep1="";
            String pobj1="";
            String prep2="";
            String pobj2="";
            String xcomp="";
            String ccomp="";
            String advmod="";
            String iobj="";

            if(tokens.size()!=13){
                // System.out.println(tokens.size()+" "+sCurrentLine);
                sCurrentLine.concat(" null");
                pobj2="null";
            }


            for(int i=0;i<tokens.size();i++){
                if(i==0)
                   // if(!tokens.get(i).toString().contains("null"))
                        Verbname=tokens.get(i).toString().split("/")[0].toLowerCase();
                if(i==1)
                   // if(!tokens.get(i).toString().contains("null"))
                    if(tokens.get(i).toString().contains("/PERSON")||tokens.get(i).toString().contains("/ORGANIZATION")||tokens.get(i).toString().contains("/LOCATION"))
                        nsubj=tokens.get(i).toString().split("/")[1].trim();
                    else
                        if(pronouns.contains(tokens.get(i).toString()))
                            nsubj="PERSON";
                        else
                            nsubj=tokens.get(i).toString().toLowerCase();
                if(i==2)
                  //  if(!tokens.get(i).toString().contains("null"))
                    if(tokens.get(i).toString().contains("/")){
                        if(tokens.get(i).toString().split("/").length==0)
                            continue;
                        System.out.println(tokens.get(i).toString()+" "+nsubj+" "+Verbname);
                        dobj=tokens.get(i).toString().split("/")[1].trim();}
                    else
                      if(pronouns.contains(tokens.get(i).toString()))
                        dobj="PERSON";
                      else
                        dobj=tokens.get(i).toString().toLowerCase();

                if(i==3)
                  //  if(!tokens.get(i).toString().contains("null"))
                        //xcomp=tokens.get(i).toString().split("/")[0];
                    if(tokens.get(i).toString().contains("/PERSON")||tokens.get(i).toString().contains("/ORGANIZATION")||tokens.get(i).toString().contains("/LOCATION"))
                        xcomp=tokens.get(i).toString().split("/")[1].trim();
                    else
                        if(pronouns.contains(tokens.get(i).toString()))
                         xcomp="PERSON";
                        else
                         xcomp=tokens.get(i).toString().toLowerCase();
                if(i==4)
                   // if(!tokens.get(i).toString().contains("null"))
                        //ccomp=tokens.get(i).toString().split("/")[0];
                    if(tokens.get(i).toString().contains("/PERSON")||tokens.get(i).toString().contains("/ORGANIZATION")||tokens.get(i).toString().contains("/LOCATION"))
                        ccomp=tokens.get(i).toString().split("/")[1].trim();
                    else
                     if(pronouns.contains(tokens.get(i).toString()))
                        ccomp="PERSON";
                     else
                        ccomp=tokens.get(i).toString().toLowerCase();
                if(i==5)
                  //  if(!tokens.get(i).toString().contains("null"))
                   //     iobj=tokens.get(i).toString().split("/")[0];
                    if(tokens.get(i).toString().contains("/PERSON")||tokens.get(i).toString().contains("/ORGANIZATION")||tokens.get(i).toString().contains("/LOCATION"))
                        iobj=tokens.get(i).toString().split("/")[1].trim();
                    else
                      if(pronouns.contains(tokens.get(i).toString()))
                        iobj="PERSON";
                      else
                        iobj=tokens.get(i).toString().toLowerCase();
                if(i==6)
                   // if(!tokens.get(i).toString().contains("null"))
                        //advmod=tokens.get(i).toString().split("/")[0];
                    if(tokens.get(i).toString().contains("/PERSON")||tokens.get(i).toString().contains("/ORGANIZATION")||tokens.get(i).toString().contains("/LOCATION"))
                        advmod=tokens.get(i).toString().split("/")[1].trim();
                    else
                     if(pronouns.contains(tokens.get(i).toString()))
                        advmod="PERSON";
                     else
                        advmod=tokens.get(i).toString().toLowerCase();
                if(i==7)
                   // if(!tokens.get(i).toString().contains("null"))
                     // prep=tokens.get(i).toString().split("/")[0];
                    if(tokens.get(i).toString().contains("/PERSON")||tokens.get(i).toString().contains("/ORGANIZATION")||tokens.get(i).toString().contains("/LOCATION"))
                        prep=tokens.get(i).toString().split("/")[1].trim();
                    else
                        prep=tokens.get(i).toString().toLowerCase();
                if(i==8)
                   // if(!tokens.get(i).toString().contains("null"))
                    //    prep1=tokens.get(i).toString().split("/")[0];
                    if(tokens.get(i).toString().contains("/PERSON")||tokens.get(i).toString().contains("/ORGANIZATION")||tokens.get(i).toString().contains("/LOCATION"))
                        prep1=tokens.get(i).toString().split("/")[1].trim();
                    else
                        prep1=tokens.get(i).toString().toLowerCase();
                if(i==9)
                    // if(!tokens.get(i).toString().contains("null"))
                    //prep2=tokens.get(i).toString().split("/")[0];
                    if(tokens.get(i).toString().contains("/PERSON")||tokens.get(i).toString().contains("/ORGANIZATION")||tokens.get(i).toString().contains("/LOCATION"))
                        prep2=tokens.get(i).toString().split("/")[1].trim();
                    else
                        prep2=tokens.get(i).toString().toLowerCase();
                if(i==10)
                    // if(!tokens.get(i).toString().contains("null"))
                    //pobj=tokens.get(i).toString().split("/")[0];
                    if(tokens.get(i).toString().contains("/PERSON")||tokens.get(i).toString().contains("/ORGANIZATION")||tokens.get(i).toString().contains("/LOCATION"))
                        pobj=tokens.get(i).toString().split("/")[1].trim();
                    else
                     if(pronouns.contains(tokens.get(i).toString()))
                        pobj="PERSON";
                     else
                        pobj=tokens.get(i).toString().toLowerCase();
                if(i==11)
                    // if(!tokens.get(i).toString().contains("null"))
                    //pobj1=tokens.get(i).toString().split("/")[0];
                    if(tokens.get(i).toString().contains("/PERSON")||tokens.get(i).toString().contains("/ORGANIZATION")||tokens.get(i).toString().contains("/LOCATION"))
                        pobj1=tokens.get(i).toString().split("/")[1].trim();
                    else
                     if(pronouns.contains(tokens.get(i).toString()))
                        pobj1="PERSON";
                     else
                        pobj1=tokens.get(i).toString().toLowerCase();
                if(i==12)
                    // if(!tokens.get(i).toString().contains("null"))
                    //pobj2=tokens.get(i).toString().split("/")[0];
                    if(tokens.get(i).toString().contains("/PERSON")||tokens.get(i).toString().contains("/ORGANIZATION")||tokens.get(i).toString().contains("/LOCATION"))
                        pobj2=tokens.get(i).toString().split("/")[1].trim();
                    else
                     if(pronouns.contains(tokens.get(i).toString()))
                        pobj2="PERSON";
                     else
                        pobj2=tokens.get(i).toString().toLowerCase();

            }

            for (Integer key : levelWeights.keySet()) {

                String query="";
                int score=0;
                if(key==1){
                    query="nsubj: "+nsubj+" AND "+"Verbname: "+Verbname;
                    score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==2){
                    query="dobj: "+dobj+" AND "+"Verbname: "+Verbname;
                    score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==3){
                    query="prep: "+prep+" AND "+"Verbname: "+Verbname;
                    score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==4){
                    query="prep: "+prep+" AND "+"Verbname: "+Verbname+" AND "+"pobj: "+pobj;
                    score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==5){
                    query="prep: "+prep+" AND "+"Verbname: "+Verbname+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==6){
                    query="prep: "+prep+" AND "+"Verbname: "+Verbname+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==7){
                    query="prep: "+prep+" AND "+"Verbname: "+Verbname+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==8){
                    query="prep: "+prep+" AND "+"Verbname: "+Verbname+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2+" AND "+"pobj2: "+pobj2;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==9){
                    query="Verbname: "+Verbname+" AND "+"dobj: "+dobj+" AND "+"prep: "+prep;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==10){
                    query="Verbname: "+Verbname+" AND "+"dobj: "+dobj+" AND "+"prep: "+prep+" AND "+"pobj: "+pobj;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==11){
                    query="Verbname: "+Verbname+" AND "+"dobj: "+dobj+" AND "+"prep: "+prep+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==12){
                    query="Verbname: "+Verbname+" AND "+"dobj: "+dobj+" AND "+"prep: "+prep+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }


                if(key==13){
                    query="Verbname: "+Verbname+" AND "+"dobj: "+dobj+" AND "+"prep: "+prep+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==14){
                    query="Verbname: "+Verbname+" AND "+"dobj: "+dobj+" AND "+"prep: "+prep+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2+" AND "+"pobj2: "+pobj2;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }
                if(key==15){
                    query="Verbname: "+Verbname+" AND "+"dobj: "+dobj+" AND "+"nsubj: "+nsubj;
                    score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==16){
                    query="Verbname: "+Verbname+" AND "+"prep: "+prep+" AND "+"nsubj: "+nsubj;
                    score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==17){
                    query="Verbname: "+Verbname+" AND "+"prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"pobj: "+pobj;
                    score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==18){
                    query="Verbname: "+Verbname+" AND "+"prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }
                if(key==19){
                    query="Verbname: "+Verbname+" AND "+"prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

                if(key==20){
                    query="Verbname: "+Verbname+" AND "+"prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2;;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }
                if(key==21){
                    query="Verbname: "+Verbname+" AND "+"prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2+" AND "+"pobj2: "+pobj2;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }
                if(key==22){
                    query="Verbname: "+Verbname+" AND "+"prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"dobj: "+dobj;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }
                if(key==23){
                    query="Verbname: "+Verbname+" AND "+"prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"dobj: "+dobj+" AND "+"pobj: "+pobj;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }
                if(key==24){
                    query="Verbname: "+Verbname+" AND "+"prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"dobj: "+dobj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }
                if(key==25){
                    query="Verbname: "+Verbname+" AND "+"prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"dobj: "+dobj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }
                if(key==26){
                    query="Verbname: "+Verbname+" AND "+"prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"dobj: "+dobj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2;
                    score=0;
                      score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }
                if(key==27){
                    query="Verbname: "+Verbname+" AND "+"prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"dobj: "+dobj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2+" AND "+"pobj2: "+pobj2;
                    score=0;
                    score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }
                if(key==28){
                    query="Verbname: "+Verbname+" AND "+"xcomp: "+xcomp;
                    score=0;
                    score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }
                if(key==29){
                    query="Verbname: "+Verbname+" AND "+"ccomp: "+ccomp;
                    score=0;
                    score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }
                if(key==30){
                    query="Verbname: "+Verbname+" AND "+"advmod: "+advmod;
                    score=0;
                    score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }
                if(key==31){
                    query="Verbname: "+Verbname+" AND "+"iobj: "+iobj;
                    score=0;
                    score=getQuerycount(searcher, query,parser);
                    score+=getQuerycount_GOOGLE(searcher2,query,parser);
                    levelScore.put(levelWeights.get(key),score);
                }

            }
            System.out.println(sCurrentLine);
//            System.out.println("nsubj: "+nsubj+" verb: "+Verbname+" dobj: "+dobj+" prep: "+prep+" pobj: "+pobj+" prep1: "+prep1+" pobj1: "+pobj1+" prep2: "+prep2+" pobj2: "+pobj2+" xcomp:"+xcomp+" ccomp:"+ccomp+" advmod:"+advmod+"iobj:"+iobj);
            System.out.println(levelScore);
//            System.out.println(computeWeightedScore(levelWeights,levelScore)+"\n\n");
            bw.write(String.valueOf(computeWeightedScore(levelWeights,levelScore)));
            bw.newLine();
            levelScore.clear();
            }
        bw.close();



            //double answer = computeWeightedScore(levelWeights,levelScore);

        }

    }




