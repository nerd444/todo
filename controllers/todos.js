const connection = require("../db/mysql-connection");

// @desc        모든 할일 목록 가져오기
// @route       GET /api/v1/todos
// @request     *
// @response    {success:true, rows:rows, count:count}
exports.getTodos = async (req, res, next) => {
  let offset = req.query.offset;
  let limit = req.query.limit;

  if (!offset || !limit) {
    res.status(400).json({ message: "parameters setting error" });
    return;
  }

  let query = `select * from todo limit ${offset},${limit}`;

  try {
    [rows] = await connection.query(query);
    let count = rows.length;
    res.status(200).json({ success: true, rows: rows, count: count });
  } catch (e) {
    res
      .status(500)
      .json({ success: false, message: "할일목록 전부 가져오는데 에러 발생" });
  }
};

// @desc        완료여부 체크 및 해제
// @route       POST /api/v1/todos/:id
// @request     completed
// @response    {success:true, result:result}
exports.completed = async (req, res, next) => {
  let id = req.body.id;
  let completed = req.body.completed;

  if (completed > 1) {
    res.status(400).json({ message: "true and false" });
    return;
  }

  let query = `update todo set completed = ${completed} where id = ${id}`;

  try {
    [result] = await connection.query(query);
    res.status(200).json({ success: true, result: result });
  } catch (e) {
    res.status(500).json({ success: false, message: "서버에러" });
  }
};
