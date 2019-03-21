var app = angular.module('view_book', ['ngRoute']);

app.controller('view_book_controller', function($scope,$http, $route, $routeParams, $location)
{
    console.log('$location: '+$location);

    $scope.get_book=function ()
    {
        $http({
            method : "GET",
            url : "/api/book/"+$location.$$path.replace('/','')
        }).then(function mySuccess(response)
        {
            $scope.Id = response.data.id;
            $scope.Title = response.data.title;
            $scope.author = response.data["authors"][0].firstName+' '+response.data["authors"][0].lastName;
            $scope.number_of_pages = response.data.numberOfPages;
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
            templateUrl : "../view_book.html"
        })
        .when("/save_book", {
            templateUrl : "../save_book.html"
        })
        .when("/books", {
            templateUrl : "../books.html"
        });
});
