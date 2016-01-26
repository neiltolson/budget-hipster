'use strict';

angular.module('budgetApp')
	.controller('BudgetIncomeDeleteController', function($scope, $uibModalInstance, entity, BudgetIncome) {

        $scope.budgetIncome = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            BudgetIncome.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
