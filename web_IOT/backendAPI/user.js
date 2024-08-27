const express = require("express");
const cc = require("kleur");
const db = require("../database/dbConnection");
const router = express.Router();

router.use((req, res, next) => {
  console.log(cc.yellow("Request made to /user Route"));
  next();
});


router.get("/", async (req, res) => {
  try {
    //console.log(req.session.member); 
    // db.query 메소드가 제대로 정의되어 있는지 확인
    if (db && typeof db.query === 'function') {
      let sql = "SELECT * FROM User";
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          user: results,
        });
        console.log(cc.green("Users retrieved!"));
      });
    } else {
      throw new Error("db.query is not a function");
    }
  } catch (err) {
    console.error(err.message);
    res.status(500).send("Internal Server Error");
  }
});

router.get("/:id/:password", async (req, res) => {
  try {
    const id = req.params.id;
    const password = req.params.password;
    // db.query 메소드가 제대로 정의되어 있는지 확인
    if (db && typeof db.query === 'function') {
      let sql = `SELECT * FROM User where login_id = '${id}' and password = '${password}'`;
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          User: results,//이거가 allget 에 변수로감
        });
        console.log(cc.green("Users retrieved!"));
      });
    } else {
      throw new Error("db.query is not a function");
    }
  } catch (err) {
    console.error(err.message);
    res.status(500).send("Internal Server Error");
  }
});

// 회원가입하기
router.post('/insertUser', (req, res) => { // => 랑 function 이랑 같은 말이다 
  //const user_id = req.body.user_id;
  const UserData = req.body.User_loginData;
  // const password = req.body.password;
  // const username = req.body.username;
  // const email = req.body.email;
  // const age = req.body.age;
  // const phone_number = req.body.phone_number;
  // const is_superuser= req.body.is_superuser;
  // contact.ejs에서 받은 값을 req로 받아온다
  // body 대신 query로 받아오면 undefined 이라고 뜬다
  // 그런데 body 라고만 쓰면 실제로는 동작하지않는다.
  // body 로 처리하기 위해 express의 미들웨어를 사용해야 한다
  // body-parser 를 설치해야 한다
  var sql = `insert into User(login_id,password,username,email,age,phone_number,is_superuser)
  values(?,?,?,?,?,?,?)` // ? 를 통해 '' 와 같은 특수기호도 넣을 수 있다. DB에 특수기호가 보안상 이유로 잘들어가지지 않는다. 치환문 이용
  var values = [UserData.id,UserData.password,UserData.firstname+UserData.lastname,UserData.email,UserData.birthday,UserData.phonenumber,0]; // 위 ? 위치에 들어가는 배열 지정하기

  db.query(sql, values, function (err, result){
      if(err) throw err; 
      console.log('자료 1개를 삽입하였습니다.');
      res.send("<script> alert('문의사항이 등록되었습니다.'); location.href='/';</script>"); 
  })
})





//로그아웃 하기
router.get('/logout', (req, res) => {
  req.session.member = null; 
  res.send("<script> alert('로그아웃 되었습니다.'); location.href='/';</script>");          
})

// 프로필 등록하기
router.post('/insertProfile', (req, res) => {
  const profile_id = req.body.profile_id;
  const user_id = req.body.user_id;
  const profile_name = req.body.profile_name;
  const profile_nickname = req.body.profile_nickname;
  const profile_face = req.body.profile_face;
  const profile_img = req.body.profile_img;

  var sql = `insert into Users_Profile(user_id,profile_name,profile_nickname,profile_face,profile_img)
  values(?,?,?,?,?)`
  var values = [user_id,profile_name,profile_nickname,profile_face,profile_img];

  db.query(sql, values, function (err, result){
      if(err) throw err; 
      console.log('자료 1개를 삽입하였습니다.');
      res.send("<script> alert('문의사항이 등록되었습니다.'); location.href='/';</script>"); 
  })
})

