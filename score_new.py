import sys
import collections
import os
#fileresult=open(sys.argv[1],"r")
#original_file = "/home/asshriva/Documents/logicnet/output_30Jan/output2.txt"
original_file =sys.argv[1]+"result1_A3_OLD"
fileresult = open(original_file,"r")
filew=open(sys.argv[1]+"test_1","w")
line_num=0
K_val=int(sys.argv[2])        #int(sys.argv[2])
name=""
dict1={}
rep =0.0
count_same =0
for line in fileresult.readlines():
	#print line
	line_num=line_num+1
	if line_num==1:
		continue
	if "#####" in line:   # exract possible verb with class
		name=line[:-1]
		continue 
	if "$$$$$" in line:
		dict2= collections.OrderedDict(reversed(sorted(dict1.items())))
		#print dict2
		classcount={}
		for k,v in dict2.iteritems():
			temp_c = v.split(" ")
			for item in temp_c:
				if item.split("-")[1].strip() in classcount:
					classcount[item.split("-")[1].strip()] = classcount[item.split("-")[1].strip()]+1
				else:
					classcount[item.split("-")[1].strip()] = 1

		res=""
		co=0
	#	print dict2
		if len(dict2)>4:
			for k,v in dict2.iteritems():
				temp_c = v.split(" ")
				for item in temp_c:
					if co>K_val-1:
						break
					co=co+1
					#res=res+" "+v.split("-")[1]
					res=res+" "+item.split("-")[1]+":"+str(k)
			#print res
			filew.write(res+"\t"+str(classcount)+"\n")
		if len(dict2)>0 and len(dict2)<=4:
			for k,v in dict2.iteritems():
				temp_c = v.split(" ")
				for item in temp_c:
					if co>4:
						break
					co=co+1
					#res=res+" "+v.split("-")[1]
					res=res+" "+item.split("-")[1]+":"+str(k)
			filew.write(res+"\t"+str(classcount)+"\n")	
			#print res
		if len(dict2)==0:
		  filew.write(res+"\n")

		  #pass
			#print res
			
		dict1.clear()
		continue
	#print line
	if float(line[:-1]) in dict1:
	#	pass
	  del dict1[float(line[:-1])]
	  rep=float(line[:-1])
	else:
	  if (float(line[:-1])!=rep):
		dict1[float(line[:-1])]=name
# #	print line
# 	if float(line[:-1].split("\t")[1].strip()) in dict1:
# 		if(name not in dict1[float(line[:-1].split("\t")[1].strip())]):
# 			dict1[float(line[:-1].split("\t")[1].strip())]=dict1[float(line[:-1].split("\t")[1].strip())] +" "+ name
# #	  del dict1[float(line[:-1].split("\t")[1].strip())]
# #	  rep=float(line[:-1].split("\t")[1].strip())
# 	else:
#          if (float(line[:-1].split("\t")[1].strip())!=rep):
# 		dict1[float(line[:-1].split("\t")[1].strip())]=name

fileresult.close()
filew.close()

#########################
import operator
#fileresult=open("EXAMPLE_wiki_OLD","r")
fileresult=open(sys.argv[1]+"test_1","r")  #open("test_1","r")
filew = open(sys.argv[1]+"test_2","w")  #open("/home/asshriva/Documents/logicnet/output_22Jan/pytests/test_2","w")
dic={}
diccount={}
for line in fileresult.readlines():
	if(line[:-1]==""):
		filew.write("\n")
		continue
	temp=line[:-1].split("\t")[0].split(" ")
	for i in range(0,len(temp)):
		if temp[i].split(":")[0] in dic:
			dic[temp[i].split(":")[0]]=dic[temp[i].split(":")[0]]+1
#			diccount[temp[i].split(":")[0]]= diccount[temp[i].split(":")[0]]+diccount[temp[i].split(":")[1]]
		else:
			dic[temp[i].split(":")[0]]=1.0
