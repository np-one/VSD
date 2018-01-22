/**
 * Created by prajpoot on 15/2/17.
 */

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by prajpoot on 16/6/16.
 */
public class test_LuceneP2 {
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


    public static HashMap<String,Integer> getQuerycount(IndexSearcher searcher, String quer, QueryParser parser) throws IOException, ParseException {

        if(quer.contains(": null"  ))
            return null;

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
        String quertemp ="";
        String term[] = quer.split(" ");
        for(int ter=0;ter<term.length;ter++){
            quertemp = quertemp+" "+term[ter].split("_")[0].split("/")[0];
        }

        quer=quertemp;

        Query query = parser.parse(quer);
        HashMap<String, Integer> str =new HashMap();
        //int count=0;
        int hits= searcher.search(query,50).totalHits;
        if(hits==0)
            return null;
        ScoreDoc[] t = (searcher.search(query, 5000)).scoreDocs;
        //  System.out.println(t.length+" "+hits);

        /*Wikipedia*/
        for (int i = 0; i < t.length; i++){
            //  System.out.println(i+"\t"+quer);
            if(searcher.doc(t[i].doc).getField("verb")!=null){

                if (str.containsKey(searcher.doc(t[i].doc).getField("verb").stringValue()))
                 str.put(searcher.doc(t[i].doc).getField("verb").stringValue(),str.get(searcher.doc(t[i].doc).getField("verb").stringValue())+ 1);
                else{
                 str.put(searcher.doc(t[i].doc).getField("verb").stringValue(),1);

                }
            }

            //count= count+1;
        }

        /*  Google N gram */
        /*for (int i = 0; i < t.length; i++){
          //  System.out.println(i+"\t"+quer);
            if (str.containsKey(searcher.doc(t[i].doc).getField("Verbname").stringValue()))
                str.put(searcher.doc(t[i].doc).getField("Verbname").stringValue(),str.get(searcher.doc(t[i].doc).getField("Verbname").stringValue())+ Integer.parseInt(searcher.doc(t[i].doc).getField("count").stringValue()));
            else{
                str.put(searcher.doc(t[i].doc).getField("Verbname").stringValue(),Integer.parseInt(searcher.doc(t[i].doc).getField("count").stringValue()));

            }

            //count= count+1;
        }*/

        return str;
    }




