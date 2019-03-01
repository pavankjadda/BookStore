var app = angular.module('books', ["ngRoute"]);
app.controller('books_controller', function($scope,$http)
{
    $scope.get_books=function ()
    {
        $http({
            method : "GET",
            url : "/api/books"
        }).then(function mySuccess(response)
        {
            $scope.books_data = response.data;
        }, function myError(response)
        {
            $scope.myWelcome = response.statusText;
        });
    }


});

app.config(function($routeProvider)
{
    $routeProvider
        .when("/", {
            templateUrl : "../index.html"
        })
        .when("/view_book/:id", {
        templateUrl : "../view_book.html"
        })
        .when("/save_book", {
            templateUrl : "../save_book.html"
        })
        .when("/books", {
            templateUrl : "../books.html"
        });
});
