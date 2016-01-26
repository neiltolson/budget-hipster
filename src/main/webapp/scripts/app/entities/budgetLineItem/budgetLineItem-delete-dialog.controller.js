'use strict';

angular.module('budgetApp')
	.controller('BudgetLineItemDeleteController', function($scope, $uibModalInstance, entity, BudgetLineItem) {

        $scope.budgetLineItem = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            BudgetLineItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
