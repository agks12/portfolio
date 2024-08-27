import pandas as pd
import math
import numpy as np

data= pd.read_table('ts.txt') # 인풋데이터읽기
result = ' '.join(map(str, data)) # 문자열변환

# 위 문자열 리스트로 만들기
bi = [] 
for i in range(len(result)):
    one = result[i:i+1]
    bi.append(one)

length = len(bi)
raw = bi
raw_f = list(map(int, raw))
average = np.mean(raw_f)

#분모 variance
var = []    
# 분모값 계산
for x in range(length):
    vara = np.power(((sum(raw_f[x:x+1]))-average),2)
    var.append(vara)
covar = sum(var)

lag_len = 500 #지연샘플개수(최대개수는 시퀀스길이-1)

hcova = [] # 결과값 저장 리스트
# 분자 auto covariance
# 분자값 계산
for x in range(lag_len):
    h = []
    print(x)
    for i in range(length-x):
        hva = (((sum(raw_f[i:i+1]))-average)*((sum(raw_f[i+x:i+x+1]))-average)) # raw_f[i:i+1]이거 그냥i번 인덱스 선택하는 부분
        h.append(hva)
    # 분모/분자 계산
    r1 = sum(h)/covar # 결과
    hcova.append(r1)

per95 = 1.959964/math.sqrt(length) # 신뢰구간 값(실제는 아래처럼 자기상관값으로 구하지만 거의 비슷함)
print(per95)

# 95% 신뢰 구간
confidence_interval = []    
# 신뢰구간 계산
for x in range(lag_len):
    sq = 1.959964*np.sqrt((1+2*np.sum(np.power(hcova[0:x+1],2)))/length)
    confidence_interval.append(sq)

co_str_list = list(map(str,hcova)) #자기상관 값
co_confidence_list = list(map(str,confidence_interval)) # 신뢰구간 값

with open('C:\\POST PROCESSING\\1.8v_158step_auto.txt', 'w+') as lf:
    lf.write('\n'.join(co_str_list))

with open('C:\\POST PROCESSING\\test1_auto_compare.txt', 'w+') as lf:
    lf.write('\n'.join(co_confidence_list))
