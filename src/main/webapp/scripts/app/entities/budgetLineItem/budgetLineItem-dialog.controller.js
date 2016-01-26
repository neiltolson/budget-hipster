'use strict';

angular.module('budgetApp').controller('BudgetLineItemDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'BudgetLineItem', 'Category', 'Budget',
        function($scope, $stateParams, $uibModalInstance, entity, BudgetLineItem, Category, Budget) {

        $scope.budgetLineItem = entity;
        $scope.categorys = Category.query();
        $scope.budgets = Budget.query();
        $scope.load = function(id) {
            BudgetLineItem.get({id : id}, function(result) {
                $scope.budgetLineItem = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('budgetApp:budgetLineItemUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.budgetLineItem.id != null) {
                BudgetLineItem.update($scope.budgetLineItem, onSaveSuccess, onSaveError);
            } else {
                BudgetLineItem.save($scope.budgetLineItem, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
