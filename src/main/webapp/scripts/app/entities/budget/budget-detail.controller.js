'use strict';

angular.module('budgetApp')
    .controller('BudgetDetailController', function ($scope, $rootScope, $stateParams, entity, Budget) {
        $scope.budget = entity;
        $scope.load = function (id) {
            Budget.get({id: id}, function(result) {
                $scope.budget = result;
            });
        };
        var unsubscribe = $rootScope.$on('budgetApp:budgetUpdate', function(event, result) {
            $scope.budget = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