#			diccount[temp[i].split(":")[0]]=diccount[temp[i].split(":")[1]]
			
		if(1):
		  if temp[i].split(":")[0] in diccount:
		  	#if float(temp[i].split(":")[1])> float(diccount[temp[i].split(":")[0]]):
			diccount[temp[i].split(":")[0]]+= float(temp[i].split(":")[1])
		  else:
		    if temp[i]!="":
		   #   print "####"+temp[i]+"#########"
		      diccount[temp[i].split(":")[0]]=float(temp[i].split(":")[1])
		  
	############### new
	ex_dict_string = line[:-1].split("\t")[1].split(",")
	ex_dict ={}
	for j in range(len(ex_dict_string)):
		temp = ex_dict_string[j].split(":")
		ex_dict[temp[0].replace("'","").replace("{","").strip()] = int(temp[1].replace("}","").strip())
	if '' in dic and len(dic)>1:
		del dic['']
	# print dic, "1111111111"
	# print ex_dict ,"22222222222222"
	for key,value in dic.iteritems():
		dic[key] = dic[key]*(dic[key]/float(ex_dict[key]))
		#print (dic[key]/float(ex_dict[key]))
		#print "****", dic[key]

	#print dic, "***"

	############### new
	d= sorted(dic.items(), key=operator.itemgetter(1),reverse=True)


	# print d
	# print ex_dict
	#print "#####"

	count1=0
	if len(d)>1:
		if d[0][1]==d[1][1]:
		  count1+=1
		  # print diccount,"ttttttttttt"	
		  if diccount[(d[0][0])]>diccount[d[1][0]]:
		  	#print str(d[0][0])+" "+str(d[1][0])
		  	filew.write(str(d[0][0])+" "+str(d[1][0])+"\n")

		  else:
		 	if diccount[(d[0][0])]<diccount[d[1][0]]:
		 		#print str(d[1][0])+" "+str(d[0][0])
		 		filew.write(str(d[1][0])+" "+str(d[0][0])+"\n")
		  	else:
#		  		print "pass" + "\t" +str(d[1][0])+" "+str(d[0][0]) +"\t"+ str(line_num)
		  		filew.write("PASS"+"\n")
		  		
			#print "count"+ str(d[0][1]) + "=="+str(d[1][1]) 
			# print "pass "+ diccount[(d[0][0])] + "=="+diccount[d[1][0]]
		else:
		#s	pass
			filew.write(str(d[0][0])+" "+str(d[1][0])+"\n")
			#print str(d[0][0])+" "+str(d[1][0])
	else:
		#pass
		filew.write(str(d[0][0])+"\n")
		#print str(d[0][0])
	dic.clear()
	diccount.clear()
	count_same += count1
fileresult.close()
filew.close()
#print count_same

########################3
#line_numfile = open("/home/asshriva/projects/lucene_vsd/secondPipeline/p2Analysis","r")
#
#for line in line_numfile:
#    if(line.startswith("all")):
#        all_lines = line.replace("all","").replace("\n","").strip().split('  ')



fileresult= open(sys.argv[1]+"test_2","r")	#open("/home/asshriva/Documents/logicnet/output_22Jan/pytests/test_2","r")
testresult= open(sys.argv[1]+"verbs2","r")#open("/home/asshriva/Documents/logicnet/output_22Jan/pytests/verbs2","r")
real=0
fake=0
nulls1=[]
lines=0
wron=0
wrong=[]
two=[]
pass_arr=[]
pass_count =0
num=0
for line,line2 in zip(testresult.readlines(),fileresult.readlines()):
	if("None" in line):
		lines = lines+1
		continue
	lines=lines+1
	if line2[:-1]!='':
		if line2[:-1].split(" ")[0].strip() == "PASS":
			pass_count +=1
			pass_arr.append(lines)
		elif line2[:-1].split(" ")[0].strip()==line[:-1].strip():
			real=real+1
		else:
			#print str(line[:-1])+"== P"+str(line2[:-1])+" line:"+str(lines)
			#wron=wron+1
			if line2[:-1].split(" ")[len(line2[:-1].split(" "))-1].strip()==line[:-1].strip():
				two.append(lines)
				#print line2 
				#real=real+1
				#print str(line[:-1])+"== P"+str(line2[:-1])+" line:"+str(lines)
			else:
				wron=wron+1
				wrong.append(lines)
				#print str(line[:-1])+"== P"+str(line2[:-1])+" line:"+str(lines)
	else:
		nulls1.append(lines)
		fake=fake+1

