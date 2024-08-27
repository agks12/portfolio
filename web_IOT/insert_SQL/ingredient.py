import os
import json
import mysql.connector

# MySQL 연결 설정 자신sql정보 삽입
db_connection = mysql.connector.connect(
    host="",
    user="",
    password="",
    database=""
)
# 커서 생성
cursor = db_connection.cursor()

# 디렉토리 내의 모든 파일 목록 가져오기
with open('C:\\Users\\SSAFY\\Downloads\\ingredient.json', encoding='utf-8') as json_file:# 파일 위치
    data = json.load(json_file)
    # JSON 파일 읽기
for i in range(len(data)):#재료 개수 만큼
        arr = data[i]["trim"] #손질법,보관법,조리법,구입요령있는 배열
        name = data[i]["name"] # 재료 이름
        rndlq = "" # 구입요령 저장할 변수
        ths = "" # 손질법 저장할 변수
        wh = "" # 조리법 저장할 변수
        qh = "" # 보관법 저장할 변수

        # prim 배열 없는경우 고려
        if(len(arr)==0):# prim배열 없을때 그냥 ""넣기
            query = "INSERT INTO cook.ingredient (ingredient_name,prep_ingredients,storage_method,purchase_tips,ingredient_recipe) VALUES (%s,%s,%s, %s,%s) " #name, thumbnail, url) VALUES (%s,%s, %s, %s, %s, %s)"
            values = (name,"","","","")
        else:
            # prim배열 크기는 0이 아니지만 1개이상~3개이하인 경우
            for i in range(len(arr)):
                if(arr[i][0]=="구"):
                    rndlq = arr[i][6:] # 구입요령 저장
                if (arr[i][0] == "손"):
                    ths = arr[i][5:] # 손질법 저장
                if(arr[i][0]=="조"):
                    wh = arr[i][5:] # 조리법 저장
                if(arr[i][0]=="보"):
                    qh = arr[i][5:] # 보관법 저장
            query = "INSERT INTO cook.ingredient (ingredient_name,prep_ingredients,storage_method,purchase_tips,ingredient_recipe) VALUES (%s,%s,%s, %s,%s) " #name, thumbnail, url) VALUES (%s,%s, %s, %s, %s, %s)"
            values = (name,ths,qh,rndlq,wh)

        cursor.execute(query, values)

# 변경사항 저장 및 연결 종료
db_connection.commit()
cursor.close()
db_connection.close()
