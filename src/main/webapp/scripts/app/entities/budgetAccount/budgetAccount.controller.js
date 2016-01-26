'use strict';

angular.module('budgetApp')
    .controller('BudgetAccountController', function ($scope, $state, BudgetAccount) {

        $scope.budgetAccounts = [];
        $scope.loadAll = function() {
            BudgetAccount.query(function(result) {
               $scope.budgetAccounts = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.budgetAccount = {
                userId: null,
                accountType: null,
                name: null,
                subAccountName: null,
                startDate: null,
                endDate: null,
                lastUpdated: null,
                status: null,
                notes: null,
                startBalance: null,
                sortOrder: null,
                id: null
            };
        };
    });
