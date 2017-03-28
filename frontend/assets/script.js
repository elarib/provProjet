var app = angular.module('frontend', ['ngFileUpload']);

app.directive('iframeSetDimensionsOnload', [function(){
return {
    restrict: 'A',
    link: function(scope, element, attrs){
        element.on('load', function(){
            /* Set the dimensions here, 
               I think that you were trying to do something like this: */
               var iFrameHeight = element[0].contentWindow.document.body.scrollHeight + 'px';
               var iFrameWidth = '100%';
               element.css('width', iFrameWidth);
               element.css('height', iFrameHeight);
        })
    }
}}])

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




    $scope.processName ="NULL";
    $scope.pid="NULL";
    $scope.operation ="NULL";
    $scope.pathSuffix ="NULL";
    $scope.isLoading = true;
    //$scope.outputResFile = "output/OUTPUT.HTML";

    $scope.sendToServer = function(){
           // 
           // $scope.pathSuffix

           $scope.resData = {
            inputName  : $scope.inputName,
            outputName : $scope.outputName,
            processName: $scope.processName,
            pid : $scope.pid,
            operation : $scope.operation,
            pathSuffix : $scope.pathSuffix

           }
           console.log($scope.resData);


           $http({
                method: 'POST',
                url: 'http://localhost:8080/res',
                data: $scope.resData
            }).then(function(data) {
                $scope.isLoading = false;
                $scope.outputResFile = "http://localhost:8080/output/"+$scope.outputName+"";
                
                console.log(data);
            },function(data) {
                console.error(data);
            });
         


        }
}]);