'use strict';

angular.module('budgetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('budgetAccount', {
                parent: 'entity',
                url: '/budgetAccounts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BudgetAccounts'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/budgetAccount/budgetAccounts.html',
                        controller: 'BudgetAccountController'
                    }
                },
                resolve: {
                }
            })
            .state('budgetAccount.detail', {
                parent: 'entity',
                url: '/budgetAccount/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BudgetAccount'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/budgetAccount/budgetAccount-detail.html',
                        controller: 'BudgetAccountDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'BudgetAccount', function($stateParams, BudgetAccount) {
                        return BudgetAccount.get({id : $stateParams.id});
                    }]
                }
            })
            .state('budgetAccount.new', {
                parent: 'budgetAccount',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budgetAccount/budgetAccount-dialog.html',
                        controller: 'BudgetAccountDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    userId: null,
                                    accountType: null,
                                    name: null,
                                    subAccountName: null,
                                    startDate: null,
                                    endDate: null,
                                    lastUpdated: null,
                                    status: null,
                                    notes: null,
                                    startBalance: null,
                                    sortOrder: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('budgetAccount', null, { reload: true });
                    }, function() {
                        $state.go('budgetAccount');
                    })
                }]
            })
            .state('budgetAccount.edit', {
                parent: 'budgetAccount',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budgetAccount/budgetAccount-dialog.html',
                        controller: 'BudgetAccountDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['BudgetAccount', function(BudgetAccount) {
                                return BudgetAccount.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('budgetAccount', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('budgetAccount.delete', {
                parent: 'budgetAccount',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budgetAccount/budgetAccount-delete-dialog.html',
                        controller: 'BudgetAccountDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['BudgetAccount', function(BudgetAccount) {
                                return BudgetAccount.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('budgetAccount', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
