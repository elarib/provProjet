// http://nodejs.org/api.html#_child_processes
var util = require('util')
var child_process = require('child_process');
var exec = child_process.exec;

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
app.use( bodyParser.json() );       // to support JSON-encoded bodies
app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
  extended: true
})); 
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
            cb(null, file.originalname)
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

function runBash(input, output, pathSuffix){

input = "./upload/"+input;
output = "output/"+output;

var isItError = false;
// executes `pwd`
return child_process.execSync("sh ../b.sh "+input+" "+output+" "+pathSuffix);



}


/** API path that will run the Bash Script */
app.post('/res', function(req, res) {
    // runBash()
    console.log(req.body);

   // res.send(runBash(req.body.inputName, req.body.outputName, req.body.pathSuffix));

   res.send(runBash("Logfile2.CSV", "OUTPUT.HTML", "AbbyyZlib.dll"));

});
