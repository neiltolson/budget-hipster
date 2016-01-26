'use strict';

angular.module('budgetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('transactionLineItem', {
                parent: 'entity',
                url: '/transactionLineItems',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'TransactionLineItems'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/transactionLineItem/transactionLineItems.html',
                        controller: 'TransactionLineItemController'
                    }
                },
                resolve: {
                }
            })
            .state('transactionLineItem.detail', {
                parent: 'entity',
                url: '/transactionLineItem/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'TransactionLineItem'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/transactionLineItem/transactionLineItem-detail.html',
                        controller: 'TransactionLineItemDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'TransactionLineItem', function($stateParams, TransactionLineItem) {
                        return TransactionLineItem.get({id : $stateParams.id});
                    }]
                }
            })
            .state('transactionLineItem.new', {
                parent: 'transactionLineItem',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/transactionLineItem/transactionLineItem-dialog.html',
                        controller: 'TransactionLineItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    description: null,
                                    amount: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('transactionLineItem', null, { reload: true });
                    }, function() {
                        $state.go('transactionLineItem');
                    })
                }]
            })
            .state('transactionLineItem.edit', {
                parent: 'transactionLineItem',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/transactionLineItem/transactionLineItem-dialog.html',
                        controller: 'TransactionLineItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TransactionLineItem', function(TransactionLineItem) {
                                return TransactionLineItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('transactionLineItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('transactionLineItem.delete', {
                parent: 'transactionLineItem',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/transactionLineItem/transactionLineItem-delete-dialog.html',
                        controller: 'TransactionLineItemDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['TransactionLineItem', function(TransactionLineItem) {
                                return TransactionLineItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('transactionLineItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
