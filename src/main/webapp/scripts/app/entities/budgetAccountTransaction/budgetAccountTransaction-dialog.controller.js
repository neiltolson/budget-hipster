'use strict';

angular.module('budgetApp').controller('BudgetAccountTransactionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'BudgetAccountTransaction', 'BudgetAccount',
        function($scope, $stateParams, $uibModalInstance, entity, BudgetAccountTransaction, BudgetAccount) {

        $scope.budgetAccountTransaction = entity;
        $scope.budgetaccounts = BudgetAccount.query();
        $scope.load = function(id) {
            BudgetAccountTransaction.get({id : id}, function(result) {
                $scope.budgetAccountTransaction = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('budgetApp:budgetAccountTransactionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.budgetAccountTransaction.id != null) {
                BudgetAccountTransaction.update($scope.budgetAccountTransaction, onSaveSuccess, onSaveError);
            } else {
                BudgetAccountTransaction.save($scope.budgetAccountTransaction, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForTransactionDate = {};

        $scope.datePickerForTransactionDate.status = {
            opened: false
        };

        $scope.datePickerForTransactionDateOpen = function($event) {
            $scope.datePickerForTransactionDate.status.opened = true;
        };
        $scope.datePickerForReconciledDate = {};

        $scope.datePickerForReconciledDate.status = {
            opened: false
        };

        $scope.datePickerForReconciledDateOpen = function($event) {
            $scope.datePickerForReconciledDate.status.opened = true;
        };
}]);
