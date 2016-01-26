'use strict';

angular.module('budgetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('budget', {
                parent: 'entity',
                url: '/budgets',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Budgets'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/budget/budgets.html',
                        controller: 'BudgetController'
                    }
                },
                resolve: {
                }
            })
            .state('budget.detail', {
                parent: 'entity',
                url: '/budget/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Budget'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/budget/budget-detail.html',
                        controller: 'BudgetDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Budget', function($stateParams, Budget) {
                        return Budget.get({id : $stateParams.id});
                    }]
                }
            })
            .state('budget.new', {
                parent: 'budget',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budget/budget-dialog.html',
                        controller: 'BudgetDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    userId: null,
                                    name: null,
                                    startDate: null,
                                    endDate: null,
                                    lastUpdated: null,
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('budget', null, { reload: true });
                    }, function() {
                        $state.go('budget');
                    })
                }]
            })
            .state('budget.edit', {
                parent: 'budget',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budget/budget-dialog.html',
                        controller: 'BudgetDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Budget', function(Budget) {
                                return Budget.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('budget', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('budget.delete', {
                parent: 'budget',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budget/budget-delete-dialog.html',
                        controller: 'BudgetDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Budget', function(Budget) {
                                return Budget.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('budget', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
