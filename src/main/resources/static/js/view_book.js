var app = angular.module('view_book', ["ngRoute"]);
app.controller('view_book_controller', function($scope,$http,$routeParams)
{
    $scope.get_book=function ()
    {
        $http({
            method : "GET",
            url : "/books/"+$routeParams.id
        }).then(function mySuccess(response)
        {
            $scope.Id = response.data.id;
            $scope.Title = response.data.title;
            $scope.author = response.data.author;
            $scope.number_of_pages = response.data.number_of_pages;
            $scope.cost = response.data.cost;

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
        .when("/view_book/:id", {
            templateUrl : "../view_book.html",
            controller: "view_book_controller"
        })
        .when("/save_book", {
            templateUrl : "../save_book.html"
        })
        .when("/books", {
            templateUrl : "../books.html"
        });
});
