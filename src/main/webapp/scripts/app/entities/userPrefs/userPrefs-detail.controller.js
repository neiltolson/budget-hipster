'use strict';

angular.module('budgetApp')
    .controller('UserPrefsDetailController', function ($scope, $rootScope, $stateParams, entity, UserPrefs) {
        $scope.userPrefs = entity;
        $scope.load = function (id) {
            UserPrefs.get({id: id}, function(result) {
                $scope.userPrefs = result;
            });
        };
        var unsubscribe = $rootScope.$on('budgetApp:userPrefsUpdate', function(event, result) {
            $scope.userPrefs = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
