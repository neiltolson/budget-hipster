'use strict';

angular.module('budgetApp')
    .factory('BudgetIncome', function ($resource, DateUtils) {
        return $resource('api/budgetIncomes/:id', {}, {
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
