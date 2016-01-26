'use strict';

angular.module('budgetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userPrefs', {
                parent: 'entity',
                url: '/userPrefss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserPrefss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userPrefs/userPrefss.html',
                        controller: 'UserPrefsController'
                    }
                },
                resolve: {
                }
            })
            .state('userPrefs.detail', {
                parent: 'entity',
                url: '/userPrefs/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserPrefs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userPrefs/userPrefs-detail.html',
                        controller: 'UserPrefsDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'UserPrefs', function($stateParams, UserPrefs) {
                        return UserPrefs.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userPrefs.new', {
                parent: 'userPrefs',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userPrefs/userPrefs-dialog.html',
                        controller: 'UserPrefsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    userId: null,
                                    key: null,
                                    value: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userPrefs', null, { reload: true });
                    }, function() {
                        $state.go('userPrefs');
                    })
                }]
            })
            .state('userPrefs.edit', {
                parent: 'userPrefs',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userPrefs/userPrefs-dialog.html',
                        controller: 'UserPrefsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserPrefs', function(UserPrefs) {
                                return UserPrefs.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userPrefs', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('userPrefs.delete', {
                parent: 'userPrefs',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userPrefs/userPrefs-delete-dialog.html',
                        controller: 'UserPrefsDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['UserPrefs', function(UserPrefs) {
                                return UserPrefs.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userPrefs', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
