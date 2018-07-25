var app = angular.module('save_book', ["ngRoute"]);
app.controller('save_book_controller', function($scope,$http,$location)
{
    $scope.validate_and_save_book=function ()
    {
        $http({
            method : "POST",
            url : "/books/save_book",
            data: JSON.stringify({title:$scope.Title, cost:$scope.cost, numberOfPages:$scope.number_of_pages,author:$scope.author})

    }).then(function mySuccess(response)
        {
            $scope.form_error=false;
            $scope.form_success=true;
            $scope.book_id=response.data.id;
            //window.alert("Book saved with Id: "+response.data.id);
            //save_book_form.$pristine = true;

        }, function myError(response)
        {
            //window.alert("Unable to save, please check Book information");
            $scope.form_error=true;
            $scope.form_success=false;
        });
    };
});

app.config(function($routeProvider)
{
    $routeProvider
        .when("/", {
            templateUrl : "../index.html"
        })
        .when("/save_book", {
            templateUrl : "../save_book.html"
        })
        .when("/books", {
            templateUrl : "../books.html"
        });
});