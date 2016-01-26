'use strict';

describe('Controller Tests', function() {

    describe('BudgetIncome Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBudgetIncome, MockBudget;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBudgetIncome = jasmine.createSpy('MockBudgetIncome');
            MockBudget = jasmine.createSpy('MockBudget');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BudgetIncome': MockBudgetIncome,
                'Budget': MockBudget
            };
            createController = function() {
                $injector.get('$controller')("BudgetIncomeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'budgetApp:budgetIncomeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
