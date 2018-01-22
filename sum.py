# filecheck =open("pass_p1.txt","r")
# check=[]
# for line in filecheck.readlines():
#   check.append(int(line.replace("\n","").strip()))
import sys
file1 = open(sys.argv[1]+"final-all-top2000-allquery","r")
file2 = open(sys.argv[1]+"verbs2","r")
count=0
linec=0
two=0
linenum=0
lines=[]
correct=[]
doublecount=0
nulls=[]
# print len(check)
for orig,pred in zip(file2.readlines(),file1.readlines()):
	linenum+=1
	# if linenum not in check:
	#   continue
	# else:
	# 	# print linenum
	if pred!="\n":

 	  linec=linec+1
 	  lines.append(linenum)
	else:
	  nulls.append(linenum)
	  continue
#	linec+=1
	orig=orig.replace("\n","")
	pred=pred.replace("\n","")
	

	if orig in pred.split(" ")[0]:
		if len(pred.split(" "))>1: 
			if int(pred.split(" ")[0].split("=")[1].replace(",","").strip().replace("{",""))==int(pred.split(" ")[1].split("=")[1].strip().replace("}","").replace(",","")):
				doublecount=doublecount+1
		#print orig +"==>"+pred
		count=count+1
		correct.append(linenum)
	else:
	  if len(pred.split(" "))>1:

	  	
	    if orig in pred.split(" ")[1]:
	    	if int(pred.split(" ")[0].split("=")[1].replace(",","").strip().replace("{",""))==int(pred.split(" ")[1].split("=")[1].strip().replace("}","").replace(",","")):
				doublecount=doublecount+1
	     	two=two+1
	    else:
	    	pass
	      #print orig +"==>"+pred
null1=0
countw=0
filer = open("/home/prajpoot/Semlink_EXPERIMENT/test_v3.2/final-input-frames","r")
for line in filer.readlines():
	if "$$$$$" in line:
		countw=countw+1
		if countw in nulls:
			#print "\n\n"+str(countw)+" "+line[:-1]
			if "@1" in line:
				null1=null1+1
			do=1
				
	      


fileres=open(sys.argv[1]+"result2","w")	      
fileres.write(str(count)+","+str(two)+","+str(linec)+","+str(linenum))
fileres.write("\ncount-null "+str(null1))
fileres.close()
# print "All Lines"+str(lines)
# print "Correct Lines"+str(correct)

filer.close()