#print real

null1=0
countw=0
#filer=open(sys.argv[1],"r")
filer = open(original_file,"r")
for line in filer.readlines():
	if "$$$$$" in line:
		countw=countw+1
		if countw in nulls1:
			#print "\n\n"+str(countw)+" "+line[:-1]
			if "@1" in line:
				null1=null1+1
				nulls1.remove(countw)
			do=1
				

filer.close()
# countw=0
# dict3={}
# filer=open("test_1","r")
# filer2=open("test_2","r")

# for line,line2 in zip(filer.readlines(),filer2.readlines()):
# 	countw=countw+1
# 	if countw in wrong:
# 		# pass
# 		print "\n##########"+str(countw)
		# print line
		# print gline2+"\n\n\n"
		# if len(line.split(" "))>3:
		# 	temp=line[:-1].split(" ")
		# 	for i in range(0,len(temp)):
		# 		if temp[i] in dict3:
		# 			dict3[temp[i]]=dict3[temp[i]]+1
		# 		else:
		# 			dict3[temp[i]]=1

		# if '' in dict3:
		# 	del[dict3['']]
		# #print str(countw)+" "+ str(dict3)
		# dict3.clear()
real= real+null1	
print "Origianl file :" + original_file	
print "k =" +str(K_val)	
print "correct= "+str(real)
print "wrong= "+str(wron)
print "two= " +str(len(two))
print "LeftNull= "+str(len(nulls1))
print "Pass_count= " + str(pass_count)

print "----------------------------"
print str(int(real+wron+len(two)))+" "+str(K_val)
print "Top1: "+str(real/float(real+wron+len(two)))+ " Top2: "+str((real+len(two))/float(real+wron+len(two)))
print "----------------------------"
fileres=open(sys.argv[1]+"result","w")
fileres.write("Origianl file :" + original_file + "\n")	
fileres.write("k =" +str(K_val) + "\n")			
fileres.write("correct= "+str(real)+"\n")
fileres.write("wrong= "+str(wron)+"\n")
fileres.write("two= " +str(len(two))+"\n")
fileres.write("LeftNull:"+str(len(nulls1))+"\n")
fileres.write(str(nulls1)+"\n")
fileres.write("Pass_count= " + str(pass_count)+"\n")
fileres.write(str(pass_arr)+"\n")
fileres.close()

## Writing results
#with open("/home/asshriva/Documents/logicnet/resultsfile", "a") as resultfile:
#    resultfile.write("Origianl file :" + original_file + "\n")
#    resultfile.write("k =" +str(K_val)+ "\n")
#    resultfile.write("correct= "+str(real)+ "\n")
#    resultfile.write("wrong= "+str(wron)+ "\n")
#    resultfile.write("two= " +str(len(two))+ "\n")
#    resultfile.write("LeftNull= "+str(len(nulls1))+ "\n")
#    resultfile.write("Pass_count= " + str(pass_count)+ "\n\n")
#resultfile.close()



# print "#        Top 1: "+ str(round(real*100/float(real+wron+len(two)+len(nulls1)),2))+"        #"
# print "#        Top 2: "+ str(round((real+len(two))*100/float(real+wron+len(two)+len(nulls1)),2))+"        #"

#filer.close()	
#countw=0
#filer1 = open("/home/asshriva/Downloads/pipelineFiles/input/Verbnet_test_data/ALL_DATA","r")
#for line in filer1.readlines():
#		countw=countw+1
#		if countw in wrong:
#			print line.split("\t")[1], line.split("\t")[2]
# #os.remove("test_1")
# #os.remove("test_2")
# # ########################################################################################################################################################################################################################################




























