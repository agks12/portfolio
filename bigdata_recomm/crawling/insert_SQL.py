import os
import json
import mysql.connector

#MySQL 연결 설정 자신sql정보 삽입
db_connection = mysql.connector.connect(
    host="",
    user="",
    password="",
    database=""
)
# 커서 생성
cursor = db_connection.cursor()

# 디렉토리 내의 모든 파일 목록 가져오기
with open('book_data.json', encoding='utf-8') as json_file:# 파일 위치
    data = json.load(json_file)


for index,now_data in enumerate(data):

    # 무조건 있는 정보들
    ISBN = now_data['ISBN']
    book_name = now_data['book_Name']
    book_category = now_data['book_category']
    author = now_data['author']
    publisher = now_data['publisher']
    publisher_date = now_data['publisher_date']
    score = now_data['score']
    img_url = now_data['img_url']
    description = now_data['description']

    # 있을 수도 있고 없을 수도 있는 정보들 하지만 검사안해도 됨 ""으로 들어가니까 아니지 안들어가야되나
    # 교과서 수록정보
    school_book_grade = now_data['school_book_grade']
    if school_book_grade=="":
        school_book_grade=0
    else:
        school_book_grade = int(now_data['school_book_grade'])

    # 쪽수 정보
    number_of_pages = now_data['number_of_pages']
    if number_of_pages=="":
        number_of_pages=None
    else:
        number_of_pages = int(now_data['number_of_pages'][:-1])

    # 시리즈
    series = now_data['serise']
    if series=="":
        series=None
    else:
        series = 1


    # 처리필요 정보
    grade = now_data['grade']
    grade_len = len(grade)

    grade_list = []
    if grade_len==0:
        grade_list.append(None)
    elif grade_len==2:
        grade_list.append(grade)
    elif grade_len==4:
        grade_list.append(grade[0:2])
        grade_list.append(grade[2:4])
    else:
        grade_list.append(grade[0:2])
        grade_list.append(grade[2:4])
        grade_list.append(grade[4:6])

    # 학년 개수별로 저장
    for val in grade_list:
        query = "INSERT INTO book_test.Book (ISBN,book_Name,grade,school_book_grade,book_category,series,number_of_pages,author,publisher,publisher_date,score,img_url,description) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) "  # name, thumbnail, url) VALUES (%s,%s, %s, %s, %s, %s)"
        values = (ISBN,book_name,val,school_book_grade,book_category,series,number_of_pages,author,publisher,publisher_date,score,img_url,description)
        cursor.execute(query, values)

# # 변경사항 저장 및 연결 종료
db_connection.commit()
cursor.close()
db_connection.close()
