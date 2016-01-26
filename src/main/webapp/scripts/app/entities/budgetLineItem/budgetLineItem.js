'use strict';

angular.module('budgetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('budgetLineItem', {
                parent: 'entity',
                url: '/budgetLineItems',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BudgetLineItems'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/budgetLineItem/budgetLineItems.html',
                        controller: 'BudgetLineItemController'
                    }
                },
                resolve: {
                }
            })
            .state('budgetLineItem.detail', {
                parent: 'entity',
                url: '/budgetLineItem/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BudgetLineItem'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/budgetLineItem/budgetLineItem-detail.html',
                        controller: 'BudgetLineItemDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'BudgetLineItem', function($stateParams, BudgetLineItem) {
                        return BudgetLineItem.get({id : $stateParams.id});
                    }]
                }
            })
            .state('budgetLineItem.new', {
                parent: 'budgetLineItem',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budgetLineItem/budgetLineItem-dialog.html',
                        controller: 'BudgetLineItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    budgetAmount: null,
                                    budgetLineItemType: null,
                                    notes: null,
                                    sortOrder: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('budgetLineItem', null, { reload: true });
                    }, function() {
                        $state.go('budgetLineItem');
                    })
                }]
            })
            .state('budgetLineItem.edit', {
                parent: 'budgetLineItem',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budgetLineItem/budgetLineItem-dialog.html',
                        controller: 'BudgetLineItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['BudgetLineItem', function(BudgetLineItem) {
                                return BudgetLineItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('budgetLineItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('budgetLineItem.delete', {
                parent: 'budgetLineItem',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/budgetLineItem/budgetLineItem-delete-dialog.html',
                        controller: 'BudgetLineItemDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['BudgetLineItem', function(BudgetLineItem) {
                                return BudgetLineItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('budgetLineItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
