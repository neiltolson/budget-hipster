'use strict';

angular.module('budgetApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


