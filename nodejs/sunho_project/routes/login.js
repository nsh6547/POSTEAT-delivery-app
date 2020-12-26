var express = require('express');
var router = express.Router();
var connection = require('./mysql_connect.js');
var sql1 = 'SELECT EXISTS (SELECT userEmailId FROM user_profile WHERE userEmailId = ? and userPw = ?) as success';
var sql2 = 'select userEmailId, userNickname, user_gender, user_age_group from user_profile where userEmailId = ?';
let fail = {"code" : 201};
/* GET users listing. */
router.post('/', function(req, res, next) {
  connection.connect(function(err){
    if(err){
      console.log('접속 오류');
      console.log(err);
    }else{
      console.log('접속 성공 login');
      let input_values = [req.body.userEmailId, req.body.userPw];
      connection.query(sql1, input_values, (err, result) => {
        if(err || result == ""){
          console.log('로그인 에러');
          console.log(err);
          res.json(fail);
        }else{
          if (result[0].success == 1){
            let select_value = req.body.userEmailId;
            connection.query(sql2, select_value, (err, result1) => {
              if(err){
                console.log("로그인 정보 반환 에러");
                console.log(err);
                res.json(fail);
              }else{
                let successdata = {
                  "code" : 200,
                  "data" : {
                      "userEmailId" : result1[0].userEmailId,
                      "userNickname" : result1[0].userNickname,
                      "user_gender" : result1[0].user_gender,
                      "user_age_group" : result1[0].user_age_group
                  }
                };
                res.json(successdata);
              }
            });
          }else{
            console.log("회원정보 존재하지 않음.");
            res.json(fail);
          }
        }
      });
    }
  });
});

module.exports = router;
