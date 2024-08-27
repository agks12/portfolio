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
      let sql = "SELECT * FROM stepofstep";
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          stepofstep: results,//이거가 allget 에 변수로감
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
    const recipeNum = req.params.recipe_id;
    // db.query 메소드가 제대로 정의되어 있는지 확인
    if (db && typeof db.query === 'function') {
      let sql = `SELECT step, stepofstep_tip, tip_img FROM stepofstep where recipe_id = ${recipeNum}`;
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          stepofstep_recipeId: results,//이거가 allget 에 변수로감
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
    const recipeNum = req.params.recipe_id;
    const stepNum = req.params.step_id;
    // db.query 메소드가 제대로 정의되어 있는지 확인
    if (db && typeof db.query === 'function') {
      let sql = `SELECT step, stepofstep_tip, tip_img FROM stepofstep where recipe_id = ${recipeNum} and step = ${stepNum}`;
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          stepofstep_recipeid_recipestep: results,//이거가 allget 에 변수로감
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

router.get("/:recipe_id/:step_id/:stepofstep_id", async (req, res) => {
  try {
    const recipeNum = req.params.recipe_id;
    const stepNum = req.params.step_id;
    const stepofstepNum = req.params.stepofstep_id
    // db.query 메소드가 제대로 정의되어 있는지 확인
    if (db && typeof db.query === 'function') {
      let sql = `SELECT step, stepofstep_tip, tip_img FROM stepofstep where recipe_id = ${recipeNum} and step = ${stepNum} and stepOfstep = ${stepofstepNum}`;
      db.query(sql, (err, results) => {
        if (err) throw err.message;
        res.json({
          recipe_step_stepofstep: results,//이거가 allget 에 변수로감
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
