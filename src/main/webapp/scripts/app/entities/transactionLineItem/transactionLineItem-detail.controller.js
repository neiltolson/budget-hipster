'use strict';

angular.module('budgetApp')
    .controller('TransactionLineItemDetailController', function ($scope, $rootScope, $stateParams, entity, TransactionLineItem, BudgetLineItem, BudgetAccountTransaction) {
        $scope.transactionLineItem = entity;
        $scope.load = function (id) {
            TransactionLineItem.get({id: id}, function(result) {
                $scope.transactionLineItem = result;
            });
        };
        var unsubscribe = $rootScope.$on('budgetApp:transactionLineItemUpdate', function(event, result) {
            $scope.transactionLineItem = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
