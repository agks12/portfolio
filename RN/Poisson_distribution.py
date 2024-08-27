import pandas as pd
import math
from collections import Counter
import numpy as np
import scipy


file_name_list = ["0.666v_5000cps", "0.695v_10000cps","0.749v_50000cps","0.775v_0.1Mcps","0.802v_0.2Mcps"] # 검사할 파일 목록
interval_length_list = [100000]#1282051 # 검사한 단위 칸 길이
sample_count = 65535 # 검사할 값 제한 위한 설정

# 각 파일 돌아거면서 검사
for file_name in file_name_list:
    for interval_length in interval_length_list:
        
        lim = interval_length*sample_count # 한계값 설정
        def ture(x):
            return x <= lim
        f = pd.read_table("D:\\Time_tag\\pin_2_18bias\\%s.txt"%file_name, sep="\t", names=['a', 'b'])  # 카운터 시간정보
        # print(f['b'])
        q1 = list(f['b'])  # 시간행 만 추출
        q = list(filter(ture, q1))
        # print(q)
        list_division = int(len(q)/150000) # 한번에 검사하면 많아서 나누기
        # print(list_division)
        q.insert(0, 0)  # 처음 인덱스 0 삽입
        q.insert(len(q), 0)  # 마지막 인덱스 0 삽입
        for x in range(list_division):
            q.insert((150000*(x+1)),0)

        oindex = list(filter(lambda x: q[x] == 0, range(len(q))))  # 0 들어간 인덱스 리스트
        # print("oindex", oindex)
        loop_number = len(oindex) - 1  # 리스트 분할횟수
        #print(len(oindex))

        for i in range(loop_number):
            a = list(q[oindex[i] + 1:oindex[i + 1]])  # 리스트 분할
            #print(a)
            #print(i)
            k = 0
            z = 0
            count_array_all_f = []
            while sum((a[0:1] + ([(interval_length * (k + 1))]))) < sum(a[len(a) - 1:len(a)]):   #하위간격 나누기
                # print(sum((a[0:1] + [(interval_length * (k + 1))])), sum(a[len(a) - 1:len(a)]))
                k += 1
                j = 0
                while sum(a[0:1] + [(interval_length * k)]) > sum(a[z:z+1]):                    #하위간격 광자수
                    # print("a[0:1]", a[0:1], "interval_length*k", interval_length*k, "a[z:z+1]", a[z:z+1])
                    j += 1
                    z += 1
                count_array_all_f.append(j)

        # print(count_array_all_f)

        count_sort = sorted(count_array_all_f)

        count = Counter(count_sort)

        #print(count)

        count_values = count.values()  # 칸 개수
        count_keys = count.keys()      # 광자 수

        #print(count_values)
        #print(count_keys)

        count_values_list = list(count_values)   # 빈(bin) 개수(리스트만)
        count_keys_list = list(count_keys)       # 광자 수(리스트만)


        #print(count_values_list)
        print(count_keys_list)


        SUM = sum(count_values_list)  # 총 칸 개수
        print(SUM,'ds')

        x_axis_length = len(count_keys_list)    # x_axis_length = 자유도
        print(x_axis_length,'lensd')

        photon_list = []
        for i in range(x_axis_length):
            photon_list.append(count_values_list[i]*count_keys_list[i]) # 각 광자 수 계산하기

        photon_sum = sum(photon_list) # 총 광자 수
        #print(photon_sum,'ps')

        average_photon = photon_sum/SUM # 평균 광자 수
        print(average_photon)

        count_real_distribution = []
        for i in range(x_axis_length):
            count_real_distribution.append((count_values_list[i])) #관측된 광자 수 분포  #/SUM)) 관측된 광자 수 분포 확률(측정된)

        print(count_real_distribution)

        real_probability = []
        for i in range(x_axis_length):
            real_probability.append((count_values_list[i])/SUM) # 실제값

        print(real_probability)

        expected_probability = []
        for i in range(x_axis_length):
            expected_probability.append((np.exp(-average_photon)*average_photon**i)/math.factorial(i))

        print(expected_probability)

        count_expected_distribution = []
        for i in range(x_axis_length):
            count_expected_distribution.append(expected_probability[i]*SUM)  # poisson확률로 구한 광자 수 분포

        print(count_expected_distribution)

        chi_square_list = []
        for i in range(x_axis_length):
            chi_square_list.append(((count_real_distribution[i]-count_expected_distribution[i])**2)/count_expected_distribution[i]) # 카이제곱 리스트 구하기
        chi_square = sum(chi_square_list)

        p_value = 1-scipy.stats.chi2.cdf(chi_square, x_axis_length-1)
        print(p_value)


        of = open("D:\\poisson_data\\test\\%s.csv"%file_name,'a')

        of.write("간격,{0}\n총칸개수,{1}\n자유도,{2}\n평균_광자수,{3}\np-value,{4}\n\n".format(interval_length,SUM,x_axis_length,average_photon,p_value) )

        of.write("photon_count,observation,expectation\n")

        c = 0
        for i in range(len(count_keys_list)):
            if c == count_keys_list[i]:
                of.write("{0},{1},{2}\n".format(count_keys_list[i],real_probability[i],expected_probability[i]))
            else:
                space = count_keys_list[i] - c
                for j in range(space):
                    of.write("0,0,0\n")
                of.write("{0},{1},{2}\n".format(count_keys_list[i], real_probability[i], expected_probability[i]))
                c += space
            c += 1

        of.write("-------------------------------------------------------------\n")
        of.close()
