'use strict';

angular.module('budgetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('budgetAccountTransaction', {
                parent: 'entity',
                url: '/budgetAccountTransactions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BudgetAccountTransactions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/budgetAccountTransaction/budgetAccountTransactions.html',
                        controller: 'BudgetAccountTransactionController'
                    }
                },
                resolve: {
                }
            })
            .state('budgetAccountTransaction.detail', {
                parent: 'entity',
                url: '/budgetAccountTransaction/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BudgetAccountTransaction'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/budgetAccountTransaction/budgetAccountTransaction-detail.html',
                        controller: 'BudgetAccountTransactionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'BudgetAccountTransaction', function($stateParams, BudgetAccountTransaction) {
                        return BudgetAccountTransaction.get({id : $stateParams.id});
                    }]
                }
            })
            .state('budgetAccountTransaction.new', {
                parent: 'budgetAccountTransaction',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budgetAccountTransaction/budgetAccountTransaction-dialog.html',
                        controller: 'BudgetAccountTransactionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    transactionDate: null,
                                    transactionType: null,
                                    description: null,
                                    amount: null,
                                    notes: null,
                                    checkNumber: null,
                                    transferTransactionId: null,
                                    reconciledDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('budgetAccountTransaction', null, { reload: true });
                    }, function() {
                        $state.go('budgetAccountTransaction');
                    })
                }]
            })
            .state('budgetAccountTransaction.edit', {
                parent: 'budgetAccountTransaction',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budgetAccountTransaction/budgetAccountTransaction-dialog.html',
                        controller: 'BudgetAccountTransactionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['BudgetAccountTransaction', function(BudgetAccountTransaction) {
                                return BudgetAccountTransaction.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('budgetAccountTransaction', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('budgetAccountTransaction.delete', {
                parent: 'budgetAccountTransaction',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budgetAccountTransaction/budgetAccountTransaction-delete-dialog.html',
                        controller: 'BudgetAccountTransactionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['BudgetAccountTransaction', function(BudgetAccountTransaction) {
                                return BudgetAccountTransaction.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('budgetAccountTransaction', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
