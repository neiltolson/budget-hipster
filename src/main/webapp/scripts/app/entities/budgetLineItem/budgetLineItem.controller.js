'use strict';

angular.module('budgetApp')
    .controller('BudgetLineItemController', function ($scope, $state, BudgetLineItem) {

        $scope.budgetLineItems = [];
        $scope.loadAll = function() {
            BudgetLineItem.query(function(result) {
               $scope.budgetLineItems = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.budgetLineItem = {
                budgetAmount: null,
                budgetLineItemType: null,
                notes: null,
                categoryGroup: null,
                categoryName: null,
                sortOrder: null,
                id: null
            };
        };
    });
