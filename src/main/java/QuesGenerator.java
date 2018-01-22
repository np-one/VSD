import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by prajpoot on 6/10/17.
 */
public class QuesGenerator {
    public static void main(String args[]) {


        SentenceAnalysis s = new SentenceAnalysis("/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english" +
                "-left3words-distsim.tagger");

        String fact = "John killed Mary in London";
        ArrayList wh = new ArrayList<>(Arrays.asList("who", "what", "when", "where", "why", "how", "how much"));
        ArrayList aux = new ArrayList<>(Arrays.asList("do", "does", "did", "has", "have", "had", "is", "am", "are", "was", "were", "be", "being", "been"
                , "may", "must", "might", "should", "could", "would", "shall", "will", "can", ""));

        ArrayList<String> pp = new ArrayList<>();
        ArrayList<String> trg = new ArrayList<>();
        ArrayList<String> auxlst = new ArrayList<>();

        int ncount = 0;

//        pp.add("to");
//        pp.add("by");
//        pp.add("for");
//        pp.add("with");
//        pp.add("about");

        ArrayList<String> subj = new ArrayList<>();
        subj.add("someone");
        subj.add("something");
        ArrayList<String> obj1 = new ArrayList<>();
        obj1.add("someone");
        obj1.add("something");
        ArrayList<String> obj2 = new ArrayList<>();
        obj2.add("someone");
        obj2.add("something");

        int nouncount = 0;

        boolean contains_obj1 = false;
        boolean contains_obj2 = false;
        boolean contains_pp = false;
        boolean contains_aux = false;
        boolean contains_subj = false;

        List<HasWord> tokens = s.GetTokens(fact.split("\t")[0]);
        List<TaggedWord> POS = s.GetPOS(tokens);
        String prev_val_prep = "";
        String prev_tag_prep = "";
        System.out.println(POS);
        for (TaggedWord tag : POS) {

            if (tag.tag().toString().startsWith("NN")) {
                if (nouncount == 0) {
                    subj.add(s.Lemmetize(tag.value().toString()));
                    contains_subj = true;
                    ncount += 1;
                }
                if (nouncount == 1) {
                    obj1.add(s.Lemmetize(tag.value().toString()));
                    contains_obj1 = true;
                    ncount += 1;
                }
                if (nouncount == 2) {
                    obj2.add(s.Lemmetize(tag.value().toString()));
                    contains_obj2 = true;
                    ncount += 1;
                }
                nouncount++;
            }

            if (tag.tag().toString().startsWith("VB") && !aux.contains(tag.value())) {
                trg.add((tag.value().toString()));
                continue;
            }

            if (tag.tag().toString().startsWith("VB") && aux.contains(tag.value())) {
                auxlst.add((tag.value().toString()));
                contains_aux = true;
                continue;
            }

            if ((tag.tag().toString().startsWith("IN") || tag.tag().toString().startsWith("TO")) &&
                    (!prev_tag_prep.startsWith("IN") && !prev_tag_prep.startsWith("TO"))) {
                prev_val_prep = tag.value().toString();
                prev_tag_prep = tag.tag().toString();
                continue;
            }

            if ((tag.tag().toString().startsWith("IN") || tag.tag().toString().startsWith("TO")) &&
                    (prev_tag_prep.startsWith("IN") || prev_tag_prep.startsWith("TO"))) {
                prev_val_prep = prev_val_prep + " " + tag.value().toString();
                continue;
            }

            if (!tag.tag().startsWith("IN") && prev_val_prep != "") {
                if (!pp.contains(prev_val_prep)) {
                    pp.add(prev_val_prep);
                    contains_pp = true;
                }
                prev_val_prep = "";
            }

        }

  /*      System.out.println("subj: " + subj);
        System.out.println("obj_1: " + obj1);
        System.out.println("obj_2: " + obj2);
        System.out.println("trg: " + trg);
        System.out.println("pp: " + pp);
        System.out.println("aux: " + auxlst);
        System.out.println(ncount);*/

        if (ncount == 3) {
            //R0
            for (int whi = 0; whi < 2; whi++) {
                for (int obji1 = 0; obji1 < obj1.size(); obji1++) {
                    for (int obji2 = 0; obji2 < obj2.size(); obji2++) {
                        if (contains_pp) {
                            for (int ppi = 0; ppi < pp.size(); ppi++) {
                                for (int auxi = 0; auxi < aux.size(); auxi++)
                                    System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + trg.get(0) + " " + obj1.get(obji1) + " " + pp.get(ppi) + " " + obj2.get(obji2));
                            }
                        } else {
                            for (int auxi = 0; auxi < aux.size(); auxi++)
                                System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + trg.get(0) + " " + obj1.get(obji1) + " " + obj2.get(obji2));
                        }
                    }
                }
            }
            //R1
            for (int whi = 0; whi < 2; whi++) {
                for (int subji = 0; subji < subj.size(); subji++) {
                    for (int obji2 = 0; obji2 < obj2.size(); obji2++) {
                        if (contains_pp) {
                            for (int ppi = 0; ppi < pp.size(); ppi++) {
                                for (int auxi = 0; auxi < aux.size(); auxi++)
                                    System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + subj.get(subji) + " " + trg.get(0) + " " + pp.get(ppi) + " " + obj2.get(obji2));
                            }
                        } else {
                            for (int auxi = 0; auxi < aux.size(); auxi++)
                                System.out.println(wh.get(whi) + " " + auxlst.get(auxi) + " " + subj.get(subji) + " " + trg.get(0) + " " + obj2.get(obji2));
                        }
                    }
                }
            }
            //R2
            for (int whi = 0; whi < 2; whi++) {
                for (int subji = 0; subji < subj.size(); subji++) {
                    for (int obji1 = 0; obji1 < obj1.size(); obji1++) {
                        if (contains_pp) {
                            for (int ppi = 0; ppi < pp.size(); ppi++) {
                                for (int auxi = 0; auxi < aux.size(); auxi++)
                                    System.out.println(pp.get(ppi) + " " + wh.get(whi) + " " + aux.get(auxi) + " " + subj.get(subji) + " " + trg.get(0) + " " + obj1.get(obji1));
                            }
                        } else {
                            for (int auxi = 0; auxi < aux.size(); auxi++)
                                System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + subj.get(subji) + " " + trg.get(0) + " " + obj1.get(obji1));
                        }

                    }
                }

            }

            //rules2
            for (int whi = 2; whi < wh.size(); whi++) {
                for (int subji = 0; subji < subj.size(); subji++) {
                    for (int obji1 = 0; obji1 < obj1.size(); obji1++) {
                        if (contains_pp) {
                            for (int ppi = 0; ppi < pp.size(); ppi++) {
                                for (int auxi = 0; auxi < aux.size(); auxi++)
                                    System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + pp.get(ppi) + " " + " " + subj.get(subji) + " " + trg.get(0) + " " + obj1.get(obji1));
                            }
                        } else {
                            for (int auxi = 0; auxi < aux.size(); auxi++)
                                System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + subj.get(subji) + " " + trg.get(0) + " " + obj1.get(obji1));
                        }
                    }
                }
            }

            for (int whi = 2; whi < wh.size(); whi++) {
                for (int subji = 0; subji < subj.size(); subji++) {
                    for (int obji1 = 0; obji1 < obj1.size(); obji1++) {
                        for (int obji2 = 0; obji2 < obj2.size(); obji2++) {
                            if (contains_pp) {
                                for (int ppi = 0; ppi < pp.size(); ppi++) {
                                    for (int auxi = 0; auxi < aux.size(); auxi++)
                                        System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + subj.get(subji) + " " + trg.get(0) + " " + obj1.get(obji1) + " " + pp.get(ppi) + " " + obj2.get(obji2));
                                }
                            } else {
                                for (int auxi = 0; auxi < aux.size(); auxi++)
                                    System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + subj.get(subji) + " " + trg.get(0) + " " + obj1.get(obji1) + " " + obj2.get(obji2));
                            }
                        }
                    }
                }
            }


        }

        if (ncount == 2) {
            //R0
            for (int whi = 0; whi < 2; whi++) {
                for (int obji1 = 0; obji1 < obj1.size(); obji1++) {
                    if (contains_pp) {
                        for (int ppi = 0; ppi < pp.size(); ppi++) {
                            if (contains_aux)
                                for (int auxi = 0; auxi < aux.size(); auxi++)
                                    System.out.println(wh.get(whi) + " " + aux.get(auxi) + trg.get(0) + " " + auxlst.get(0) + " " + obj1.get(obji1) + " " + pp.get(ppi));
                            else
                                for (int auxi = 0; auxi < aux.size(); auxi++)
                                    System.out.println(wh.get(whi) + " " + aux.get(auxi) + trg.get(0) + " " + obj1.get(obji1) + " " + pp.get(ppi));
                        }
                    } else {
                        for (int auxi = 0; auxi < aux.size(); auxi++)
                            System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + trg.get(0) + " " + obj1.get(obji1));
                    }
                }
            }
            //R1
            for (int whi = 0; whi < 2; whi++) {
                for (int subji = 0; subji < subj.size(); subji++) {
                    if (contains_pp) {
                        for (int ppi = 0; ppi < pp.size(); ppi++) {
                            for (int auxi = 0; auxi < aux.size(); auxi++)
                                System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + subj.get(subji) + " " + trg.get(0) + " " + pp.get(ppi));
                        }
                    } else {
                        for (int auxi = 0; auxi < aux.size(); auxi++)
                            System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + subj.get(subji) + " " + trg.get(0));
                    }

                }
            }
            //rules2
            for (int whi = 2; whi < wh.size(); whi++) {
                for (int subji = 0; subji < subj.size(); subji++) {
                    for (int obji1 = 0; obji1 < obj1.size(); obji1++) {
                        if (contains_pp) {
                            for (int ppi = 0; ppi < pp.size(); ppi++) {
                                for (int auxi = 0; auxi < aux.size(); auxi++)
                                    System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + pp.get(ppi) + " " + " " + subj.get(subji) + " " + trg.get(0) + " " + obj1.get(obji1));
                            }
                        } else {
                            for (int auxi = 0; auxi < aux.size(); auxi++)
                                System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + subj.get(subji) + " " + trg.get(0) + " " + obj1.get(obji1));
                        }
                    }
                }
            }

            for (int whi = 2; whi < wh.size(); whi++) {
                for (int subji = 0; subji < subj.size(); subji++) {
                    for (int obji1 = 0; obji1 < obj1.size(); obji1++) {
                        if (contains_pp) {
                            for (int ppi = 0; ppi < pp.size(); ppi++) {
                                for (int auxi = 0; auxi < aux.size(); auxi++)
                                    System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + subj.get(subji) + " " + trg.get(0) + " " + obj1.get(obji1) + " " + pp.get(ppi));
                            }
                        } else {
                            for (int auxi = 0; auxi < aux.size(); auxi++)
                                System.out.println(wh.get(whi) + " " + aux.get(auxi) + " " + subj.get(subji) + " " + trg.get(0) + " " + obj1.get(obji1));
                        }
                    }
                }

            }

        }

    }


}

