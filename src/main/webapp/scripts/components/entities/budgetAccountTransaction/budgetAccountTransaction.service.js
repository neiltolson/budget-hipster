'use strict';

angular.module('budgetApp')
    .factory('BudgetAccountTransaction', function ($resource, DateUtils) {
        return $resource('api/budgetAccountTransactions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.transactionDate = DateUtils.convertLocaleDateFromServer(data.transactionDate);
                    data.reconciledDate = DateUtils.convertLocaleDateFromServer(data.reconciledDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.transactionDate = DateUtils.convertLocaleDateToServer(data.transactionDate);
                    data.reconciledDate = DateUtils.convertLocaleDateToServer(data.reconciledDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.transactionDate = DateUtils.convertLocaleDateToServer(data.transactionDate);
                    data.reconciledDate = DateUtils.convertLocaleDateToServer(data.reconciledDate);
                    return angular.toJson(data);
                }
            }
        });
    });
