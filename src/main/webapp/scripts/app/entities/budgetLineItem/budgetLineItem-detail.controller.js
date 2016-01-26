'use strict';

angular.module('budgetApp')
    .controller('BudgetLineItemDetailController', function ($scope, $rootScope, $stateParams, entity, BudgetLineItem, Category, Budget) {
        $scope.budgetLineItem = entity;
        $scope.load = function (id) {
            BudgetLineItem.get({id: id}, function(result) {
                $scope.budgetLineItem = result;
            });
        };
        var unsubscribe = $rootScope.$on('budgetApp:budgetLineItemUpdate', function(event, result) {
            $scope.budgetLineItem = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
