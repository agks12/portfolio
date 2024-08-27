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
      let sql = "SELECT * FROM step";
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          step: results,
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

router.get("/:recipe_id", async (req, res) => {
  try {
    const reciepNum = req.params.recipe_id;
    // db.query 메소드가 제대로 정의되어 있는지 확인
    if (db && typeof db.query === 'function') {
      let sql = `SELECT step_tip,cook_tool FROM step where recipe_id = ${reciepNum}`;
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          step_recipeid: results,
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

router.get("/:recipe_id/:step_id", async (req, res) => {
  try {
    const reciepNum = req.params.recipe_id;
    const stepNum = req.params.step_id;
    // db.query 메소드가 제대로 정의되어 있는지 확인
    if (db && typeof db.query === 'function') {
      let sql = `SELECT step_tip,cook_tool FROM step where recipe_id = ${reciepNum} and step = ${stepNum}`;
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          step_recipeid_stepid: results,
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
