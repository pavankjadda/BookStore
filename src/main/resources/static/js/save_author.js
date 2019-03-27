var app = angular.module('save_author', ["ngRoute"]);
app.controller('save_author_controller', function($scope,$http,$location)
{
    $scope.validate_and_save_author=function ()
    {
        var author={firstName:$scope.firstName, lastName:$scope.lastName, email:$scope.email, phone: $scope.phone};

        $http({
            method : "POST",
            url : "/api/author/create",
            data: author

    }).then(function mySuccess(response)
        {
            $scope.form_error=false;
            $scope.form_success=true;
            $scope.author_id=response.data.id;

        }, function myError(response)
        {
            $scope.form_error=true;
            $scope.form_success=false;
        });
    };

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
