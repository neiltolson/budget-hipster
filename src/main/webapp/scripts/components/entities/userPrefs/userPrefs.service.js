'use strict';

angular.module('budgetApp')
    .factory('UserPrefs', function ($resource, DateUtils) {
        return $resource('api/userPrefss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
