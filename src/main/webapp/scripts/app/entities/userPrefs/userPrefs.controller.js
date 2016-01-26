'use strict';

angular.module('budgetApp')
    .controller('UserPrefsController', function ($scope, $state, UserPrefs) {

        $scope.userPrefss = [];
        $scope.loadAll = function() {
            UserPrefs.query(function(result) {
               $scope.userPrefss = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.userPrefs = {
                userId: null,
                key: null,
                value: null,
                id: null
            };
        };
    });
