import matplotlib.pyplot as plt

# 16진수난수 파일열고 data에 저장
t = open("D:\\C_ran\\test_16.txt", 'r')
data = t.read()
#print(data)
t.close()

# 복사
data_1M = data[:]

# 16진수 매치시킬 리스트
a = ["0","1","2","3","4","5","6","7","8","9","a", "b", "c", "d", "e", "f"]

of = open("D:\\C_ran\\test_16_hhh.txt", 'w')

# 파일에 16진수 각각 몇개인지 계산
l = []
for i in a:
    #print(i)
    c = data_1M.count(i)
    # of.write(str(c))
    l.append(c)

# 전체 개수 저장
s = sum(l)
print(l)
print(s)
of.write("length = "+str(s)+"\n")

# 개수를 확률로 변환
for i in range(16):
    pro = l[i]/s
    of.write(str(pro)+"\n")

# 그래프 그리기
plt.plot(a, l, color='red', marker='o', alpha=0.5, linewidth=2)
plt.show()

of.close()
