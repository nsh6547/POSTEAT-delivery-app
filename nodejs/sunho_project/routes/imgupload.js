var express = require('express');
var router = express.Router();
var multer = require('multer');
var fs = require('fs');
const { PythonShell } = require('python-shell');
let result_ai;
var upload = multer({
    dest : '../public/images'
})
/* GET home page. */
router.post('/', upload.single('img'), function(req, res, next) {
    console.log(req.file);
    fs.readFile(req.file.path, function (err, data) {
        console.log(data);
        var filePath = 'public\\images\\emo_pic.jpg';
        fs.writeFile(filePath, data, function (error) {
            if (error) {
                throw error;
            } else {
                var options = {
                    mode: 'text',
                    pythonPath: 'C:\\Users\\nsh65\\anaconda3\\python',
                    scriptPath: 'public\\ai\\'
                }
                PythonShell.run('face_crop_linux.py', options, function(err, results){
                    if(err) throw err;
                    else{
                        var a = JSON.parse(results)
                        result_ai = a.emotion;
                        var result_json = {
                            "data" : {
                                "results" : "success",
                                "result_data" : result_ai
                            }
                        };
                        res.json(result_json);
                        fs.unlink(req.file.path, function (removeFileErr) {
                            if (removeFileErr) {
                                throw removeFileErr;
                            }
                        });
                    }
                })
            }
        });
    });

});

module.exports = router;
