from docx import Document
import pandas as pd
import math

docx_path1 = '동백꽃_봄봄_형태소_분석_결과_카운팅포함.docx'
docx_path2 = '심청전_형태소_분석_결과_카운팅포함.docx'
docx_path3 = '타임 머신 The Time Machine_형태소_분석_결과_카운팅포함.docx'
docx_path4 = '홍길동전_형태소_분석_결과_카운팅포함.docx'
docx_path5 = '감자_형태소_분석_결과_카운팅포함.docx'
docx_path6 = '어린왕자_형태소_분석_결과_카운팅포함.docx'

path_list = [docx_path1, docx_path2, docx_path3, docx_path4, docx_path5, docx_path6] # 파일 리스트
N = len(path_list) # 계산에 필요한 n(총 문서수)

doc_per_word = {} # 문서별로 존재하는 단어 정리
all_word_list ={} # 모든 문서의 모든 단어를 정리(단일) 키:값 쌍으로 저장하는데 value는 "등장한 문서수"

# 문서별로 단어수, 전체 단어목록 계산
for index,Docx in enumerate(path_list): # 파일 개수만큼
    docName = path_list[index] # 파일이름
    doc = Document(Docx) # 파일 실행같은 함수
    word = {} # 지금 문서에서 나오는 단어:개수 쌍(문서에 단어:개수있음) 저장
    for paragraph in doc.paragraphs: # 한줄 씩 읽기?
        text = paragraph.text # 한줄의 모든 글자
        separation_index = text.find(":") # : 기준으로 뒤는 단어 앞은 개수
        text_key = text[:separation_index] # 단어
        text_count = text[separation_index+2:] # 개수
        word[text_key] = text_count # word 딕셔너리에 단어 : 개수 쌍 저장

        if text_key not in all_word_list: # 만약 모든 단일 단어 저장하는 리스트에 지금 단어 없으면 추가 있으면 건너뜀(단일로 하므로)
            all_word_list[text_key] = [] # 없으면 먼저 리스트 생성
            all_word_list[text_key].append(docName)  # 후 단어 저장
        else: # 만약 1번 문서에서 "option" 이란 단어가 처음 나오면 위 코드 실행되고 "option" : [문서1]으로 저장됨 후 문서2 에서도 나오면  "option" : [문서1,문저2]됨
            if docName not in all_word_list[text_key]: # 만약 지금 단어가 이 문서에서 처음 등장하는 거면 문서이름 리스트에 저장
                all_word_list[text_key].append(docName) # 문서 이름 저장

    # 이 값이 tf가 됨
    doc_per_word[docName] = word # 지금 계산한 문서에서 나온 모든 단어:개수 쌍을 딕셔너리에 하위로 추가 즉 doc_per_word 딕셔너리는 파일 개수 만큼의 키를 가지고 각 키 아래에 또 단어가 존재

# idf 계산
for key,value in all_word_list.items(): # 모든 단어 : 등장 문서 수 인 딕셔너리 이용
    dft = len(value) # df(t) : 특정 단어 t가 등장한 문서의 수.
    idf = math.log( N/(1+dft) ) #  idf(t) : df(t)에 반비례하는 수.
    all_word_list[key] = idf # idf

table_doc_word = pd.DataFrame(columns=list(all_word_list.keys()), index=path_list) # 모든 문서의 단어에 대한 tf-idf 계산하기 위해 만든 테이블

# if_idf 계산
for D_key,D_value in doc_per_word.items(): # 문서개수 만큼 돔 각 D-value는 문서에 존재하는 단어:개수 쌍
    for key,value in D_value.items(): # 단어 : 개수 쌍을 1개 씩 봄
        tf_idf = float(value) * all_word_list[key] # 지금 단어의 tf-idf계산
        row = table_doc_word.loc[D_key] # 테이블에서 지금 문서 열을추출
        row[key] = tf_idf # 열(단어)중 단어 이름이 지금 단어인 경우 값 삽입

pd_to_dict = table_doc_word.to_dict("index") # 정렬하기 위해 다시 딕셔너리로 변환
for DOCX_index, DOCX_value in pd_to_dict.items():# 문서 만큼 돔
    # 딕셔너리의 복사본을 만들어서 원본을 수정
    copy_dict = DOCX_value.copy() # 복사본 만들기
    for sub_key, sub_value in copy_dict.items(): # 각 문서의 각 단어들을 검사하면서 nan인 값인경우(먼가 정렬에 영향미치는듯) -1000으로 제일 하위순서로 되게 값 지정 //
        if pd.isnull(sub_value): # nan 인경우 pd.isnulll(값) 이나 np.isnan(값) 사용 sub_valus == np.isnan 안됨
            DOCX_value[sub_key] = -1000 # 값 변경

# nan값때매 이상한거였음?
# 각 문서의 단어를 중요도 내림차순으로 정렬
for index, tf_docx in pd_to_dict.items(): # 각 문서
    sorted_items = sorted(tf_docx.items(),key=lambda x: x[1],reverse=True) # 각 문서의 단어 딕셔너리(단어:중요도) 를 중요도 기즈ㅜㄴ으로 내림차순 정렬 => 튜플 리스트로 변환됨
    sorted_dict = {key: value for key, value in sorted_items} # 다시 딕셔너리로 변환
    pd_to_dict[index] = sorted_dict # 지금 문서 딕셔러리 정렬된 값으로 변경

result = pd.DataFrame()# 결과 테이블
# 정렬된 모든 단어 : 중요도 딕셔너리로 테이블 만들기
for index, (doc_name,values) in enumerate(pd_to_dict.items()): # 문서 수만큼 돔
    # 행에 입력되는 - 종류는 같으므로 중복되지 않게 이름으로 구분
    result["TF-IDF"+str(index)] = list(values.values()) # 정의한 테이블의 중요도 행 정의
    result["word"+doc_name] = list(values.keys()) # 정의한 테이블의 단어 이름 정의

print(result) # 결과 테이블 출력
result.to_csv("result_tf-idf.csv",encoding="cp949") # 결과 테이블 저장
