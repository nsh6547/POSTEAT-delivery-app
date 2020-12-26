//1.mysql
var connection = require('./mysql_connect.js');
require('date-utils');


//2.웹서버 생성
var express = require('express');
var app = express();
var http = require('http');
var fs = require('fs');
app.set('view engine', 'ejs');
app.use(express.static('public'));
//큐알코드
var QRCode = require('qrcode');
var qr = require('qr-image');
const crypto = require('crypto');
  
      // 암호화 키
const aes_key = 'capstonecapstone';

var port1 = 80; // aws상 포트
//var port1 = 3001;

var server = http.createServer(app);
server.listen(port1);

var io = require('socket.io')(server);


var temp_qrnum; //큐알넘버 받는곳
var count; //카운트
var location_arr // location 정보 받기

//관리자모드

var arr = new Array();
var i = 0;
var count_qrcode;

function handleDisconnect() {
  connection.on('error', function(err) {
    console.log('db error', err);
    if (err.code === 'PROTOCOL_CONNECTION_LOST') {
      console.log("db reconnect!");
      connection.connect();
    } else {
      throw err;
    }
  });
}

handleDisconnect();

app.post('/management', function(req, res) {
  console.log("관리자모드 시작");
  connection.query('SELECT * FROM qrcode, product where qr_num = product_qr_num;', function(err, results) {
    if (err || results == "") {
      console.log(err);
      res.json("err");
    } else {
       res.json(results);
    }
  });
});

app.post('/search', function(req, res){
  var inputdata;
  req.on('data', function(data){
    inputdata = JSON.parse(data);
  });

  req.on('end', () => {
    if(inputdata.index == "qr_num"){
      connection.query('SELECT * FROM qrcode, product where qr_num LIKE ("' + inputdata.qr_num + '") and qr_num = product_qr_num;', function(err, results){
        if (err || results == "") {
          console.log(err);
          res.json("err");
        } else {
          res.json(results);
        }
      });
    }
    else if (inputdata.index == "qr_id") {
      connection.query('SELECT * FROM qrcode, product where qr_id LIKE ("' + inputdata.qr_id + '") and qr_num = product_qr_num;', function(err, results){
        if (err || results == "") {
          console.log(err);
          res.json("err");
        } else {
          res.json(results);
        }
      });
    }
    else if(inputdata.index == "product_name"){
      connection.query('SELECT * FROM qrcode, product where qrcode.product_name LIKE ("' + inputdata.product_name + '") and qr_num = product_qr_num;', function(err, results){
        if (err || results == "") {
          console.log(err);
          res.json("err");
        } else {
          res.json(results);
        }
      });
    } 
    else {
      connection.query('SELECT * FROM qrcode, product where num LIKE ("' + inputdata.num + '") and qr_num = product_qr_num;', function(err, results){
        if (err || results == "") {
          console.log(err);
          res.json("err");
        } else {
          res.json(results);
        }
      });
    }
  })
})

app.post('/delete', function(req, res) {
  var inputdata;
  req.on('data', (data) => {
    inputdata = JSON.parse(data);
    console.log(inputdata);
  });

  req.on('end', () => {
    connection.query('delete from qrcode where qr_num = "' + inputdata.num + '";', function(err, results) {
      if (err) {
        console.log("delete err");
        res.json("err");
      } else {
        console.log("delete complete");
      }
    });
    connection.query('delete from product where product_qr_num = "' + inputdata.num + '";', function(err, results) {
      if (err) {
        console.log("delete err");
        res.json("err");
      } else {
        console.log("delete complete");
        res.json("ok");
      }
    });
  });
});


app.post('/create', function(req, res) {
  var inputdata;
  req.on('data', (data) => {
    inputdata = JSON.parse(data);
    console.log(inputdata);
  });

  req.on('end',()=>{
    var product_name = inputdata.product_name;
    var company = inputdata.company;
    var date = inputdata.date;
    var remark = inputdata.remark;
    var newDate = new Date();
    var time = newDate.toFormat('YYYY-MM-DD HH24:MI:SS');
    var string_length = 15;
    var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
    

    function randomString() {
      var randomstring = '';
      for (var i = 0; i < string_length; i++) {
        var rnum = Math.floor(Math.random() * chars.length);
        randomstring += chars.substring(rnum, rnum + 1);
      }
      return randomstring;
    }
    function crypto_aes_128_ccm(randomstring){
      var aad_string = randomString();
  
      // number used once 매번 바꿔 사용하는 번호 
      const nonce = crypto.randomBytes(12);
  
      // Addtional Associated Data : https://cloud.google.com/kms/docs/additional-authenticated-data
      const aad = Buffer.from(aad_string, 'hex');
  
      // aes 128 ccm 암호화 객체 생성 TAG는 16바이트
      const cipher = crypto.createCipheriv('aes-128-ccm', aes_key, nonce,  {
      authTagLength: 16
      });
  
      // 평문 데이터
      const plaintext = randomstring;
  
      // aad 추가
      cipher.setAAD(aad, {
      plaintextLength: Buffer.byteLength(plaintext)
      });
  
      // 평문 암호화
      const ciphertext = cipher.update(plaintext, 'utf8');
      console.log(ciphertext.toString("hex"));
      console.log(ciphertext);
  
      // 암호화 완료 - 이 이후로는 더이상 이 암호화 객체를 사용할 수 없음
      cipher.final();
  
      // 최종 암호화 TAG(MAC) 값 얻기
      const tag = cipher.getAuthTag();
  
      connection.query('insert into crypto(ciphertext, aad, tag, nonce) values ("' + ciphertext.toString("hex") + '" , "' + aad_string + '" , "' + tag.toString("hex")  + '" , "' + nonce.toString("hex") + '");', function(error, results) {
          if (error || results == "") {
            console.log(error);
          }
          console.log(results);
      });
  
      connection.query('insert into qrcode(qr_num, qr_id, qr_count, qr_time, product_name) values ("' + randomstring + '", "' + id + '" , 0, "' + time + '", "' + product_name +  '");', function(error, results) {
          if (error || results == "") {
            console.log(error);
            res.json("err1");
            res.end();
          }
          console.log(results);
      });
      connection.query('insert into product values ("' + randomstring + '", "' + product_name + '" , "' + company + '" , "' + date + '" , "' + remark + '");', function(error, results) {
      if (error || results == "") {
          console.log(error);
          res.json("err");
          res.end();
      }
      console.log(results);
      });
      return ciphertext.toString("hex");
  }
    for (var i = 0; i < inputdata.create_count; i++) {
      var randomstring = randomString();
      var id = product_name + randomstring;
      var qrcode_contents = crypto_aes_128_ccm(randomstring);

      var path = __dirname + '/qr_pdf/' + id + '.pdf';
      var path2 = __dirname + '/public/images/qr_png/' + id + '.png';

      var qr_pdf = qr.image(qrcode_contents, {
        type: 'pdf'
      });
      var qr_png = qr.image(qrcode_contents, {
        type: 'png'
      })
      qr_pdf.pipe(require('fs').createWriteStream(path));
      qr_png.pipe(require('fs').createWriteStream(path2));
    }
    res.json("ok");
  });
});