# fileresult=open(sys.argv[1]+"_OLD","r")
# filew=open("test_1_old","w")
# line_num=0
# K_val=int(sys.argv[2])
# name=""
# dict1={}
# rep =0.0
# for line in fileresult.readlines():
# 	#print line
# 	line_num=line_num+1
# 	if line_num==1:
# 		continue
# 	if "#####" in line:
# 		name=line[:-1]
# 		continue 
# 	if "$$$$$" in line:
# 		dict2= collections.OrderedDict(reversed(sorted(dict1.items())))
# 		res=""
# 		co=0
# 	#	print dict2
# 		if len(dict2)>4:
# 			for k,v in dict2.iteritems():
# 				if co>K_val-1:
# 					break
# 				co=co+1
# 				#res=res+" "+v.split("-")[1]
# 				res=res+" "+v.split("-")[1]+":"+str(k)
# 			#print res
# 			filew.write(res+"\n")
# 		if len(dict2)>0 and len(dict2)<=4:
# 			for k,v in dict2.iteritems():
# 				if co>4:
# 					break
# 				co=co+1
# 				#res=res+" "+v.split("-")[1]
# 				res=res+" "+v.split("-")[1]+":"+str(k)
# 			filew.write(res+"\n")	
# 			#print res
# 		if len(dict2)==0:
# 		  filew.write(res+"\n")
# 		  #pass
# 			#print res
			
# 		dict1.clear()
# 		continue
# 	#print line
# 	if float(line[:-1]) in dict1:
# 	  del dict1[float(line[:-1])]
# 	  rep=float(line[:-1])
# 	else:
# 	  if (float(line[:-1])!=rep):
# 		dict1[float(line[:-1])]=name

# fileresult.close()
# filew.close()

# ##########################
# import operator
# #fileresult=open("EXAMPLE_wiki_OLD","r")
# fileresult=open("test_1_old","r")
# filew = open("test_2_old","w")
# dic={}
# diccount={}
# for line in fileresult.readlines():
# 	temp=line[:-1].split(" ")
# 	for i in range(0,len(temp)):
# 		if temp[i].split(":")[0] in dic:
# 			dic[temp[i].split(":")[0]]=dic[temp[i].split(":")[0]]+1
# #			diccount[temp[i].split(":")[0]]= diccount[temp[i].split(":")[0]]+diccount[temp[i].split(":")[1]]
# 		else:
# 			dic[temp[i].split(":")[0]]=1
# #			diccount[temp[i].split(":")[0]]=diccount[temp[i].split(":")[1]]
			
# 		if(1):
# 		  if temp[i].split(":")[0] in diccount:
# 		  	#if float(temp[i].split(":")[1])> float(diccount[temp[i].split(":")[0]]):
# 			diccount[temp[i].split(":")[0]]+= float(temp[i].split(":")[1])
# 		  else:
# 		    if temp[i]!="":
# 		    #print "####"+temp[i]+"#########"
# 		      diccount[temp[i].split(":")[0]]=float(temp[i].split(":")[1])
		  
		  
# 	if '' in dic and len(dic)>1:
# 		del dic['']

# 	d= sorted(dic.items(), key=operator.itemgetter(1),reverse=True)
# 	if len(d)>1:
# 		if d[0][1]==d[1][1]:
# 		#if 1:	
# 		  if diccount[(d[0][0])]>diccount[d[1][0]]:
# 		  	#print str(d[0][0])+" "+str(d[1][0])
# 		  	filew.write(str(d[0][0])+" "+str(d[1][0])+"\n")

# 		  else:
# 		 	if diccount[(d[0][0])]<diccount[d[1][0]]:
# 		 		#print str(d[1][0])+" "+str(d[0][0])
# 		 		filew.write(str(d[1][0])+" "+str(d[0][0])+"\n")
# 		  	else:
# 		  		#print "pass"
# 		  		filew.write("pass"+"\n")
		  		
