// http://nodejs.org/api.html#_child_processes
var util = require('util')
var child_process = require('child_process');
var exec = child_process.exec;
var execSync = require("k-gun-execsync");

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
app.use('/output', express.static(__dirname + '/output'));
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

function runBash(input, output, processName, pid, operation, pathSuffix){

input = "./upload/"+input;
output = "output/"+output;

var isItError = false;
// executes `pwd`  Logfile2.CSV logFile.html ? ? ? AbbyyZlib.dll
// inputName  : $scope.inputName,
//             outputName : $scope.outputName,
//             processName: $scope.processName,
//             pid : $scope.pid,
//             operation : $scope.operation,
//             pathSuffix : $scope.pathSuffix
var cmd = "sh ../b.sh "+input+" "+output+" "+processName+" "+pid+" "+operation+" "+pathSuffix;
console.log(cmd);
//console.log(child_process.execSync(cmd,{encoding: 'utf-8'}).toString().trim());

//var child = child_process.execSync(cmd, {encoding: 'utf-8'});


//console.log(child.toString());
child_process.execSync("ls", function(output){
   console.log(output);
});

}

var dumpData= {
  "inputName":"Logfile2.CSV",
  "operation":"NULL",
  "outputName":"kjhjkh.html",
  "pid":"NULL",
  "processName":"NULL",
  "pathSuffix":"AbbyyZlib.dll"
};

runBash(dumpData.inputName, dumpData.outputName, dumpData.processName, dumpData.pid, dumpData.operation, dumpData.pathSuffix);


/** API path that will run the Bash Script */
app.post('/res', function(req, res) {
    // runBash()
    console.log(req.body);

   runBash(res, req.body.inputName, req.body.outputName, req.body.processName, req.body.pid, req.body.operation, req.body.pathSuffix);

   res.send("lkjl");

});
