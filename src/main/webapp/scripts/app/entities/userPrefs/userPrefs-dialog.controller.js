'use strict';

angular.module('budgetApp').controller('UserPrefsDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserPrefs',
        function($scope, $stateParams, $uibModalInstance, entity, UserPrefs) {

        $scope.userPrefs = entity;
        $scope.load = function(id) {
            UserPrefs.get({id : id}, function(result) {
                $scope.userPrefs = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('budgetApp:userPrefsUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.userPrefs.id != null) {
                UserPrefs.update($scope.userPrefs, onSaveSuccess, onSaveError);
            } else {
                UserPrefs.save($scope.userPrefs, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
