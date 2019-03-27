var app = angular.module('view_book', ['ngRoute']);

app.controller('view_book_controller', function($scope,$http, $route, $routeParams, $location)
{
    console.log('$location: '+$location);

    $scope.get_book=function ()
    {
        $http({
            method : "GET",
            url : "/api/author/"+$location.$$path.replace('/','')
        }).then(function mySuccess(response)
        {
            $scope.id = response.data.id;
            $scope.firstName = response.data.firstName;
            $scope.lastName = response.data.lastName;
            $scope.email = response.data.email;
            $scope.phone = response.data.phone;

            $scope.addressType = response.data.addresses[0].addressType.type;
            $scope.streetName = response.data.addresses[0].streetName;
            $scope.apartment = response.data.addresses[0].apartment;
            $scope.city = response.data.addresses[0].city;
            $scope.state = response.data.addresses[0].state;
            $scope.zipCode = response.data.addresses[0].zipCode;

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
