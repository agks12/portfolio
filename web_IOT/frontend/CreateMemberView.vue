<template>
    <div class="survey-container">
      <h1 class="survey-title">선호하는 음식의 종류를 여러개 선택해주세요.</h1>
      <div v-for="(item, index) in items" :key="index" class="survey-item">
        <button
          :class="{ 'active': selectedItems[index] }"
          @click="toggleCheckbox(index)"
        >
          {{ item }}
        </button>
        </div>
    <!-- RouterLink를 버튼으로 스타일링 -->
        <router-link :to="{ name: 'servey_allerg' }">
            <button @click="submitSurvey" class="submit-button">Coutinue</button>
        </router-link>
    </div>
  </template>
  
  <script setup>
  //모바일스타트
  import axios from 'axios'; // Axios 라이브러리 가져오기
  import { ref } from 'vue';
  import { useRecipeStore } from "@/store/recipe";


  //const loginFlag = localStorage.getItem('loginFlag');
  //console.log("pwpwpwpwpwpwpwwpwp",loginFlag);


  const recipeStore = useRecipeStore();
  //console.log("카테 페이지",recipeStore.user_category);
  
  const items = ['양식', '중식', '한식', '일식', '비건', '육류'];
  const selectedItems = ref(Array(items.length).fill(false));
  
  const toggleCheckbox = (index) => {
    selectedItems.value[index] = !selectedItems.value[index];
  };

  const user_id =1;
  const user_profile=1;
  //Favorite_category

 const submitSurvey = () => {
    // 선택된 항목에 대한 카테고리 ID 배열
    const selectedCategoryIds = [];
    
    // 선택된 항목에 대한 카테고리 ID 추출
    items.forEach((item, index) => {
        if (selectedItems.value[index]) {
            const category = recipeStore.user_category.find(cat => cat.category === item);
            if (category) {
                selectedCategoryIds.push(category.category_id);
            }
        }
    });

    // 이미 선택된 항목인지 확인
    const existingFavorites = recipeStore.Favorite_category.filter(favorite => {
        return (
            favorite.user_id === user_id &&
            favorite.profile_id === user_profile &&
            selectedCategoryIds.includes(favorite.category_id)
        );
    });

    if (existingFavorites.length > 0) {
        // 이미 선택된 항목 중 하나라도 존재하면 해당 항목만 추가
        console.log("이미 선택된 항목이 있습니다.");
        const existingCategoryIds = existingFavorites.map(favorite => favorite.category_id);
        const newCategoryIds = selectedCategoryIds.filter(id => !existingCategoryIds.includes(id));
        if (newCategoryIds.length === 0) {
            // 이미 존재하는 항목만 선택한 경우
            console.log("이미 존재하는 항목만 선택되었습니다.");
            return;
        }
        // 이미 존재하는 항목 외의 항목을 추가
        selectedCategoryIds.length = 0;
        selectedCategoryIds.push(...newCategoryIds);
    }

    // Axios를 사용하여 POST 요청 보내기
    axios.post('http://localhost:5000/user/categoryFollow', {
        user_id: user_id,
        profile_id: user_profile,
        category_id: selectedCategoryIds
    })
    .then(response => {
        console.log('서버 응답:', response.data);
        //alert("선호도 조사 완료");
        // POST 요청 성공 시 수행할 작업 추가
    })
    .catch(error => {
        console.error('POST 요청 오류:', error);
        // POST 요청 실패 시 수행할 작업 추가
    });
};




</script>

<style scoped>
    body {
      margin: 0;
      padding: 0; /* 기본 padding도 제거합니다. */
      font-family: Arial, sans-serif;
    }

    .survey-container {
      width: 25%; /* 전체 화면을 사용합니다. */
      height: 100vh; /* 화면의 높이를 100%로 설정합니다. */
      display: flex;
      flex-direction: column; /* 내부 컨텐츠를 세로로 정렬합니다. */
      align-items: center; /* 가운데 정렬합니다. */
      justify-content: center; /* 가운데 정렬합니다. */
      text-align: center;
      padding: 2rm;
      border-radius: 10px;
      /* box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); */
      background-color: #D7CCC8;
      border: 0;
    }
    
    .survey-title {
      color: #040404;
      font-size: 0.7rem; /* 28px의 3배 */
      margin-bottom: 0.5px; /* 20px의 3배 */
      /* padding-bottom: 100px; */
    }
    
    .survey-item {
      margin-bottom: 15px;
    }
    
    button {
      width: 250px; /* 200px의 3배 */
      padding: 20px; /* 10px의 3배 */
      font-size: 1.5rem; /* 20px의 3배 */
      background-color: #EFEBE9;
      color: #1f1d1d;
      border: 2px solid #1f1d1d;
      border-radius: 14px;
      cursor: pointer;
      transition: background-color 0.3s, color 0.3s;
    }
    
    button.active {
      background-color: #795548;
      color: #fff;
      border-color: #795548;
    }
    
    .submit-button {
      width: 400px; /* 250px의 3배 */
      padding: 20px; /* 10px의 3배 */
      font-size: 50px; /* 25px의 3배 */
      background-color: #3E2723;
      color: #fff;
      border: none;
      border-radius: 14px;
      cursor: pointer;
      transition: background-color 0.3s;
      align-items: center; /* 세로 가운데 정렬 */
      justify-content: center; /* 가로 가운데 정렬 */
    }
    
    .submit-button:hover {
      background-color: #3E2723;
    }
</style>
