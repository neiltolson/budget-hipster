'use strict';

angular.module('budgetApp')
	.controller('BudgetAccountDeleteController', function($scope, $uibModalInstance, entity, BudgetAccount) {

        $scope.budgetAccount = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            BudgetAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
