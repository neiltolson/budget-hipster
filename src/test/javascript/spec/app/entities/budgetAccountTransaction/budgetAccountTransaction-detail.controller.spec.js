'use strict';

describe('Controller Tests', function() {

    describe('BudgetAccountTransaction Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBudgetAccountTransaction, MockBudgetAccount;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBudgetAccountTransaction = jasmine.createSpy('MockBudgetAccountTransaction');
            MockBudgetAccount = jasmine.createSpy('MockBudgetAccount');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BudgetAccountTransaction': MockBudgetAccountTransaction,
                'BudgetAccount': MockBudgetAccount
            };
            createController = function() {
                $injector.get('$controller')("BudgetAccountTransactionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'budgetApp:budgetAccountTransactionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
