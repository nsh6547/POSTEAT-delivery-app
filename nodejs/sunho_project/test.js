const { json } = require('body-parser');
const { PythonShell } = require('python-shell');
var options = {
    mode: 'text',
    pythonPath: 'C:\\Users\\nsh65\\anaconda3\\python',
    scriptPath: 'public\\ai\\'
}
PythonShell.run('face_crop_linux.py', options, function(err, results){
    if(err) throw err;
    else{
        
    }
});