# 			#print "count"+ str(d[0][1]) + "=="+str(d[1][1]) 
# 			# print "pass "+ diccount[(d[0][0])] + "=="+diccount[d[1][0]]
# 		else:
# 		#s	pass
# 			filew.write(str(d[0][0])+" "+str(d[1][0])+"\n")
# 			#print str(d[0][0])+" "+str(d[1][0])
# 	else:
# 		#pass
# 		filew.write(str(d[0][0])+"\n")
# 		#print str(d[0][0])
# 	dic.clear()
# 	diccount.clear()
# fileresult.close()
# filew.close()

# ########################3
# fileresult=open("test_2_old","r")
# testresult=open("verbs2","r")
# #real=0
# #fake=0
# lines=0
# for line,line2 in zip(testresult.readlines(),fileresult.readlines()):
# 	lines=lines+1
# 	if lines in nulls1:
# 		if line2[:-1]!='':
# 			if line2[:-1].split(" ")[0]==line[:-1]:
# 				real=real+1
# 				nulls1.remove(lines)
# 			else:
# 				#print str(line[:-1])+"== P"+str(line2[:-1])+" line:"+str(lines)
# 				wron=wron+1
# 				nulls1.remove(lines)
# 				# if line2[:-1].split(" ")[len(line2[:-1].split(" "))-1]==line[:-1]:
# 				# 	two.append(lines)
# 				# 	#print line2 
# 				# 	#real=real+1
# 				# 	#print str(line[:-1])+"== P"+str(line2[:-1])+" line:"+str(lines)
# 				# else:
# 				# 	wron=wron+1
# 				# 	#print str(line[:-1])+"== P"+str(line2[:-1])+" line:"+str(lines)

# print "#############----EXP_1-----################"		
# print "correct= "+str(real)
# print "wrong= "+str(wron)
# # print "nulls= "+str(fake)
# print "two= " +str(len(two))
# print "LeftNull:"+str(len(nulls1))
# filer.close()	
# #os.remove("test_1_old")
# # os.remove("test_2_old")

# print "#############----EXP_2-----################"

# import collections
# file1=open("test_1_old","r")
# file2=open("test_1","r")
# filew = open("test_3","w")
# for line1,line2 in zip(file1.readlines(),file2.readlines()):
# 	line1=line1.strip()
# 	line2=line2.strip()
# 	temp1=line1.split(" ")
# 	temp2=line2.split(" ")
# 	# if len(temp1)==5 and len(temp2)==5:
# 	# 	temp=temp1[:3]+temp2[:2]
# 	# else:
# 	temp=temp1[:int(sys.argv[2])]+temp2[:int(sys.argv[2])]
# 	for i in range(0,len(temp)):
# 		if temp[i].split(":")[0] in dic:
# 			dic[temp[i].split(":")[0]]=dic[temp[i].split(":")[0]]+1
# #			diccount[temp[i].split(":")[0]]= diccount[temp[i].split(":")[0]]+diccount[temp[i].split(":")[1]]
# 		else:
# 			dic[temp[i].split(":")[0]]=1
# #			diccount[temp[i].split(":")[0]]=diccount[temp[i].split(":")[1]]
			
# 		if(1):
# 		  if temp[i].split(":")[0] in diccount:
# 		  	#if temp[i].split(":")[1]> diccount[temp[i].split(":")[0]]:
# 			diccount[temp[i].split(":")[0]]=float(diccount[temp[i].split(":")[0]])+ float(temp[i].split(":")[1])
# 		  else:
# 		    if temp[i]!="":
# 		    #print "####"+temp[i]+"#########"
# 		      diccount[temp[i].split(":")[0]]=float(temp[i].split(":")[1])
		  
		  
# 	if '' in dic and len(dic)>1:
# 		del dic['']

# 	d= sorted(dic.items(), key=operator.itemgetter(1),reverse=True)
# 	if len(d)>1:
# 		if d[0][1]==d[1][1]:
# 		#if 1:	
# 		  if diccount[(d[0][0])]>diccount[d[1][0]]:
# 		  	#print str(d[0][0])+" "+str(d[1][0])
# 		  	filew.write(str(d[0][0])+" "+str(d[1][0])+"\n")

