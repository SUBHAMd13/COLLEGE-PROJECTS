def reduct_generation(adl_ls):
	reduct = []

	for cnt in range(len(adl_ls)):
		print("STEP :",cnt+1)
		ci = adl_ls[cnt]
		
		si = []
		temp = []
		
		ti = []
		mini = []
		
		copy_reduct = reduct.copy()

		if(cnt == 0):
			for i in ci:
				tp = []
				tp.append(i)
				ti.append(tp)
			print(f"S{cnt+1} :",si)
			print(f"T{cnt+1} :",ti)

		else:

			# Operation for Si
			for r in copy_reduct:
				if(set(r).intersection(set(ci))):
					si.append(r)
					
				else:
					temp.append(r)
					
			print(f"S{cnt+1} : {si}")	
			# print(f"temp = {temp}")

			# Operation for Ti
			for l in temp:
				for k in ci:
					tp1 = l.copy()
					tp1.append(k)
					ti.append(tp1)		

			print(f"T{cnt+1} : {ti}")

		#Operation for MINi
		for p in ti:
			re = 0
			tp2 = []
			while(re <= cnt):
				
				intersect = list(set(p).intersection(set(adl_ls[re])))
				if(len(intersect) == 1):
					tp2.extend(intersect)

				re += 1
			if(tp2 == p):
				mini.append(p)
		print(f"Min{cnt+1} :",mini)

		reduct.clear()
		reduct.extend(si)
		reduct.extend(mini)
		print(f"Reduct{cnt+1} :",reduct)



def sorted_absorption_law_apply(ls):
	ls.append('N')
	i = 0
	while(ls[i] != 'N'):
		j = i+1
		while(ls[j] != 'N'):
			if(set(ls[i]).issubset(set(ls[j]))):
				ls.remove(ls[j])
				j -=1
			j +=1
		i +=1
	ls.remove('N')
	
	print("After applying absorption law and sorting in ascending order:")
	print(ls)          #sorted ADL
	print()

	reduct_generation(ls)




def discernibility_mat(mat, row, col):
	entry_list = []

	print("Discernibility matrix:")
	
	mat1 = [["N"]*row]*row
	for i in range(row-1):
		for j in range(i+1,row):
			temp=[]
			if(mat[i][col-1] != mat[j][col-1]):
				for k in range(col-1):
					
					if(mat[i][k] != mat[j][k]):
						temp.append(k)
			else:
				temp.append("N")

			mat1[i][j] = temp
			
			print (mat1[i][j],end=" ")              #discernibility matrix
			
			if(mat1[i][j] != ["N"]):
				entry_list.append(mat1[i][j])
		print()
	
	entry_list.sort(key = len)
	
	print()
	print("All element of matrix:")
	print(entry_list)      
	print()         
	
	sorted_absorption_law_apply(entry_list)



fl=open('user.txt','r')

mat_dim = list(map(int,fl.readline().rstrip().split()))
mat = [list(map(int, fl.readline().rstrip().split())) for j in range(mat_dim[0])]

discernibility_mat(mat, mat_dim[0], mat_dim[1])
