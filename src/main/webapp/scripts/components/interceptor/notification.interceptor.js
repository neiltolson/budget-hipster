 'use strict';

angular.module('budgetApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-budgetApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-budgetApp-params')});
                }
                return response;
            }
        };
    });