app.post('/discrimination', function(req, res) {
  var inputdata;
  var resultdata;

  req.on('data', (data) => {
    inputdata = JSON.parse(data);
    console.log(inputdata);
  });

  req.on('end', () => {
    const ciphertext_b = Buffer.from(inputdata.num, 'hex');

    connection.query('SELECT aad, nonce, tag FROM crypto WHERE ciphertext ="' + inputdata.num + '";', function(err, result){
      if (err || result == "") {
        console.log("select crypto err");
        res.json("err");
      }else{
        const aad = Buffer.from(result[0].aad, 'hex');
        // 암호화시 사용한 nonce 암호화시 전달 받거나 서로간의 규약이 필요( 일부만 받아 조합해서 사용 등)
        const nonce = Buffer.from(result[0].nonce, 'hex');
        // 암호화 데이터

        // 암호화시 TAG
        const tag = Buffer.from(result[0].tag, 'hex');

        // 암호화 객체 생성 (key와 nonce 추가)
        const decipher = crypto.createDecipheriv('aes-128-ccm', aes_key, nonce,  {
          authTagLength: 16
        });

        // 태그 추가
        decipher.setAuthTag(tag);
        // aad 추가
        decipher.setAAD(aad, {
          plaintextLength: ciphertext_b.length
        });

        // 복호화 시작!
        const receivedPlaintext = decipher.update(ciphertext_b, null, 'utf8');

        try {
          // 복호화 완료 - 더 이상 복호화 할 수 없음.
          decipher.final();
        } catch (err) {
          console.error('Authentication failed!');
          res.end();
        }

        // 복화화 결과 출력!
        console.log(receivedPlaintext);
        connection.query('SELECT EXISTS (SELECT qr_num FROM qrcode WHERE qr_num ="' + receivedPlaintext + '") as success;', function(err, rows) {
          if (err || rows == "") {
            console.log("select err");
            res.json("err");
          }else{
            if (rows[0].success == 1) {
              var count;
              var product_name;
              console.log("접속");
              console.log("****************" + receivedPlaintext);
              connection.query('UPDATE qrcode SET qr_count = qr_count + 1 WHERE qr_num = "' + receivedPlaintext + '";', function(err, results) {
                if (err || results == "") {
                  console.log("update err");
                } else {
                  console.log("update complete");
                }
              });
              connection.query('SELECT qr_count, product_name FROM qrcode WHERE qr_num = "' + receivedPlaintext + '";', function(err, results) {
                if (err || results == "") {
                  console.log("count select err");
                } else {
                  count = results[0].qr_count
                  product_name = results[0].product_name;
                  resultdata = [{
                    "success": 1,
                    "qr_count": count
                  }]
                }
                console.log(resultdata);
                res.json(resultdata);
                var newDate = new Date();
                var time = newDate.toFormat('YYYY-MM-DD HH24:MI:SS');
                console.log(time);
                connection.query('insert into location(latitude, longitude, lo_count, qr_num, access_time, product_name) values(' + inputdata.lat + ',' + inputdata.lon + ',' + count + ',"' + receivedPlaintext + '", "' + time + '", "' + product_name + '");', function(error, results) {
                  if (err) {
                    console.log("insert err");
                  } else {
                    console.log("insert complete");
                  }
                });
              });
      
            } else {
              console.log(rows);
              res.json(rows);
              res.end();
            }
    
          }
          
    
        });
      }
    });

  });

});


app.post('/map', function(req, res) {
  connection.query('SELECT qr_num, latitude as lat, longitude as lng, lo_count, access_time, product_name from location;', function(err, rows) {
    res.json(rows);
  });
});