'use strict';

angular.module('budgetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('budgetIncome', {
                parent: 'entity',
                url: '/budgetIncomes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BudgetIncomes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/budgetIncome/budgetIncomes.html',
                        controller: 'BudgetIncomeController'
                    }
                },
                resolve: {
                }
            })
            .state('budgetIncome.detail', {
                parent: 'entity',
                url: '/budgetIncome/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BudgetIncome'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/budgetIncome/budgetIncome-detail.html',
                        controller: 'BudgetIncomeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'BudgetIncome', function($stateParams, BudgetIncome) {
                        return BudgetIncome.get({id : $stateParams.id});
                    }]
                }
            })
            .state('budgetIncome.new', {
                parent: 'budgetIncome',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budgetIncome/budgetIncome-dialog.html',
                        controller: 'BudgetIncomeDialogController',
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
                        $state.go('budgetIncome', null, { reload: true });
                    }, function() {
                        $state.go('budgetIncome');
                    })
                }]
            })
            .state('budgetIncome.edit', {
                parent: 'budgetIncome',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budgetIncome/budgetIncome-dialog.html',
                        controller: 'BudgetIncomeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['BudgetIncome', function(BudgetIncome) {
                                return BudgetIncome.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('budgetIncome', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('budgetIncome.delete', {
                parent: 'budgetIncome',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budgetIncome/budgetIncome-delete-dialog.html',
                        controller: 'BudgetIncomeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['BudgetIncome', function(BudgetIncome) {
                                return BudgetIncome.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('budgetIncome', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
