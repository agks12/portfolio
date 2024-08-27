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
      let sql = "SELECT * FROM recipe";
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          Recipe: results,
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
    // db.query 메소드가 제대로 정의되어 있는지 확인
    const recipeNum = req.params.recipe_id;
    if (db && typeof db.query === 'function') {
      let sql = `SELECT * FROM recipe where recipe_id = ${recipeNum}`;
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          Recipe_one: results,
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

router.get("/category/:category_id", async (req, res) => {
  try {
    // db.query 메소드가 제대로 정의되어 있는지 확인
    const categoryNum = req.params.category_id;
    if (db && typeof db.query === 'function') {
      let sql = `SELECT * FROM recipe where category_id = ${categoryNum}`;
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          recipe_category: results,
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
