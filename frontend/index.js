// http://nodejs.org/api.html#_child_processes
var util = require('util')
var exec = require('child_process').exec;

var express = require('express');
var app = express();
var path = require('path');
var bodyParser = require('body-parser');
var multer = require('multer');


app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "http://localhost");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});
app.use('/assets', express.static(__dirname + '/assets'));
app.use('/bower_components', express.static(__dirname + '/bower_components'));

// viewed at http://localhost:8080
app.get('/', function(req, res) {
    res.sendFile(path.join(__dirname + '/index.html'));
});

 var storage = multer.diskStorage({ //multers disk storage settings
        destination: function (req, file, cb) {
            cb(null, './upload/')
        },
        filename: function (req, file, cb) {
            var datetimestamp = Date.now();
            cb(null, file.fieldname + '-' + datetimestamp + '.' + file.originalname.split('.')[file.originalname.split('.').length -1])
        }
    });
    var upload = multer({ //multer settings
                    storage: storage
                }).single('file');
    /** API path that will upload the files */
    app.post('/upload', function(req, res) {
        upload(req,res,function(err){
            if(err){
                 res.json({error_code:1,err_desc:err});
                 return;
            }
             res.json({error_code:0,err_desc:null});
        })
    });

app.listen(8080);

function runBash(){

var child;
// executes `pwd`
child = exec("sh ../b.sh ../Logfile2.CSV logfile2.html AbbyyZlib.dll", function (error, stdout, stderr) {
  console.log('stdout: ' + stdout);
  console.log('stderr: ' + stderr);
  if (error !== null) {
    console.log('exec error: ' + error);
  }
});

}