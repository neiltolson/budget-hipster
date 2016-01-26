'use strict';

angular.module('budgetApp')
	.controller('BudgetDeleteController', function($scope, $uibModalInstance, entity, Budget) {

        $scope.budget = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Budget.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
