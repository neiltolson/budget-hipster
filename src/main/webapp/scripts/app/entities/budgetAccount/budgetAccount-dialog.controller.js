'use strict';

angular.module('budgetApp').controller('BudgetAccountDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'BudgetAccount',
        function($scope, $stateParams, $uibModalInstance, entity, BudgetAccount) {

        $scope.budgetAccount = entity;
        $scope.load = function(id) {
            BudgetAccount.get({id : id}, function(result) {
                $scope.budgetAccount = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('budgetApp:budgetAccountUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.budgetAccount.id != null) {
                BudgetAccount.update($scope.budgetAccount, onSaveSuccess, onSaveError);
            } else {
                BudgetAccount.save($scope.budgetAccount, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForStartDate = {};

        $scope.datePickerForStartDate.status = {
            opened: false
        };

        $scope.datePickerForStartDateOpen = function($event) {
            $scope.datePickerForStartDate.status.opened = true;
        };
        $scope.datePickerForEndDate = {};

        $scope.datePickerForEndDate.status = {
            opened: false
        };

        $scope.datePickerForEndDateOpen = function($event) {
            $scope.datePickerForEndDate.status.opened = true;
        };
        $scope.datePickerForLastUpdated = {};

        $scope.datePickerForLastUpdated.status = {
            opened: false
        };

        $scope.datePickerForLastUpdatedOpen = function($event) {
            $scope.datePickerForLastUpdated.status.opened = true;
        };
}]);
