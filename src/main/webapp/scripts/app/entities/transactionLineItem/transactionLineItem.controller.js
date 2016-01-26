'use strict';

angular.module('budgetApp')
    .controller('TransactionLineItemController', function ($scope, $state, TransactionLineItem) {

        $scope.transactionLineItems = [];
        $scope.loadAll = function() {
            TransactionLineItem.query(function(result) {
               $scope.transactionLineItems = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.transactionLineItem = {
                description: null,
                amount: null,
                id: null
            };
        };
    });
