adl_ls = [[1,2],[2,3],[1,3]]
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


