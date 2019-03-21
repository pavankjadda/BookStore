var app = angular.module('save_book', ["ngRoute"]);
app.controller('save_book_controller', function($scope,$http,$location)
{
    $scope.validate_and_save_book=function ()
    {
        var authors=[];
        authors.push($scope.author.id);
        var book={title:$scope.Title, cost:$scope.cost, numberOfPages:$scope.number_of_pages, authors: authors};

        $http({
            method : "POST",
            url : "/api/book/save_book",
            data: book

    }).then(function mySuccess(response)
        {
            $scope.form_error=false;
            $scope.form_success=true;
            $scope.book_id=response.data.id;

        }, function myError(response)
        {
            //window.alert("Unable to save, please check Book information");
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
        });
});
