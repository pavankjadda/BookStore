var app = angular.module('authors', ["ngRoute"]);
app.controller('authors_controller', function($scope,$http)
{
    $scope.get_authors=function ()
    {
        $http({
            method : "GET",
            url : "/api/author/list"
        }).then(function mySuccess(response)
        {
            $scope.authors_data = response.data;
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
        }).when("/view_author/:id", {
        templateUrl : "../view_author.html"
        })
        .when("/save_author", {
            templateUrl : "../save_author.html"
        })
        .when("/authors", {
            templateUrl : "../authors.html"
        });
});
