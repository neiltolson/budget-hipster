'use strict';

angular.module('budgetApp')
	.controller('BudgetAccountTransactionDeleteController', function($scope, $uibModalInstance, entity, BudgetAccountTransaction) {

        $scope.budgetAccountTransaction = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            BudgetAccountTransaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
