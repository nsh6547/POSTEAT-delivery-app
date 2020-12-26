var express = require('express');
var router = express.Router();
var connection = require('./mysql_connect.js');
var sql1 = 'select * from restaurant_info where category = ?';
/* GET users listing. */
router.post('/', function(req, res, next) {
  connection.connect(function(err){
    if(err){
      console.log('접속 오류');
      console.log(err);
    }else{
      console.log('접속 성공 menuList');
      let input_value = req.body.category;
      connection.query(sql1, input_value, (err, result) => {
        if(err){
          console.log("음식점 목록 불러오기 실패");
          console.log(err);
          res.json(err);
        }else{
          console.log(result);
          res.json(result);
        }
      });
    }
  });
});

module.exports = router;
