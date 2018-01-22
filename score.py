
import sys
import collections
import os
fileo=open(sys.argv[1]+"/"+"final-input1","r")
dic1=[]
not_list1=[]
linenum=0
for line in fileo.readlines():
	linenum=+1
	if line in dic1:
		not_list1.append(linenum)
	else:
		dic1.append(line)

fileresult=open(sys.argv[1]+"result1_A3_OLD","r")
filew=open(sys.argv[1]+"test_1","w")
line_num=0
K_val=int(sys.argv[2])
name=""
dict1={}
rep =0.0
for line in fileresult.readlines():
	#print line
	line_num=line_num+1
	if line_num==1:
		continue
	if "#####" in line:
		name=line[:-1]
		continue 
	if "$$$$$" in line:
		dict2= collections.OrderedDict(reversed(sorted(dict1.items())))
		res=""
		co=0
	#	print dict2
		if len(dict2)>4:
			for k,v in dict2.iteritems():
				if co>K_val-1:
					break
				co=co+1
				#res=res+" "+v.split("-")[1]
				res=res+" "+v.split("-")[1]+":"+str(k)
			#print res
			filew.write(res+"\n")
		if len(dict2)>0 and len(dict2)<=4:
			for k,v in dict2.iteritems():
				if co>4:
					break
				co=co+1
				#res=res+" "+v.split("-")[1]
				res=res+" "+v.split("-")[1]+":"+str(k)
			filew.write(res+"\n")	
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

fileresult.close()
filew.close()

##########################
import operator
#fileresult=open("EXAMPLE_wiki_OLD","r")
fileresult=open(sys.argv[1]+"test_1","r")
filew = open(sys.argv[1]+"test_2","w")
dic={}
diccount={}
for line in fileresult.readlines():
	temp=line[:-1].split(" ")
	for i in range(0,len(temp)):
		if temp[i].split(":")[0] in dic:
			dic[temp[i].split(":")[0]]=dic[temp[i].split(":")[0]]+1
#			diccount[temp[i].split(":")[0]]= diccount[temp[i].split(":")[0]]+diccount[temp[i].split(":")[1]]
		else:
			dic[temp[i].split(":")[0]]=1
#			diccount[temp[i].split(":")[0]]=diccount[temp[i].split(":")[1]]
			
		if(1):
		  if temp[i].split(":")[0] in diccount:
		  	#if float(temp[i].split(":")[1])> float(diccount[temp[i].split(":")[0]]):
			diccount[temp[i].split(":")[0]]+= float(temp[i].split(":")[1])
		  else:
		    if temp[i]!="":
		    #print "####"+temp[i]+"#########"
		      diccount[temp[i].split(":")[0]]=float(temp[i].split(":")[1])
		  
		  
	if '' in dic and len(dic)>1:
		del dic['']

	d= sorted(dic.items(), key=operator.itemgetter(1),reverse=True)
	if len(d)>1:
		if d[0][1]==d[1][1]:
		#if 1:	
		  if diccount[(d[0][0])]>diccount[d[1][0]]:
		  	#print str(d[0][0])+" "+str(d[1][0])
		  	filew.write(str(d[0][0])+" "+str(d[1][0])+"\n")

		  else:
		 	if diccount[(d[0][0])]<diccount[d[1][0]]:
		 		#print str(d[1][0])+" "+str(d[0][0])
		 		filew.write(str(d[1][0])+" "+str(d[0][0])+"\n")
		  	else:
		  		#print "pass"
		  		filew.write("pass"+"\n")
		  		
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
fileresult.close()
filew.close()

########################3
fileresult=open(sys.argv[1]+"test_2","r")
testresult=open(sys.argv[1]+"verbs2","r")
real=0
fake=0
nulls1=[]
lines=0
wron=0
wrong=[]
two=[]
linenum_1=0
for line,line2 in zip(testresult.readlines(),fileresult.readlines()):
	linenum_1+=1
	if line_num in not_list1:
	  continue
	if "None" in line:
		lines=lines+1
		continue
	lines=lines+1
	if line2[:-1]!='':
		if line2[:-1].split(" ")[0].strip()==line[:-1].strip():
			real=real+1
			#print line2[:-1].split(" ")[0].strip(),line[:-1].strip()
		else:
			#print str(line[:-1])+"== P"+str(line2[:-1])+" line:"+str(lines)
			#wron=wron+1
			if line2[:-1].split(" ")[len(line2[:-1].split(" "))-1].strip().split("-")[0]==line[:-1].strip().split("-")[0]:
				two.append(lines)
				#print line2 
				#real=real+1
				#print str(line[:-1])+"== P"+str(line2[:-1])+" line:"+str(lines)
			else:
				# print "wwwwwwwwwwwwwwwwwwww"+str(lines)
				# print line2[:-1].split(" ")[len(line2[:-1].split(" "))-1].strip().split("-")[0],line[:-1].strip().split("-")[0]
				wron=wron+1
				wrong.append(lines)
				#print str(line[:-1])+"== P"+str(line2[:-1])+" line:"+str(lines)
	else:
		nulls1.append(lines)
		fake=fake+1

print real

null1=0
countw=0
filer=open(sys.argv[1]+"result1_A3_OLD","r")
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
print real
fileres=open(sys.argv[1]+"result","w")			
fileres.write("correct= "+str(real)+"\n")
fileres.write("wrong= "+str(wron)+"\n")
fileres.write("two= " +str(len(two))+"\n")
fileres.write("LeftNull:"+str(len(nulls1))+"\n")
fileres.write("Null:"+str((nulls1))+"\n")
#print wrong
fileres.close()
# print "#        Top 1: "+ str(round(real*100/float(real+wron+len(two)+len(nulls1)),2))+"        #"
# print "#        Top 2: "+ str(round((real+len(two))*100/float(real+wron+len(two)+len(nulls1)),2))+"        #"
#print nulls1
filer.close()	
# os.remove("test_1")
# os.remove("test_2")
########################################################################################################################################################################################################################################

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