# 		  else:
# 		 	if diccount[(d[0][0])]<diccount[d[1][0]]:
# 		 		#print str(d[1][0])+" "+str(d[0][0])
# 		 		filew.write(str(d[1][0])+" "+str(d[0][0])+"\n")
# 		  	else:
# 		  		#print "pass"
# 		  		filew.write("pass"+"\n")
		  		
# 			#print "count"+ str(d[0][1]) + "=="+str(d[1][1]) 
# 			# print "pass "+ diccount[(d[0][0])] + "=="+diccount[d[1][0]]
# 		else:
# 		#s	pass
# 			filew.write(str(d[0][0])+" "+str(d[1][0])+"\n")
# 			#print str(diccount[(d[0][0])])+"++++++++"+str(diccount[d[1][0]])
# 			#print str(d[0][0])+" "+str(d[1][0])
# 	else:
# 		#pass
# 		filew.write(str(d[0][0])+"\n")
# 		#print str(d[0][0])
# 	dic.clear()
# 	diccount.clear()
# fileresult.close()
# filew.close()

# ###########################33
# fileresult=open("test_3","r")
# testresult=open("verbs2","r")
# real=0
# fake=0
# nulls1=[]
# lines=0
# wron=0
# wrongl=[]
# two=[]
# for line,line2 in zip(testresult.readlines(),fileresult.readlines()):
# 	lines=lines+1
# 	if line2[:-1]!='':
# 		if line2[:-1].split(" ")[0]==line[:-1]:
# 			real=real+1
# 		else:
# 			#print str(line[:-1])+"== P"+str(line2[:-1])+" line:"+str(lines)
# 			#wron=wron+1
# 			if line2[:-1].split(" ")[len(line2[:-1].split(" "))-1]==line[:-1]:
# 				two.append(lines)
# 				#print line2 
# 				#real=real+1
# 				#print str(line[:-1])+"== P"+str(line2[:-1])+" line:"+str(lines)
# 			else:
# 				wron=wron+1
# 				wrongl.append(lines)
# 				#print str(line[:-1])+"== P"+str(line2[:-1])+" line:"+str(lines)
# 	else:
# 		nulls1.append(lines)
# 		fake=fake+1

# # print real

# null1=0
# countw=0
# filer=open(sys.argv[1],"r")
# for line in filer.readlines():
# 	if "$$$$$" in line:
# 		countw=countw+1
# 		if countw in nulls1:
# 			#print "\n\n"+str(countw)+" "+line[:-1]
# 			if "@@@@@1 " in line:
# 				null1=null1+1
# 				nulls1.remove(countw)
# 			do=1	

# filer.close()
# countw=0
# dict3={}
# filer=open("test_1","r")
# filer1 =open("test_1_old","r")
# filer2=open("test_3","r")
# verbfile=open("verbs2","r")
# wrv=[]
# for line,line1,line2,verb in zip(filer.readlines(),filer1.readlines(),filer2.readlines(),verbfile.readlines()):
# 	countw=countw+1
# 	if countw in two:
# 		pass
# 		# print "##########"+str(countw)
# 		# print line.strip().split(" ")[:5] + line1.strip().split(" ")[:5]
# 		# print line2+"\n\n\n"
# 		if verb.strip() not in wrv:
# 			wrv.append(verb.strip())
# 		# if len(line.split(" "))>3:
# 		# 	temp=line[:-1].split(" ")
# 		# 	for i in range(0,len(temp)):
# 		# 		if temp[i] in dict3:
# 		# 			dict3[temp[i]]=dict3[temp[i]]+1
# 		# 		else:
# 		# 			dict3[temp[i]]=1

# 		# if '' in dict3:
# 		# 	del[dict3['']]
# 		# #print str(countw)+" "+ str(dict3)
# 		# dict3.clear()
# # print len(wrv)
# real= real+null1			
# print "correct= "+str(real)
# print "wrong= "+str(wron)
# #print "nulls= "+str(fake-null1)
# print "two= " +str(len(two))
# print "LeftNull:"+str(len(nulls1))
# filer.close()	
