import pandas as pd
import math

f = pd.read_table("1.022v_50000cps.txt", sep="\t", names=['a', 'b'])  # 카운터 시간정보
# print(f['b'])
q = list(f['b'])  # 시간행 만 추출
# print(q)
list_division = int(len(q)/150000)
#q.insert(0, 0)  # 처음 인덱스 0 삽입
q.insert(len(q), 0)  # 마지막 인덱스 0 삽입
for x in range(list_division):
    q.insert((150000*(x+1)),0)

oindex = list(filter(lambda x: q[x] == 0, range(len(q))))  # 0 들어간 인덱스 리스트
# print(oindex)
loop_number = len(oindex) - 1  # 리스트 분할횟수
print(len(oindex))
interval_length = 158

count_18_64 = []
for e in range(64):
    count_18_64.append(18)

hex_rotate = [18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,0,18,
                1,18,2,18,3,18,4,18,5,18,6,18,7,18,8,18,9,18,10,18,11,18,12,18,13,18,14,18,15,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,15,18,
                0,18,1,18,2,18,3,18,4,18,5,18,6,18,7,18,8,18,9,18,10,18,11,18,12,18,13,18,14,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,14,18,
                15,18,0,18,1,18,2,18,3,18,4,18,5,18,6,18,7,18,8,18,9,18,10,18,11,18,12,18,13,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,13,18,
                14,18,15,18,0,18,1,18,2,18,3,18,4,18,5,18,6,18,7,18,8,18,9,18,10,18,11,18,12,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,12,18,
                13,18,14,18,15,18,0,18,1,18,2,18,3,18,4,18,5,18,6,18,7,18,8,18,9,18,10,18,11,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,11,18,
                12,18,13,18,14,18,15,18,0,18,1,18,2,18,3,18,4,18,5,18,6,18,7,18,8,18,9,18,10,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,10,18,
                11,18,12,18,13,18,14,18,15,18,0,18,1,18,2,18,3,18,4,18,5,18,6,18,7,18,8,18,9,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,9,18,
                10,18,11,18,12,18,13,18,14,18,15,18,0,18,1,18,2,18,3,18,4,18,5,18,6,18,7,18,8,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,8,18,
                9,18,10,18,11,18,12,18,13,18,14,18,15,18,0,18,1,18,2,18,3,18,4,18,5,18,6,18,7,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,7,18,
                8,18,9,18,10,18,11,18,12,18,13,18,14,18,15,18,0,18,1,18,2,18,3,18,4,18,5,18,6,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,6,18,
                7,18,8,18,9,18,10,18,11,18,12,18,13,18,14,18,15,18,0,18,1,18,2,18,3,18,4,18,5,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,5,18,
                6,18,7,18,8,18,9,18,10,18,11,18,12,18,13,18,14,18,15,18,0,18,1,18,2,18,3,18,4,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,4,18,
                5,18,6,18,7,18,8,18,9,18,10,18,11,18,12,18,13,18,14,18,15,18,0,18,1,18,2,18,3,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,3,18,
                4,18,5,18,6,18,7,18,8,18,9,18,10,18,11,18,12,18,13,18,14,18,15,18,0,18,1,18,2,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,2,18,
                3,18,4,18,5,18,6,18,7,18,8,18,9,18,10,18,11,18,12,18,13,18,14,18,15,18,0,18,1,18,
                18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,1,18,
                2,18,3,18,4,18,5,18,6,18,7,18,8,18,9,18,10,18,11,18,12,18,13,18,14,18,15,18,0,18]   #16진수 회전

File = open('C:\\POST PROCESSING\\1.022v_50000cps_hex_158step.txt', 'w')
File_1 = open('C:\\POST PROCESSING\\1.022v_50000cps_bi_158step.txt', 'w')


for i in range(loop_number):
    a = list(q[oindex[i] + 1:oindex[i + 1]])  # 리스트 분할
    #print(a)
    print(i)
    k = 0
    z = 0
    count_array_all_f = []
    while sum((a[0:1] + ([(interval_length * (k + 1))]))) < sum(a[len(a) - 1:len(a)]):   #하위간격 나누기
        #print(sum((a[0:1] + [(interval_length * (k + 1))])))
        k += 1
        j = 0
        while sum(a[0:1] + [(interval_length * k)]) > sum(a[z:z+1]):                    #하위간격 광자수
            j += 1
            z += 1
        count_array_all_f.append(j)

    loop_number_1 = math.floor(len(count_array_all_f)/1024)

    count_array_all = count_array_all_f[0:loop_number_1*1024]

    for y in range(loop_number_1):
        count_array = count_array_all[1024*(y):1024*(y+1)]
        for m in range(1024):                                          #광자수 0,1그리고 2개이상 검사
            if count_array[m] == 0:
                count_array[m] = 18
            elif count_array[m] == 1:
                count_array[m] = 1
            else:
                count_array[m] = 19

        for l in range(16):                     #1개 간격에 2개이상 제거
            if (19 in count_array[64 * l:64 * (l + 1)]) == True:
                count_array[64 * l:64 * (l + 1)] = count_18_64
            else:
                pass

        for l in range(16):                     #64간격에 광자1개만 검출
            if sum(count_array[64 * l:64 * (l + 1)]) == 1135:
                count_array[64 * l:64 * (l + 1)] = count_array[64 * l:64 * (l + 1)]
                #print(len(count_array_2[64 * l:64 * (l + 1)]))
            else:
                count_array[64 * l:64 * (l + 1)] = count_18_64
                #print(len(count_array_2[64 * l:64 * (l + 1)]))

        for p in range(1024):
            if count_array[p] == 1:
                count_array[p] = hex_rotate[p]
                    #print(hex_rotate[p])
            else:
                count_array[p] = count_array[p]

        for t in  range(16):
            if sum(count_array[32+(t*64):64+(t*64)])-558 == 18:
                pass
            else:
                Hexadecimal = format((sum(count_array[32+(t*64):64+(t*64)])-558), 'x')
                Binary = format((sum(count_array[32+(t*64):64+(t*64)])-558), 'b').zfill(4)
                File.write(Hexadecimal)
                File_1.write(Binary)

File.close()
File_1.close()
