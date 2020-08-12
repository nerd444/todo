const express = require("express");
const dotenv = require("dotenv");
dotenv.config({ path: "./config/config.env" });

const todos = require("./routes/todos");

const app = express();
app.use(express.json());

app.use("/api/v1/todos", todos);

const PORT = process.env.PORT || 5252;

app.listen(
  PORT,
  console.log(`Server running in ${process.env.NODE_ENV}mode on port ${PORT}`)
);
