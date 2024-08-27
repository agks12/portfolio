import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
// import axios from 'axios'

export const useRecipeStore = defineStore('recipe', () => {
  const user_ingredients = ref([]); // 모든 식자재
  const user_ingredient_availble = ref([]); // 보유 식자재
  const user_recipe_ingredient = ref([]); // 레시피와 사용재료 같이있는 테이블
  const recipes = ref([]); // 모든 레시피 데이터
  const user_category = ref([]); //카테고리 테이블 모든 데이터
  const ingredients = ref([]);
  const useIngredient_recipe = ref([]) ;
  const score = ref([]); // 평점
  const Allergy = ref([]); // 알러지
  const Follow = ref([]); // 쉐프팔로우
  const Favorite_category = ref([]);
  const Favorite_ingredient = ref([]);
  const favorite_recipe = ref([]);
  //const expiration = ref([]); // 유통기한

  // 레시피 전체 데이터 객체
  const recipes_object  = computed(() => {
    const recipesById = {};
  
    // recipes 배열을 순회하면서 각 객체의 recipe_id를 키로 가지는 객체를 생성합니다.
    recipes.value.forEach(recipe => {
      recipesById[recipe.recipe_id] = recipe;
    });
  
    // 만들어진 객체를 반환합니다.
    return recipesById;
  });

  //servey에 필요한 재료목록 정의
  // 선호재료
  const ingredient_servey = computed(() => {
    return user_ingredients.value.map(item => ({
      ingredient_id: item.ingredient_id,
      ingredient_name: item.ingredient_name
    }));
  });



  // 추천 카테고리
  const recommend_category = ref([
    {id:1, title:"오늘의 추천", img:require("@/assets/image/today_2.png")}, // 랜덤
    {id:2, title:"실시간 인기 음식" , img:require("@/assets/image/popularity_2.png")}, // 실시간 인기 음식(좋아요 순)
    {id:3, title:"냉장고 재료 맞춤" , img:require("@/assets/image/refrigerator_2.avif")}, // 지금 냉장고에 있는 재료로
    {id:4, title:"사용자 맞춤" , img:require("@/assets/image/profile_2.png")}, // 선호설문결과로  
  ])


  //favorite_recipe userid로 
  const filteredFavorites = (userId) => computed(() => {
    return favorite_recipe.value.filter(item => item.user_id === userId);
  });


  //무작위 오늘의 추천
  // const category_1 = computed(() => {
  //   // 데이터를 복사하여 변경사항이 원본 데이터에 영향을 미치지 않음.
  //   const shuffledRecipes = [...recipes.value].sort(() => Math.random() - 0.5);
  //   // 무작위로 섞인 데이터를 5개씩 묶어 배열을 만듬.
  //   return Array.from({ length: Math.ceil(shuffledRecipes.length / 5) }, (value, index) =>
  //       shuffledRecipes.slice(index * 5, index * 5 + 5)
  //   );
  // });

  const category_1 = (userId, user_pw) => computed(() => {
    // Allergy 데이터에서 userId와 user_pw에 해당하는 ingredient_id 추출
    const allergyIngredientIds = Allergy.value.filter(item => item.user_id === userId && item.profile_id === user_pw)
        .map(item => item.ingredient_id);

    // user_recipe_ingredient 데이터에서 Allergy에 해당하는 ingredient_id에 해당하는 recipe_id 추출
    const filteredRecipeIds = user_recipe_ingredient.value
        .filter(item => allergyIngredientIds.includes(item.ingredient_id))
        .map(item => item.recipe_id);

    // Allergy에 해당하는 recipe_id를 제외한 레시피 필터링
    const filteredRecipes = recipes.value.filter(recipe => !filteredRecipeIds.includes(recipe.recipe_id));

    // 필터링된 레시피를 랜덤으로 정렬하여 반환
    const shuffledRecipes = filteredRecipes.sort(() => Math.random() - 0.5);

    // 랜덤으로 정렬된 레시피를 5개씩 묶어서 반환
    return Array.from({ length: Math.ceil(shuffledRecipes.length / 5) }, (_, index) => {
      const start = index * 5;
      const end = start + 5;
      const slicedArray = shuffledRecipes.slice(start, end);
      // 마지막 배열이 2개 이하의 요소를 가지고 있다면 버림
      if (slicedArray.length >= 3) {
          return slicedArray;
      }
    });
  });
  //console.log("category_1",category_1(1,1));

  //const ref_category_1 = ref([category_1]);

  //실시간 인기 음식(평점 (좋아요) 순)
  const category_2 = (userId, user_pw) => computed(() => {
    // Allergy 데이터에서 userId와 user_pw에 해당하는 ingredient_id 추출
    const allergyIngredientIds = Allergy.value.filter(item => item.user_id === userId && item.profile_id === user_pw)
        .map(item => item.ingredient_id);

    // user_recipe_ingredient 데이터에서 Allergy에 해당하는 ingredient_id에 해당하는 recipe_id 추출
    const filteredRecipeIds = user_recipe_ingredient.value
        .filter(item => allergyIngredientIds.includes(item.ingredient_id))
        .map(item => item.recipe_id);

    // Allergy에 해당하는 recipe_id를 제외한 레시피 필터링
    const filteredRecipes = recipes.value.filter(recipe => !filteredRecipeIds.includes(recipe.recipe_id));

    // recipe_score를 기준으로 내림차순으로 정렬
    const sortedRecipes = filteredRecipes.sort((a, b) => b.recipe_score - a.recipe_score);

    // 정렬된 데이터를 5개씩 묶어 배열을 만듭니다.
    return Array.from({ length: Math.ceil(sortedRecipes.length / 5) }, (_, index) => {
      const start = index * 5;
      const end = start + 5;
      const slicedArray = sortedRecipes.slice(start, end);
      // 마지막 배열이 2개 이하의 요소를 가지고 있다면 버림
      if (slicedArray.length >= 3) {
          return slicedArray;
      }
    });
  });
  //console.log("무작위 추천2",category_2(1,1));
  //const ref_category_2 = ref([category_2]);




  //추천 보유식자재 정렬함수
  //보유 재료 추천 userid로 가져오기
  // 사용자의 보유 식자재와 user_recipe_ingredient 데이터 간의 공통 ingredient_id찾기
  const ingredient_match = (userId, user_pw) => computed(() => {
    const allergyIngredientIds = Allergy.value
        .filter(item => item.user_id === userId && item.profile_id === user_pw)
        .map(item => item.ingredient_id);

    const userAvailableIngredients = user_ingredient_availble.value.filter(item => item.user_id === userId);

    // 알러지 재료를 가진 레시피의 recipe_id 모음
    const allergicRecipeIds = user_recipe_ingredient.value
        .filter(recipeIngredient => allergyIngredientIds.includes(recipeIngredient.ingredient_id))
        .map(recipeIngredient => recipeIngredient.recipe_id);

    // 사용자의 보유 식자재와 user_recipe_ingredient 데이터 간의 공통 ingredient_id찾기
    const commonRecipeIds = userAvailableIngredients.flatMap(ingredient => {
        // user_recipe_ingredient에서 현재 식자재의 ingredient_id와 일치하는 데이터를 필터링
        return user_recipe_ingredient.value.filter(recipeIngredient =>
            recipeIngredient.ingredient_id === ingredient.ingredient_id &&
            !allergyIngredientIds.includes(recipeIngredient.ingredient_id) &&
            !allergicRecipeIds.includes(recipeIngredient.recipe_id) // 알러지 재료를 가진 레시피 제외
        ).map(recipeIngredient => ({ id: recipeIngredient.recipe_id, expiration_data: ingredient.expiration_data }));
    });
  
    // 각 recipe_id 별로 중복 횟수와 expiration_data를 저장하여 객체에 저장
    const recipeIdCounts = commonRecipeIds.reduce((acc, { id, expiration_data }) => {
      acc[id] = acc[id] || { count: 0, expiration_data: [] };
      acc[id].count++;
      acc[id].expiration_data.push(expiration_data);
      return acc;
    }, {});
  
    // 중복 횟수가 같을 때 expiration_data가 낮은 것이 우선되도록 정렬
    const sortedRecipeCounts = Object.entries(recipeIdCounts).sort((a, b) => {
      if (Math.min(...a[1].expiration_data) <= 2 && Math.min(...b[1].expiration_data) <= 2) {
        // expiration_data가 2 이하인 경우 expiration_data 값이 작은 것이 우선순위가 되도록 정렬
        if (Math.min(...a[1].expiration_data) !== Math.min(...b[1].expiration_data)) {
          return Math.min(...a[1].expiration_data) - Math.min(...b[1].expiration_data);
        } else {
          return b[1].count - a[1].count; // 중복 횟수 내림차순 정렬
        }
      } else {
        // expiration_data가 3 이상인 경우 중복 횟수가 높은 것이 우선순위가 되도록 정렬
        if (a[1].count !== b[1].count) {
          return b[1].count - a[1].count; // 중복 횟수 내림차순 정렬
        } else {
          // 중복 횟수가 같을 때 expiration_data가 낮은 것이 우선되도록 정렬
          return Math.min(...a[1].expiration_data) - Math.min(...b[1].expiration_data);
        }
      }
    });

    // 정렬된 배열을 다시 객체로 변환하여 반환
    return sortedRecipeCounts.map(([id, { count, expiration_data }]) => ({ id, count, expiration_data }));
});

  //console.log("ingredient_match",ingredient_match(2,1));

  //보유식자재 기준 추천
  const category_3 = (userId,userPw)=> computed(() => {
    const recipeIds = ingredient_match(userId,userPw).value.map(item => item.id);
    // 레시피 ID를 이용하여 recipes 데이터에서 해당 레시피를 추출
    const matchedRecipes = recipeIds.flatMap(recipeId => {
      return recipes.value.filter(recipe => recipe.recipe_id == recipeId);
    });
  
    // 추출된 레시피를 5개씩 묶어서 반환
    return Array.from({ length: Math.ceil(matchedRecipes.length / 5) }, (_, index) => {
      const start = index * 5;
      const end = start + 5;
      const slicedArray = matchedRecipes.slice(start, end);
      // 마지막 배열이 2개 이하의 요소를 가지고 있다면 버림
      if (slicedArray.length >= 3) {
          return slicedArray;
      }
    });
  });
  //console.log("ingredientIds",category_3(2,1));


  //선호 카테고리,선호 재료추천 알러지 있으면 제외
  const category_4 = (userId, user_pw) => computed(() => {
    // Favorite_ingredient 데이터를 가져와서 userId와 user_pw에 해당하는 ingredient_id 추출
    const favoriteIngredientIds = Favorite_ingredient.value.filter(item => item.user_id === userId && item.profile_id === user_pw)
        .map(item => item.ingredient_id);

    // user_recipe_ingredient 데이터에서 favoriteIngredientIds에 해당하는 ingredient_id에 해당하는 recipe_id 추출
    const favoriteRecipeIds = user_recipe_ingredient.value.filter(item => favoriteIngredientIds.includes(item.ingredient_id))
        .reduce((acc, item) => {
            acc[item.recipe_id] = (acc[item.recipe_id] || 0) + 1;
            return acc;
        }, {});

    // Favorite_category 데이터에서 userId와 user_pw에 해당하는 category_id 추출
    const favoriteCategoryIds = Favorite_category.value.filter(item => item.user_id === userId && item.profile_id === user_pw)
        .map(item => item.category_id);

    // Allergy 데이터에서 userId와 user_pw에 해당하는 ingredient_id 추출
    const allergyIngredientIds = Allergy.value.filter(item => item.user_id === userId && item.profile_id === user_pw)
        .map(item => item.ingredient_id);

    // user_recipe_ingredient 데이터에서 Allergy에 해당하는 ingredient_id에 해당하는 recipe_id 추출
    const filteredRecipeIds = user_recipe_ingredient.value.filter(item => allergyIngredientIds.includes(item.ingredient_id))
        .map(item => item.recipe_id);

    // recipes 데이터에서 필터링 및 정렬
    const filteredRecipes = recipes.value.filter(recipe => {
        // favoriteRecipeIds에 해당하는 recipe_id가 있는지 확인하고, category_id가 favoriteCategoryIds에 있는지 확인
        return Object.prototype.hasOwnProperty.call(favoriteRecipeIds, recipe.recipe_id) && favoriteCategoryIds.includes(recipe.category_id)
            // Allergy에 해당하는 recipe_id가 없는지 확인
            && !filteredRecipeIds.includes(recipe.recipe_id);
    }).sort((a, b) => {
        // 중복 횟수를 기준으로 내림차순으로 정렬
        return favoriteRecipeIds[b.recipe_id] - favoriteRecipeIds[a.recipe_id];
    });

    const slicedRecipes = Array.from(
      { length: Math.ceil(filteredRecipes.length / 5) },
      (_, index) => {
          const start = index * 5;
          const end = start + 5;
          const slicedArray = filteredRecipes.slice(start, end);
          // 마지막 배열이 2개 이하의 요소를 가지고 있다면 버림
          if (slicedArray.length >= 3) {
              return slicedArray;
          }
      }
  );
  
  // 빈 배열 제거
  return slicedRecipes.filter(Boolean);
  
  });
  //console.log("processFavoriteData",category_4(1,1));

  //const userData = JSON.parse(localStorage.getItem("user_id"));
  let user_id = 1;
  //if(userData[0].user_id!=null)user_id = userData[0].user_id;
  // 추천 카테고리 별로 데이터 쪼개기

  console.log("user_id",user_id);

  const recommend_list = ref([
    {category : 1, recipe_list: category_1(user_id,1)},
    {category : 2, recipe_list: category_2(user_id,1)},
    {category : 3, recipe_list: category_3(user_id,1)},
    {category : 4, recipe_list: category_4(user_id,1)},
  ])

  const selected_category = ref(0)

  const currentRowSlide = ref(0)
  
  return { recipes, ingredients, user_ingredients,useIngredient_recipe,score,Allergy,Follow,Favorite_category,Favorite_ingredient,recommend_category, 
    selected_category,ingredient_servey,user_category,favorite_recipe,user_ingredient_availble,user_recipe_ingredient,currentRowSlide,filteredFavorites,category_1,category_2,category_3,category_4 
  , recipes_object,recommend_list}
  })
