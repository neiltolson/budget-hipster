'use strict';

angular.module('budgetApp')
	.controller('TransactionLineItemDeleteController', function($scope, $uibModalInstance, entity, TransactionLineItem) {

        $scope.transactionLineItem = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            TransactionLineItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