    public static void run(String path) throws IOException, ParseException {


        IndexSearcher searcher = null;
        QueryParser parser = null;
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/home/prajpoot/lucenedict/dictionary1/").toPath())));
        parser = new QueryParser("content", new StandardAnalyzer());
       // $$$$$attach null null null null null null to null null filing null null @@@@@1 *****attach
        BufferedReader br = new BufferedReader(new FileReader(path+"/final-input1"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(path+"result1_A3_OLD_q2000_All_query"));
        String sCurrentLine = "";
        LinkedHashMap<String,HashMap<String,Integer>> levelScore = new LinkedHashMap<String, HashMap<String,Integer>>();
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


            /*if(sCurrentLine.contains("$$$$$")){
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
            }*/
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
                        //System.out.println(tokens.get(i).toString()+" "+nsubj+" "+Verbname);
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
                HashMap<String,Integer> score= new HashMap<String, Integer>();
                if(key==1){
                    query="nsubj: "+nsubj;
                    score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==2){
                    query="dobj: "+dobj;
                    score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

                if(key==3){
                    query="prep: "+prep;
                    score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==4){
                    query="prep: "+prep+" AND "+"pobj: "+pobj;
                    score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

                if(key==5){
                    query="prep: "+prep+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

                if(key==6){
                    query="prep: "+prep+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

                if(key==7){
                    query="prep: "+prep+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

                if(key==8){
                    query="prep: "+prep+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2+" AND "+"pobj2: "+pobj2;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==9){
                    query="dobj: "+dobj+" AND "+"prep: "+prep;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

                if(key==10){
                    query="dobj: "+dobj+" AND "+"prep: "+prep+" AND "+"pobj: "+pobj;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

                if(key==11){
                    query="dobj: "+dobj+" AND "+"prep: "+prep+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

                if(key==12){
                    query="dobj: "+dobj+" AND "+"prep: "+prep+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }


                if(key==13){
                    query="dobj: "+dobj+" AND "+"prep: "+prep+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

                if(key==14){
                    query="dobj: "+dobj+" AND "+"prep: "+prep+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2+" AND "+"pobj2: "+pobj2;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==15){
                    score=null;
                    query="dobj: "+dobj+" AND "+"nsubj: "+nsubj;
                    score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

                if(key==16){
                    score=null;
                    query="prep: "+prep+" AND "+"nsubj: "+nsubj;
                    score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

                if(key==17){
                    query="prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"pobj: "+pobj;
                    score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

                if(key==18){
                    query="prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==19){
                    query="prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

                if(key==20){
                    query="prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2;;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==21){
                    query="prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2+" AND "+"pobj2: "+pobj2;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==22){
                    query="prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"dobj: "+dobj;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==23){
                    query="prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"dobj: "+dobj+" AND "+"pobj: "+pobj;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==24){
                    query="prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"dobj: "+dobj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==25){
                    query="prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"dobj: "+dobj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==26){
                    query="prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"dobj: "+dobj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2;
                    score=null;
                      score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==27){
                    query="prep: "+prep+" AND "+"nsubj: "+nsubj+" AND "+"dobj: "+dobj+" AND "+"pobj: "+pobj+" AND "+"prep1: "+prep1+" AND "+"pobj1: "+pobj1+" AND "+"prep2: "+prep2+" AND "+"pobj2: "+pobj2;
                    score=null;
                    score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==28){
                    query="xcomp: "+xcomp;
                    score=null;
                    score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==29){
                    query="ccomp: "+ccomp;
                    score=null;
                    score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==30){
                    query="advmod: "+advmod;
                    score=null;
                    score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }
                if(key==31){
                    query="iobj: "+iobj;
                    score=null;
                    score=getQuerycount(searcher, query,parser);
                    levelScore.put(key.toString(),score);
                }

            }
          //  System.out.println(sCurrentLine);
//            System.out.println("nsubj: "+nsubj+" verb: "+Verbname+" dobj: "+dobj+" prep: "+prep+" pobj: "+pobj+" prep1: "+prep1+" pobj1: "+pobj1+" prep2: "+prep2+" pobj2: "+pobj2+" xcomp:"+xcomp+" ccomp:"+ccomp+" advmod:"+advmod+"iobj:"+iobj);
        //    System.out.println(levelScore);
//            System.out.println(computeWeightedScore(levelWeights,levelScore)+"\n\n");
            System.out.println(sCurrentLine);
            //String[] queryOrder = new String[] {"6","22","12","23","11","5","18","4","15","17","9","10","16","2","3","28","29","30","31","7","8","13","14","19","20","21","24","25","26","27"};  // 20 queries
            String[] queryOrder = new String[] {"2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","1"};
            Boolean flag = false;
            String result ="";
            for(int i=0;i<queryOrder.length; i++) {
                //System.out.println(i);
              //  System.out.println(queryOrder[i]);
                if(levelScore.get(queryOrder[i])!=null) {

                    HashMap<String, Integer> simVerb = levelScore.get(queryOrder[i]);

                    for(String s:simVerb.keySet()){
                        result = result+" "+simVerb.get(s)+":"+s;
                    }

                    flag = true;
                   // break;
                }
            }
            if (flag==false){
                bw.newLine();
            }
            else {
                bw.write(result);
                bw.newLine();
            }
//            bw.write(String.valueOf(computeWeightedScore(levelWeights,levelScore)));
//            bw.newLine();
            levelScore.clear();
            }

        bw.close();



            //double answer = computeWeightedScore(levelWeights,levelScore);

        }

    }



