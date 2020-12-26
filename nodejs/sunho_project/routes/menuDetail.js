var express = require('express');
var router = express.Router();
var connection = require('./mysql_connect.js');
var sql1 = 'select restaurantName, minimum, payment, phoneNumber, businessTime, dayOff, deliveryLocation, restaurantLocation from restaurant_info as a, restaurant_detail as b where a.restaurantNumber = ? and b.restaurantNumber = ?';
var sql2 = 'select restaurantName, minimum, payment, phoneNumber, deliveryLocation, restaurantLocation, menuName, price from restaurant_info as a, restaurant_detail as b, restaurant_menu as c where a.restaurantNumber = ? and b.restaurantNumber = ? and c.restaurantNumber = ?';
/* GET users listing. */
router.post('/', function(req, res, next) {
  connection.connect(function(err){
    if(err){
      console.log('접속 오류');
      console.log(err);
    }else{
      console.log('접속 성공 menuDetail');
      console.log(req.body.restaurantNumber);
      let inputvalue = req.body.restaurantNumber;
      let inputvalue2 = [inputvalue, inputvalue];
      console.log(inputvalue);
      connection.query(sql1, inputvalue2, (err, result) => {
        if(err){
          console.log('메뉴 불러오기 오류');
          console.log(err);
          res.json(err);
        }else{
          res.json(result);
        }
      });
    }
  });
});
router.post('/menu', function(req, res, next) {
  connection.connect(function(err){
    if(err){
      console.log('접속 오류');
      console.log(err);
    }else{
      console.log('접속 성공 menuDetail');
      console.log(req.body.restaurantNumber);
      let inputvalue = req.body.restaurantNumber;
      let inputvalue3 = [inputvalue, inputvalue, inputvalue];
      console.log(inputvalue);
      connection.query(sql2, inputvalue3, (err, result) => {
        if(err){
          console.log('메뉴 불러오기 오류');
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
