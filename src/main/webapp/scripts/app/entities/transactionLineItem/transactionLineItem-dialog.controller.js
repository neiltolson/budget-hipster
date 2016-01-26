'use strict';

angular.module('budgetApp').controller('TransactionLineItemDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'TransactionLineItem', 'BudgetLineItem', 'BudgetAccountTransaction',
        function($scope, $stateParams, $uibModalInstance, entity, TransactionLineItem, BudgetLineItem, BudgetAccountTransaction) {

        $scope.transactionLineItem = entity;
        $scope.budgetlineitems = BudgetLineItem.query();
        $scope.budgetaccounttransactions = BudgetAccountTransaction.query();
        $scope.load = function(id) {
            TransactionLineItem.get({id : id}, function(result) {
                $scope.transactionLineItem = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('budgetApp:transactionLineItemUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.transactionLineItem.id != null) {
                TransactionLineItem.update($scope.transactionLineItem, onSaveSuccess, onSaveError);
            } else {
                TransactionLineItem.save($scope.transactionLineItem, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
