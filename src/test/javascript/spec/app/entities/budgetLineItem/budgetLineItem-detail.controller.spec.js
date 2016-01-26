'use strict';

describe('Controller Tests', function() {

    describe('BudgetLineItem Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBudgetLineItem, MockCategory, MockBudget;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBudgetLineItem = jasmine.createSpy('MockBudgetLineItem');
            MockCategory = jasmine.createSpy('MockCategory');
            MockBudget = jasmine.createSpy('MockBudget');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BudgetLineItem': MockBudgetLineItem,
                'Category': MockCategory,
                'Budget': MockBudget
            };
            createController = function() {
                $injector.get('$controller')("BudgetLineItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'budgetApp:budgetLineItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
