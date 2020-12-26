var express = require('express');
var router = express.Router();
var connection = require('./mysql_connect.js');
var bodyParser = require('body-parser');
var sql1 = 'insert into user_profile values (?, ?, ?, ?, ?)'
let success = {"code" : 200};
let fail = {"message" : "정확한 입력을 부탁드립니다."};
/* GET users listing. */
router.post('/', function(req, res, next) {
  console.log(req.body);
  connection.connect(function(err){
    if(err){
      console.log('접속 오류');
      console.log(err);
    }else{
      console.log('접속 성공 signup');
      let input_values = [req.body.userEmailId, req.body.userPw, req.body.user_gender,
        req.body.user_age_group, req.body.userNickname];
      connection.query(sql1, input_values, (err, result)=>{
        if(err){
          console.log("회원가입 실패");
          console.log(err);
          res.json(fail);
        }else{
          console.log("회원가입 완료");
          console.log()
          res.json(success);
        }
      });
    }
  });
});

module.exports = router;
