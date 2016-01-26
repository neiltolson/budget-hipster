'use strict';

angular.module('budgetApp')
	.controller('UserPrefsDeleteController', function($scope, $uibModalInstance, entity, UserPrefs) {

        $scope.userPrefs = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            UserPrefs.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
