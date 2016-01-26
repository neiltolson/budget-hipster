'use strict';

angular.module('budgetApp')
    .controller('BudgetAccountDetailController', function ($scope, $rootScope, $stateParams, entity, BudgetAccount) {
        $scope.budgetAccount = entity;
        $scope.load = function (id) {
            BudgetAccount.get({id: id}, function(result) {
                $scope.budgetAccount = result;
            });
        };
        var unsubscribe = $rootScope.$on('budgetApp:budgetAccountUpdate', function(event, result) {
            $scope.budgetAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
