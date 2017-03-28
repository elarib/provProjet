var app = angular.module('frontend', ['ngFileUpload']);
app.controller('frontendController', ['$scope', 'Upload', '$timeout', '$http', function ($scope, Upload, $timeout, $http) {



	$scope.uploadFiles = function(files, errFiles) {
        $scope.files = files;
        $scope.errFiles = errFiles;

        angular.forEach(files, function(file) {
            file.upload = Upload.upload({
                url: 'http://localhost:8080/upload',
                data: {file: file}
            });

            $scope.inputName = file.name;





            file.upload.then(function (response) {
                $timeout(function () {
                    file.result = response.data;
                });
            }, function (response) {
                if (response.status > 0)
                    $scope.errorMsg = response.status + ': ' + response.data;
            }, function (evt) {
                file.progress = Math.min(100, parseInt(100.0 * 
                                         evt.loaded / evt.total));
            });
        });

    }



    $scope.pathSuffix ="AbbyyZlib.dll";
    $scope.outputName = "logFile222.html";
    $scope.operation ="";
    $scope.pid=222;

    $scope.sendToServer = function(){
           // 
           // $scope.pathSuffix

           $scope.resData = {
            inputName  : $scope.inputName,
            outputName : $scope.outputName,
            pathSuffix : $scope.pathSuffix

           }
           console.log($scope.resData);


           $http({
                method: 'POST',
                url: 'http://localhost:8080/res',
                data: $scope.resData
            }).then(function(data) {
                console.log(data);
            },function(data) {
                console.error(data);
            });
         


        }
}]);