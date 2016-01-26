'use strict';

angular.module('budgetApp')
    .controller('BudgetAccountTransactionController', function ($scope, $state, BudgetAccountTransaction, ParseLinks) {

        $scope.budgetAccountTransactions = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            BudgetAccountTransaction.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.budgetAccountTransactions = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.budgetAccountTransaction = {
                transactionDate: null,
                transactionType: null,
                description: null,
                amount: null,
                notes: null,
                checkNumber: null,
                transferTransactionId: null,
                reconciledDate: null,
                id: null
            };
        };
    });
