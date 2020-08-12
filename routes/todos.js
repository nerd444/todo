const express = require("express");
const { getTodos, completed } = require("../controllers/todos");

const router = express.Router();

// api/v1/users
router.route("/").get(getTodos).post(completed);

module.exports = router;
