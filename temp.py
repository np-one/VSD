# -*- coding: utf-8 -*-
"""
Created on Wed Feb  8 20:18:49 2017

@author: asshriva
"""
import sys
pipeline2 = open(sys.argv[1]+"result1_A3_OLD_q2000_All_query","r")
top_verb = open(sys.argv[1]+"result1_A3_OLD_q2000_all_All_query","w")
len_two_verbs =[]
verb_arr=[]
top_k = 250000
stop_verb = ["be", "'s", "``", "lnternational/800"]
for line in pipeline2:
    if(line == "\n"):
        verb_arr.append([])
    else:
        temp = line.replace("\n","").strip().split(" ")
        temp_new=[]
        for i in range(len(temp)):
            if(temp[i].split(":")[1].strip() in stop_verb or len(temp[i].split(":")[1].strip())==1):
                len_two_verbs.append(temp[i].split(":")[1].strip())
                pass
            else:
                temp[i] = tuple(temp[i].strip().split(":"))
                temp_new.append(temp[i])
        verb_arr.append(temp_new)

for i in range(len(verb_arr)):
    if(len(verb_arr[i])!=0):
        if(verb_arr[i][0][0] != ""):
            verb_arr[i] = sorted(verb_arr[i],key=lambda x: int(x[0]), reverse = True)[:top_k]
            
            
for i in range(len(verb_arr)):
    if(len(verb_arr[i])==0):
        top_verb.write("\n")
    else:
        temp=""
        for j in range(len(verb_arr[i])):
            temp = temp +" "+ verb_arr[i][j][1]
        top_verb.write(temp.strip())
        top_verb.write("\n")
top_verb.close()
#         
#        
#verb_actual = open("/home/asshriva/Documents/logicnet/output_22Jan/pytests/test_2","r")
#pipeline2 = open("/home/asshriva/projects/lucene_vsd/secondPipeline/result1_A3_OLD_q2000_all_All_query","r")
#
#line_num=0
#count=0
#for line1, line2 in zip(verb_actual.readlines(), pipeline2.readlines()):
#    line_num+=1
#    if((line1.strip() == "" and line2.strip() != "") or (line1.strip() == "PASS" and line2.strip() != "")):
#        print line_num
#        count+=1
#print count

#myfile = open("/home/asshriva/projects/lucene_vsd/secondPipeline/result1_A3_OLD_q2000_All_query","r")
#null_count, line_count = 0,0
#for line in myfile:
#    if(line == "\n"):
#        null_count+=1
#    else:
#        line_count+=1
#print null_count, line_count

##### create short data set for compairing both pipelines ###################

#line_numfile = open("/home/asshriva/projects/lucene_vsd/secondPipeline/p2Analysis","r")
#
#for line in line_numfile:
#    if(line.startswith("all")):
#        all_lines = line.replace("all","").replace("\n","").strip().split('  ')
#    if(line.startswith("correct")):
#        correct_lines = line.replace("all","").replace("\n","").strip().split('  ')
#
#datafile = open("/home/asshriva/projects/lucene_vsd/secondPipeline/final-input1","r")
#shortData = open("/home/asshriva/projects/lucene_vsd/secondPipeline/final-input1_shortData","w")
#
#num=0
#for line in datafile:
#    num = num+1
#    if(str(num) in all_lines):
#        shortData.write(line)
#shortData.close()
        











































        