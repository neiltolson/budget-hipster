'use strict';

angular.module('budgetApp')
    .controller('BudgetController', function ($scope, $state, Budget) {

        $scope.budgets = [];
        $scope.loadAll = function() {
            Budget.query(function(result) {
               $scope.budgets = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.budget = {
                userId: null,
                name: null,
                startDate: null,
                endDate: null,
                lastUpdated: null,
                status: null,
                id: null
            };
        };
    });
