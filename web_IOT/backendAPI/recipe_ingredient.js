const express = require("express");
const cc = require("kleur");
const db = require("../database/dbConnection");
const router = express.Router();

router.use((req, res, next) => {
  console.log(cc.yellow("Request made to /recipe Route"));
  next();
});

router.get("/", async (req, res) => {
  try {
    // db.query 메소드가 제대로 정의되어 있는지 확인
    if (db && typeof db.query === 'function') {
      let sql = "SELECT * FROM recipe_ingredientunit";
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
            recipe_ingredientunit: results,
        });
        console.log(cc.green("recipe retrieved!"));
      });
    } else {
      throw new Error("db.query is not a function");
    }
  } catch (err) {
    console.error(err.message);
    res.status(500).send("Internal Server Error");
  }
});


router.get("/:recipe_id", async (req, res) => {
    try {
     const recipeNum = req.params.recipe_id;
      // db.query 메소드가 제대로 정의되어 있는지 확인
      if (db && typeof db.query === 'function') {
        let sql = `SELECT * FROM recipe_ingredientunit where recipe_id = ${recipeNum}`;
        db.query(sql, (err, results) => {
          if (err) throw err.message;
          res.json({
              recipe_ingredientunit_recipe_id: results,
          });
          console.log(cc.green("recipe retrieved!"));
        });
      } else {
        throw new Error("db.query is not a function");
      }
    } catch (err) {
      console.error(err.message);
      res.status(500).send("Internal Server Error");
    }
  });


router.get("/UseRecipe/:ingredient_id", async (req, res) => {
    try {
     const ingredientNum = req.params.ingredient_id;
      // db.query 메소드가 제대로 정의되어 있는지 확인
      if (db && typeof db.query === 'function') {
        let sql = `SELECT * FROM recipe_ingredientunit where ingredient_id = ${ingredientNum}`;
        db.query(sql, (err, results) => {
          if (err) throw err.message;
          res.json({
              recipe_ingredientunit_useRecipe: results,
          });
          console.log(cc.green("recipe retrieved!"));
        });
      } else {
        throw new Error("db.query is not a function");
      }
    } catch (err) {
      console.error(err.message);
      res.status(500).send("Internal Server Error");
    }
  });

module.exports = router;
