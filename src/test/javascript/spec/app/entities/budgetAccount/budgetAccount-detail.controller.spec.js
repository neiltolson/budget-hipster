'use strict';

describe('Controller Tests', function() {

    describe('BudgetAccount Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBudgetAccount;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBudgetAccount = jasmine.createSpy('MockBudgetAccount');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BudgetAccount': MockBudgetAccount
            };
            createController = function() {
                $injector.get('$controller')("BudgetAccountDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'budgetApp:budgetAccountUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
