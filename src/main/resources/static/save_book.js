var app = angular.module('save_book', ["ngRoute"]);
app.controller('save_book_controller', function($scope,$http)
{
    $scope.validate_and_save_book=function ()
    {
        $http({
            method : "POST",
            url : "/books/save_book",
            data: JSON.stringify({title:$scope.Title, cost:$scope.cost, numberOfPages:$scope.number_of_pages,author:$scope.author})

    }).then(function mySuccess(response)
        {
            $scope.myWelcome = response.data;
        }, function myError(response)
        {
            $scope.myWelcome = response.statusText;
        });
    };

    /*$http({
        method : "GET",
        url : "/books"
    }).then(function mySuccess(response) {
        $scope.myWelcome = response.data;
    }, function myError(response) {
        $scope.myWelcome = response.statusText;
    });*/


});

app.config(function($routeProvider)
{
    $routeProvider
        .when("/", {
            templateUrl : "index.html"
        })
        .when("/save_book", {
            templateUrl : "save_book.html"
        })
        .when("/books", {
            templateUrl : "books.html"
        });
});