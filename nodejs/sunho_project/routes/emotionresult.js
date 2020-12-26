var express = require('express');
var router = express.Router();
var connection = require('./mysql_connect.js');
var sql1 = "select restaurantNumber, restaurantName from restaurant_info where category = ? or category = ? order by rand() limit 1";
/* GET home page. */
router.post('/', function(req, res, next) {
  // res.render('index', { title: 'Express' });
  connection.connect(function(err){
      if(err){
          console.log('접속오류');
          console.log(err);
      }else{
          console.log('접속 성공 emotionresult');
          console.log(req.body.category1, req.body.category2);
          let inputvalue = [req.body.category1, req.body.category2];
          connection.query(sql1, inputvalue, (err, result) => {
              if(err){
                  console.log("음식점 랭킹 불러오기 실패");
                  console.log(err);
                  res.json(err);
              }else{
                  console.log(result);
                  res.json(result);
              }
          });
      }
  })
});

module.exports = router;