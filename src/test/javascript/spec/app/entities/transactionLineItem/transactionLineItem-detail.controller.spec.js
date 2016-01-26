'use strict';

describe('Controller Tests', function() {

    describe('TransactionLineItem Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTransactionLineItem, MockBudgetLineItem, MockBudgetAccountTransaction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTransactionLineItem = jasmine.createSpy('MockTransactionLineItem');
            MockBudgetLineItem = jasmine.createSpy('MockBudgetLineItem');
            MockBudgetAccountTransaction = jasmine.createSpy('MockBudgetAccountTransaction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TransactionLineItem': MockTransactionLineItem,
                'BudgetLineItem': MockBudgetLineItem,
                'BudgetAccountTransaction': MockBudgetAccountTransaction
            };
            createController = function() {
                $injector.get('$controller')("TransactionLineItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'budgetApp:transactionLineItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
