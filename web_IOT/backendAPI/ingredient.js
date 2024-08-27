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
    // db.query 메소드가 제대로 정의되어 있는지 확인
    if (db && typeof db.query === 'function') {
      let sql = "SELECT * FROM ingredient";
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          ingredient: results,
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

router.get("/:ingredient_id", async (req, res) => {
  try {
    const ingredientNum = req.params.ingredient_id;
    // db.query 메소드가 제대로 정의되어 있는지 확인
    if (db && typeof db.query === 'function') {
      let sql = `SELECT * FROM ingredient where ingredient_id = ${ingredientNum}`;
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          ingredient_one: results,
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

module.exports = router;
