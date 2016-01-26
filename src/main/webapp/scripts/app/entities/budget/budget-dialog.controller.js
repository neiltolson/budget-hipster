'use strict';

angular.module('budgetApp').controller('BudgetDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Budget',
        function($scope, $stateParams, $uibModalInstance, entity, Budget) {

        $scope.budget = entity;
        $scope.load = function(id) {
            Budget.get({id : id}, function(result) {
                $scope.budget = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('budgetApp:budgetUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.budget.id != null) {
                Budget.update($scope.budget, onSaveSuccess, onSaveError);
            } else {
                Budget.save($scope.budget, onSaveSuccess, onSaveError);
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
