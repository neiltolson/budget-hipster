'use strict';

angular.module('budgetApp')
    .controller('BudgetIncomeController', function ($scope, $state, BudgetIncome) {

        $scope.budgetIncomes = [];
        $scope.loadAll = function() {
            BudgetIncome.query(function(result) {
               $scope.budgetIncomes = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.budgetIncome = {
                description: null,
                amount: null,
                id: null
            };
        };
    });