// ---------------------------------------------
// 팔로우
router.post('/chefFollow', (req, res) => {
  const profile_id = req.body.profile_id;
  const user_id = req.body.user_id;
  const chef_id = req.body.chef_id;

  var sql = `insert into Profile_Follow(profile_id2,chef_id,user_id)
  values(?,?,?)`
  var values = [profile_id,user_id,chef_id];

  db.query(sql, values, function (err, result){
      if(err) throw err; 
      console.log('자료 1개를 삽입하였습니다.');
      res.send("<script> alert('문의사항이 등록되었습니다.'); location.href='/';</script>"); 
  })
})

// 레시피 즐겨찾기
router.post('/foodFollow', (req, res) => {
  const recipe_id = req.body.recipe_id;
  const profile_id = req.body.profile_id;
  const user_id = req.body.user_id;

  var sql = `insert into Profile_Favorite_Recipe(recipe_id,profile_id,user_id)
  values(?,?,?)`
  var values = [recipe_id,profile_id,user_id];

  db.query(sql, values, function (err, result){
      if(err) throw err; 
      console.log('자료 1개를 삽입하였습니다.');
      res.send("<script> alert('문의사항이 등록되었습니다.'); location.href='/';</script>"); 
  })
})

// 카테고리
router.post('/categoryFollow', (req, res) => {
  const categoryFllow_names = req.body.category_id; // 배열로 전송된다고 가정
  console.log(categoryFllow_names);
  const profile_id = 1;
  const user_id = 1;
  // 배열의 각 요소에 대해 반복하여 쿼리 실행
  categoryFllow_names.forEach(category_names => {
    var sql = `insert into Profile_Favorite_Category(category_id,profile_id,user_id) values(?,?,?)`;
    var values = [category_names,profile_id,user_id];

    db.query(sql, values, function (err, result) {
        if (err) throw err;
        console.log('선호카테고리 자료 1개를 삽입하였습니다.');
    });
  });
  res.send("<script> alert('문의사항이 등록되었습니다.'); location.href='/';</script>");
});



// 알러지 정보
router.post('/allergy', (req, res) => {
  const allergy = req.body.ingredients;
  const profile_id = req.body.profile_id;
  const user_id = req.body.user_id;
  console.log(allergy);
  // 배열의 각 요소에 대해 반복하여 쿼리 실행
  allergy.forEach(allery_data => {
    var sql = `insert into Profile_Allergy(ingredient_id,profile_id,allergy,user_id) values(?,?,?,?)`;
    var values = [allery_data.ingredient_id,profile_id,allery_data.allergy_name,user_id];
    db.query(sql, values, function (err, result) {
        if (err) throw err;
        console.log('알러지 자료 1개를 삽입하였습니다.');
    });
  });
  res.send("<script> alert('문의사항이 등록되었습니다.'); location.href='/';</script>");
});


// 선호재료
router.post('/ingredientFollow', (req, res) => {
  const ingredientFllow_names = req.body.ingredient_id; // 배열로 전송된다고 가정
  const profile_id = req.body.profile_id;
  const user_id = req.body.user_id;
  // 배열의 각 요소에 대해 반복하여 쿼리 실행
  ingredientFllow_names.forEach(ingredientFllow_name => {
      var sql = `INSERT INTO Profile_Favorite_Ingredient(ingredient_id, profile_id, user_id) VALUES (?, ?, ?)`;
      var values = [ingredientFllow_name, profile_id, user_id];

      db.query(sql, values, function (err, result) {
          if (err) throw err;
          console.log('선호 재료 1개를 삽입하였습니다.');
      });
  });
  res.send("<script> alert('문의사항이 등록되었습니다.'); location.href='/';</script>");
});





// 평점
router.post('/score', (req, res) => {
  const recipe_id = req.body.recipe_id;
  const profile_id = req.body.profile_id;
  const score = req.body.score;
  const user_id = req.body.user_id;

  var sql = `insert into Profile_Score(recipe_id,profile_id,scroe,user_id)
  values(?,?,?,?)`
  var values = [recipe_id,profile_id,score,user_id];

  db.query(sql, values, function (err, result){
      if(err) throw err; 
      console.log('자료 1개를 삽입하였습니다.');
      res.send("<script> alert('문의사항이 등록되었습니다.'); location.href='/';</script>"); 
  })
})


module.exports = router;
