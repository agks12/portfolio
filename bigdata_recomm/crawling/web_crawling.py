from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service as ChromeService
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver import ActionChains
import pandas as pd
import json
import time

# Chrome 드라이버 경로 지정 (설치된 Chrome 버전과 일치해야 함)
driver_path = "C:\\Users\\SSAFY\\PycharmProjects\\pythonProject1\\chromedriver"

# 동화/명작/고전 -> 9개 장르
# 여기 쓰는 url은 각 장르 첫번째 페이지 url임 즉 이 코드는 한번 실행하면 해당 장르(예 가족이야기)에 해당하는 모든 책 추출함
# 만약 동화/명작/고전 으로 선택된 상태에서 url을 입력하면 한번 실행으로 9개 장르에 해당하는 모든 책을 크롤링 할 수 있지만 시간이 오래걸려서 중간에 끊어짐 없이 한번에 받기는 어려워서 장르별로 실행함
url = "https://www.aladin.co.kr/shop/wbrowse.aspx?BrowseTarget=List&ViewRowsCount=25&ViewType=Detail&PublishMonth=0&SortOrder=2&page=1&Stockstatus=1&PublishDay=84&CID=48877&SearchOption=&CustReviewRankStart=&CustReviewRankEnd=&CustReviewCountStart=&CustReviewCountEnd=&PriceFilterMin=&PriceFilterMax="

book_category = 6 # 분류번호
book_category_name = "외국창작동화" # 장르 이름

start_Time = time.time() # 걸린 시간 측정 시작점

# Chrome 브라우저 옵션 설정
options = webdriver.ChromeOptions()
options.add_argument("--headless")  # 창 없는 headless 모드로 실행하려면 주석 해제
options.add_argument("--disable-gpu")
options.add_argument("--no-sandbox")

# 크롬 드라이버 최신 버전 설정
service = ChromeService(executable_path=ChromeDriverManager().install())
driver = webdriver.Chrome(service=service, options=options)

driver.get(url) # url접속

#알라딘 사이트 분류창에서 젠체 페이지 번호 추출하기
numbox_last = driver.find_elements(By.CLASS_NAME, "numbox_last") # numbox_last클래스 이름 찾기
numbox_last_str = numbox_last[0].find_elements(By.TAG_NAME,"a") # numbox_last클래스 안의 a태그에 페이지 번호 있음
page_num_str = numbox_last_str[0].get_attribute("href") # numbox_last클래스 안의 a태그의 href안에 페이지 번호 있음
page_num_str_st = page_num_str.index("(") # ( 페이지 번호) 형태여서 "(",")"찾기
page_num_str_end = page_num_str.index(")")
Last_Page_Num = int(page_num_str[page_num_str_st+2:page_num_str_end-1])+1 # 전체 페이지 번호

table = [] # 전체 딕셔너리 저장할 리스트

next_page_list = [] #각 페이지 url저장할 리스트
#페이지 번호 저장하기
for i in range(1, Last_Page_Num):#Last_Page_Num):
    next_page_list.append("https://www.aladin.co.kr/shop/wbrowse.aspx?BrowseTarget=List&ViewRowsCount=25&ViewType=Detail&PublishMonth=0&SortOrder=2&page={}&Stockstatus=1&PublishDay=84&CID=48877&SearchOption=&CustReviewRankStart=&CustReviewRankEnd=&CustReviewCountStart=&CustReviewCountEnd=&PriceFilterMin=&PriceFilterMax=".format(i))

discript_error_list=[] # 책소개 없는책 저장할 리스트
pages_error_list=[] # 페이지쪽수 없는책 저장할 리스트
seris_error_list=[] # 시리즈 없는책 저장할 리스트
PageN=len(next_page_list) # 여기서 말하는 페이지 = 책 25개있는 한 페이지

