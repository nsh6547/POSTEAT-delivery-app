const mysql = require('mysql2');
let connection = mysql.createConnection({ // 헤브컴
  host: 'localhost',
  user: 'root',
  password: '123456',
  database: 'baemin',
  waitForConnections : true,
  connectionLimit : 10,
  queueLimit: 0
});

module.exports = connection;
