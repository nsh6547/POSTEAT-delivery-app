var express = require('express');
var router = express.Router();
var connection = require('./mysql_connect.js');
var sql1 = "select * from restaurant_detail";
var sql2 = "select * from restaurant_info";
var sql3 = "select * from restaurant_menu";
/* GET home page. */
router.get('/', function(req, res, next) {
  // res.render('index', { title: 'Express' });
  connection.connect(function(err){
    if(err){
      console.log('접속오류');
      console.log(err);
    }else{
      console.log('접속성공');
      connection.query(sql1, (err, result, fileds)=>{
        res.json(result);
      });

    }
  })
});

module.exports = router;
