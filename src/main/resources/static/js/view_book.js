var app = angular.module('view_book', ["ngRoute"]);
app.controller('view_book_controller', function($scope,$http,$location)
{
    $scope.validate_and_save_book=function ()
    {
        $http({
            method : "GET",
            url : "/books/"+id
        }).then(function mySuccess(response)
        {
            $scope.books_data = response.data;
        }, function myError(response)
        {
            $scope.myWelcome = response.statusText;
        });
    };
});

app.config(function($routeProvider)
{
    $routeProvider
        .when("/", {
            templateUrl : "../index.html"
        })
        .when("/view_book", {
            templateUrl : "../view_book.html"
        })
        .when("/save_book", {
            templateUrl : "../save_book.html"
        })
        .when("/books", {
            templateUrl : "../books.html"
        });
});
