'use strict';

angular.module('budgetApp')
    .controller('BudgetIncomeDetailController', function ($scope, $rootScope, $stateParams, entity, BudgetIncome, Budget) {
        $scope.budgetIncome = entity;
        $scope.load = function (id) {
            BudgetIncome.get({id: id}, function(result) {
                $scope.budgetIncome = result;
            });
        };
        var unsubscribe = $rootScope.$on('budgetApp:budgetIncomeUpdate', function(event, result) {
            $scope.budgetIncome = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
