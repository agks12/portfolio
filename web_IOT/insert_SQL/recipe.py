import os
import json
import mysql.connector

# MySQL 연결 설정 자신 mysql정보
db_connection = mysql.connector.connect(
    host="127.0.0.1",
    user="root",
    password="0000",
    database="cook_test"
)
# 커서 생성
cursor = db_connection.cursor()

directory_path = 'C:\\Users\\SSAFY\\Downloads\\recipes_file' # 파일 결로

# 디렉토리 내의 모든 파일 목록 가져오기 #폴더내에서 json파일 여러개 알아서 가져오기
json_files = [f for f in os.listdir(directory_path) if f.endswith('.json')]

category_id = 1 #외래키 profile_favorite_category테이블의 category_id에 존재하는 값만 사용가능
chef_id = 1 # 외래키 users_profile테이블의 profile_id에 존재하는 값만 사용가능 profile_id는 user테이블의 user_id에 존재하는 값만 사용가능

i=0
#폴더 내에 json파일 개수만큼 루프 돔
for json_file_name in json_files:
    # JSON 파일 경로
    json_file_path = os.path.join(directory_path, json_file_name)
    # JSON 파일 읽기
    with open(json_file_path, encoding='utf-8') as json_file:
        i+=1
        data = json.load(json_file)
        recipeFlowLen = len(data['recipe'])
        recipeImgLen = len(data['recipe_img'])
        LoopCnt = max(recipeFlowLen,recipeImgLen)

        # 테이블에 데이터 삽입 주의 테이블 이름 띄어쓰기 하면 ``으로 감싸야함
        # recipe 테이블에 데이터 삽입
        query = "INSERT INTO cook_test.recipe (category_id, `chef id`, name,thumbnail,url ) VALUES (%s, %s,%s,%s, %s) " #name, thumbnail, url) VALUES (%s,%s, %s, %s, %s, %s)"
        values = (category_id, chef_id,data['name'], data['thumbnail'], data['url'])
        cursor.execute(query, values)
        db_connection.commit() # 이거 해야 sql에 적용됨

        # recipe_id가 recipe_method, ingreient_list 테이블에 키로 들어가기 때문에 recipe테이블의 recipe_id를 찾음
        query = "select recipe_id from cook_test.recipe order by recipe_id DESC LIMIT 1"
        cursor.execute(query)
        result = cursor.fetchall()
        nowRecipeId = result[0][0]

        # recipe_method 테이블에 데이터 삽입
        for j in range(LoopCnt):
            if(j>=recipeFlowLen):
                nowRecipeFlow = ""
            else:
                nowRecipeFlow = data['recipe'][j]

            if(j>=recipeImgLen):
                nowRecipeImg=""
            else:
                nowRecipeImg = data['recipe_img'][j]

            query = "INSERT INTO cook_test.recipe_method (recipe_id, method,method_img ) VALUES (%s,%s, %s) "
            values = (nowRecipeId,nowRecipeFlow, nowRecipeImg)
            cursor.execute(query, values)

        # ingreient_list 테이블에 데이터 삽입
        ingredientLen = len(data['ingre_list'])
        for k in range(ingredientLen):
            nowIngredient = data['ingre_list'][k]['ingre_name'] + " " + data['ingre_list'][k]['ingre_count'] + data['ingre_list'][k]['ingre_unit']
            query = "INSERT INTO cook_test.ingredient_list (recipe_id, ingre_name) VALUES (%s, %s) "
            values = (nowRecipeId,nowIngredient)
            cursor.execute(query, values)

        # sql에 적용 (recipe_method+ingreient_list)
        db_connection.commit()

        print( int((i/10044)*100) )#진행률
# 변경사항 저장 및 연결 종료
db_connection.commit()
cursor.close()
db_connection.close()