# 페이지 개수 만큼 루프 돌기
book_cnt=0
for index, nowPageNum, in enumerate(next_page_list):
    print("pageNum ",index+1,"/",PageN) # 현재 페이지 나타내기

    driver.get(nowPageNum) # 현재 페이지 접속 하기
    naver_logos = driver.find_elements(By.CLASS_NAME, "bo3") # 현재 페이지의 책 목록 추출하기

    new_url = [] # 현재 페이지 책 상세 페이지 url저장할 리스트

    # 책 상세 페이지 url저장하기
    for URL in naver_logos:
        u = URL.get_attribute("href") # href가 url 정보
        new_url.append(u) # 리스트에 추가

    # 현재 페이지 책 하나씩 상세 페이지 들어가기
    for logo in new_url:
        book_cnt+=1
        print("book_num",book_cnt) # 전체 책 개수 나타내기
        driver.get(logo) # 현재 책 상세페이지 접속

        # 책소개 부분의 html은 동적이라서 스크롤 내려서 보여졌을 때 존재하므로 강제로 스크롤 내려서 봐야됨
        # 책소개 위치는 Ere_prod_mconts_box이름을 가진 마지막 클래스 아래에 존재하므로 먼저 Ere_prod_mconts_box클래스 모두 추출

        # 로그인 해야 접속 할 수 있는 페이지인지 검사 로그인 해야되면 접속 하지 않고 다음페이지로
        try:
            hegith = driver.find_elements(By.CLASS_NAME, "Ere_prod_mconts_box")
        except:
            print("로그인 해야 접속 할 수 있는 페이지")
            continue

        # Ere_prod_mconts_box 클래스 마지막 위치로 스크롤 내림
        action = ActionChains(driver)
        action.move_to_element(hegith[-1]).perform()

        # 현재 상태에서 800px정도 아래에 책소개 있으므로 800px만 아래로 스크롤 해서 책소개 html에 나타내기
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight+800);")

        # 완전히 로드되고 나서 html가져와야 될거 같은데 시간이 길면 더 잘되야되는데 안들어오는게 더많아짐?
        #time.sleep(0.3)

        # 현재 상세페이지의 책 정보중 일부가 json형태로 html에 나타나 있는데 이걸 이용하면 편하므로 가져오기
        scripts = driver.find_elements(By.CSS_SELECTOR, 'script[type="application/ld+json"]')
        # 아래 코드에서도 나타나지만 가져온 태그들은 같은 이름이 여러개 있으면 모두 갖고 오므로 scripts[0]처럼 몇번째 쓸건지 지정해야함
        script = scripts[0].get_attribute('innerHTML')

        # 웹페이지 json파일 형식에 오류 있는 경우가 있으면 그 페이지 크롤링 하지 않고 넘어가는 부분 /지금 방식은 모두 끝난후 저장하는 거라서 오류있어서 멈추면 지금까지 했던거 저장안됨
        try:
            json_data = json.loads(script) # json_data에 가져온 책정보json 저장
        except json.JSONDecodeError as e:
            print("Error decoding JSON:", e)
            continue

        # 위에서 저장한 책정보 json파일에서 아래 항목을 추출하면 아래항목은 추가 과정없이 데이터 추출 끝
        book_Name = json_data['name'] # 책 이름
        author = json_data['author']['name'] # 저자 이름들
        publisher = json_data['publisher']['name'] # 출판사
        publisher_date = json_data['workExample'][0]['datePublished'] # 출판일
        img_url = json_data['image'] # 책 이미지 url

        # 위에서 저장한 책 정보 json파일에 없는 항목들은 직접 태그 찾아서 가져와야함 아래 항목들이 해당함
        seris = driver.find_elements(By.CLASS_NAME, "Ere_sub1_title") # 책 시리즈 정보 가져오기 위한 태그
        score = driver.find_elements(By.CLASS_NAME, "Ere_sub_pink") # 책 평점 정보 가져오기 위한 태그
        pages = driver.find_elements(By.CLASS_NAME, "conts_info_list1") # 책 쪽수 정보 가져오기 위한 태그
        category = driver.find_elements(By.ID, "ulCategory") # 9개 장르 제외한 "학년별 교과서 수록 도서", "학년별 추천도서" 정보 가져오기 위한 태그
        book_script = driver.find_elements(By.CLASS_NAME, "Ere_prod_mconts_box") # 책 소개 가져오기 위한 태그
        book_script_flag = driver.find_elements(By.CLASS_NAME, "Ere_prod_mconts_LL") # 책 소개가 있는지 없는지 판단 하기 위한 태그

        # 바로 위 항목들에 해당 하는 변수 선언
        description = "" # 책 소개
        number_of_pages = "" # 책 쪽수
        seris_Name ="" # 책 시리즈
        ISBN = "" # 책 ISBN
        grade= "" # 추천 학년 정보
        school_book_grade="" # 교과서 수록도서 학년 정보

        if scripts: # 책 json파일이 없다면 책 상세 페이지가 아닌거라 가정하고 있을때만 책 정보 저장 시작

            # 아래 항목이름 if문들은 각 항목이 있을 때만 실행되도록 하는 코드(없을 때도 하면 오류)

            # 책 시리즈 정보 있을 때 실행
            if seris:
                for ser in seris: # 하지만 seris로 저장한 태그가 2개 이상인 경우 그중에서 시리즈 정보 가진 태그를 판별해야되므로 루프 돌려서 검사함
                    if ser.get_attribute("href"): # 시리즈 정보 가진 태그는 a 태그이고 href속성을 가지므로 만약 href태그 있으면 시리즈 정보 이므로 이때 시리즈 정보 저장
                        seris_Name = ser.text # 시리즈 정보 저장
                        break # 찾으면 바로 끝내기

            # 책 평점 정보 있을 때 실행
            if score:
                book_score = score[0].text # 책 평점 정보 태그는 무조건 1개 이므로 조건문만 만족하면 바로 평점 정보 저장

            # 책 쪽수 정보 있을 때 실행
            if pages:
                # 책 쪽수 정보는 큰 태그 안에 있는 li태그들 중 1개 이므로 루프로 1개씩 검사해서 "쪽수"에 해당하는 li태그 찾아야함
                # 또한 책 쪽수 정보 태그안에 ISBN도 있으므로 2가지 검사함
                pages_list = pages[0].find_elements(By.TAG_NAME,"li")
                for value in pages_list: # li태그 1개씩 검사
                    if value.text[-1]=="쪽": # 만약 태그의 마지막 텍스트가 "쪽"인 경우 페이지 정보이므로 쪽수 정보 저장
                        number_of_pages = value.text # 123쪽 전체형태로 저장
                    if value.text.find("ISBN")!=-1: # 만약 태그안에 ISBN이라는 텍스트 있으면 ISBN정보이므로 저장
                        ISBN = value.text[7:] # 전체 텍스트중에서 숫자 정보만 추출 (원래 형태 => ISBN : 000000000)

            #  교과서 수록도서,한년별 추천 도서 태그 있을 때 실행
            if category:
                category_list = category[0].find_elements(By.TAG_NAME,"li") # pages 경우처럼 태그 안에 li태그로 구분되어 있으므로 각각 검사
                for v in category_list: # li태그 1개씩 검사
                    # 초등 추천 도서는 해당값이 여러개 이면 모두 추천해야되므로 문자열에 계속 더하는 식으로 저장
                    if v.text.find("초등1~2학년")!=-1: # 초등 1,2학년 추천인 경우
                        grade+="12" # 기존 문자열에 "12"더하기
                    if v.text.find("초등3~4학년")!=-1: # 초등 3,4학년 추천인 경우
                        grade+="34" # 기존 문자열에 "34"더하기
                    if v.text.find("초등5~6학년")!=-1: # 초등 5,6학년 추천인 경우
                        grade+="56"  # 기존 문자열에 "56"더하기
                    if v.text.find("교과서 수록도서")!=-1: # 교과서 수록 도서인 경우
                        grade_index = v.text.index("학년") # 몇학년인지 찾아야되므로 문자열 검색으로 "학년" 찾고
                        school_book_grade = v.text[grade_index-1] # "학년" 글자 앞에 있는 값 저장

            # 책소개 태그 있는 경우
            if book_script:
                for index, script in enumerate(book_script): # 책소개 태그도 1개가 아니므로 무조건 루프 돌려서 검사필요
                    img_element = script.find_elements(By.TAG_NAME, "div") # 각 태그내부에 책소개가 위치하므로 내부 div태그 추출
                    if img_element[0].text == "책소개": #책 소개를 포함하는 태그안에는 "책소개" 텍스트를 가진 태그가 있으므로 조건문으로 검색
                        src = script.find_elements(By.CLASS_NAME, "Ere_prod_mconts_R") # "책소개를 가진  태그안에 Ere_prod_mconts_R라는 클래스 이름가진 태그에 내용있음
                        description = src[0].text # 책 소개 내용 추출
                        break # 발견하면 바로 루프 끝내기

            # 시리즈 정보 없는 책인 경우 기록하기 위해 리스트에 저장
            if seris_Name=="":
                seris_error_list.append(ISBN) # 시리즈 정보 없는 책의 ISBN을 저장

            # 책소개 정보 없는 책인 경우 기록하기 위해 리스트에 저장
            if description=="":
                print(ISBN)
                discript_error_list.append(ISBN)# 책소개 정보 없는 책의 ISBN을 저장

            # 책쪽수 정보 없는 책인 경우 기록하기 위해 리스트에 저장
            if number_of_pages=="":
                pages_error_list.append(ISBN)# 책쪽수 정보 없는 책의 ISBN을 저장

            # key-value로 리스트에 모든 책정보 저장
            dict = {"ISBN":ISBN,"book_Name":book_Name,"grade":grade,"school_book_grade":school_book_grade,"book_category":book_category, "serise":seris_Name, "number_of_pages":number_of_pages,"author":author, "publisher":publisher, "publisher_date":publisher_date, "score":float(book_score),"img_url":img_url, "description":description}
            table.append(dict)
        else:
            print("요소를 찾을 수 없습니다.") #책 상세 페이지 아닌 경우

# 모든 작업이 완료되었으므로 브라우저 닫기
driver.quit()

# 저장된 모든 책 정보 json파일로 저장
with open(book_category_name+".json", 'w', encoding='utf-8') as f:
    json.dump(table, f, ensure_ascii=False, indent=4)

end_Time = time.time() # 경과시간 측정 위한 종료시간

print(f"소요시간 {(end_Time - start_Time)/60:.1f} min",) # 경과 시간 출력

# 책소개, 책쪽수, 책시리즈 없는 애들 pd로 합치기
df1 = pd.DataFrame(discript_error_list, columns = ['discript']) # 책소개
df2 = pd.DataFrame(pages_error_list, columns = ['page']) # 책쪽수
df3 = pd.DataFrame(seris_error_list, columns = ['seris']) # 책 시리즈
df = pd.concat([df1,df2,df3],axis=1)

# 책소개, 책쪽수, 책시리즈 없는 애들 csv로 저장
df.to_csv(book_category_name+'.csv',encoding="cp949")
