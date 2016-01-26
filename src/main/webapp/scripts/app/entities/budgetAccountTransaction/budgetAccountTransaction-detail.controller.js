'use strict';

angular.module('budgetApp')
    .controller('BudgetAccountTransactionDetailController', function ($scope, $rootScope, $stateParams, entity, BudgetAccountTransaction, BudgetAccount) {
        $scope.budgetAccountTransaction = entity;
        $scope.load = function (id) {
            BudgetAccountTransaction.get({id: id}, function(result) {
                $scope.budgetAccountTransaction = result;
            });
        };
        var unsubscribe = $rootScope.$on('budgetApp:budgetAccountTransactionUpdate', function(event, result) {
            $scope.budgetAccountTransaction = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
