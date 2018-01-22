import sys
# file=open("/home/prajpoot/WordNetExtraction/TESTTTT","w")
# file.write(sys.argv[1])
# file.close()

# -*- coding: utf-8 -*-
"""
Created on Thu Jan  5 23:44:04 2017

@author: asshriva
"""
import re
myfile = open(sys.argv[1]+"final-input","r")
output = open(sys.argv[1]+"final-input1","w")

month = ["Jan", "Feb", "March", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"]
month_actual = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"]
for line in myfile:
    temp = line.split(" ")
    if("/" in temp[0]):
        temp[0] = temp[0].split("/")[0].strip()
    for i in range(len(temp)):
        if("\/" in temp[i]):
            temp[i] = temp[i].replace("\/","")
        if(temp[i] == "$" or temp[i] == "$\n" or bool(re.match("[a-z]+[$]", temp[i]))):
            temp[i] = "doller"
        if((temp[i].endswith(".") or temp[i].endswith(".\n")) and len(temp[i]) > 1):
            if(temp[i].replace("\n","").replace(".","") in month):
                temp[i] = month_actual[month.index(temp[i].replace("\n","").replace(".",""))]
            elif(temp[i].replace("\n","") in ["U.S.", "U.K.", "S.A.", "S.A"]):
                temp[i] = "location"
            elif(temp[i].replace("\n","") not in ["a.m.", "p.m.", "A.M.", "P.M"]):
                temp[i] = "organization"
        if(temp[i].replace("\n","").strip() in ["U.S", "U.K", "S.A"]):
            temp[i] = "location"
        if(bool(re.match("[A-Z]+[&][A-Z]+", temp[i].replace("\n","")) or bool(re.match("[a-z]+[&][a-z]+", temp[i].replace("\n",""))))):
            temp[i] = "organization"
        if(temp[i].replace("\n","").isdigit()):           
#        if(int(temp[i]) == True ):
            if(int(temp[i].replace("\n",""))>1500 and int(temp[i].replace("\n","")) < 2200):
                temp[i] = "year"
            else:
                temp[i] = "number"
        if(temp[i].replace("\n","").isdigit() or re.match("[a-z]+[0-9]+", temp[i].replace("\n",""))):
            temp[i] = "number"
        if((temp[i][0]).isdigit() and temp[i][len(temp[i])-1].isdigit() and temp[i][len(temp[i])-2].isdigit() and temp[i][len(temp[i])-3].isdigit()):
            if("," in temp[i]):
                temp[i] = "number"
        if((temp[i][0]).isdigit() and temp[i][len(temp[i])-1].isdigit()):
            if("/" in temp[i]):
                temp[i] = "number"
            if(":" in temp[i]):
                temp[i] = "time"
        if(re.match("['][0-9]+", temp[i].replace("\n",""))):
            temp[i] = "number"
        if(temp[i]=="N" or temp[i] == "N\n" or temp[i] == "'") or temp[i] == "'\n":
            temp[i] = "null"
        if(temp[i].replace("\n","").strip() == "'s"):
            temp[i] = "person"
        if(temp[i].replace("\n","") =="a.m." or temp[i].replace("\n","") =="p.m."):
            temp[i] = "time"
        if(bool(re.match("[0-9]+[.][0-9]+", temp[i].replace("\n",""))) or bool(re.match("[0-9]+[,][0-9]+",temp[i].replace("\n","")))):
            temp[i] = "number"
        if(temp[i] == "%" or temp[i] == "%\n"):
            temp[i] = "percent"
        if(re.match("[A-Z]*['][A-Z]+[a-z]*", temp[i].replace("\n",""))):
            temp[i] = "person"
        if(re.match("[A-Z]*[a-z]*[0-9][A-Z]*[a-z]*", temp[i].replace("\n",""))):
            temp[i] = "number"
        if(temp[i].strip() == "\*" or temp[i] == "\*\*"):
            temp[i] = "null"
        
    output.write(" ".join((temp)).strip())
    output.write("\n")

myfile.close()
output.close()

# output_check = open("/home/asshriva/Documents/logicnet/output","r")
# count =0
# #line_num =0
# for line in output_check:
# #    line_num+=1
#     temp = line.strip().split(" ")
#     if(len(temp)!=13):
#         count+=1
#         #print line_num
        
# output_check.close()
            
#             