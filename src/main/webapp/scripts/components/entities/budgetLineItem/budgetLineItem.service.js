'use strict';

angular.module('budgetApp')
    .factory('BudgetLineItem', function ($resource, DateUtils) {
        return $resource('api/budgetLineItems/:id', {}, {
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
