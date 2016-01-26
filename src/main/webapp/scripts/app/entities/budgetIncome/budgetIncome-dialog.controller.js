'use strict';

angular.module('budgetApp').controller('BudgetIncomeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'BudgetIncome', 'Budget',
        function($scope, $stateParams, $uibModalInstance, entity, BudgetIncome, Budget) {

        $scope.budgetIncome = entity;
        $scope.budgets = Budget.query();
        $scope.load = function(id) {
            BudgetIncome.get({id : id}, function(result) {
                $scope.budgetIncome = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('budgetApp:budgetIncomeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.budgetIncome.id != null) {
                BudgetIncome.update($scope.budgetIncome, onSaveSuccess, onSaveError);
            } else {
                BudgetIncome.save($scope.budgetIncome, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
