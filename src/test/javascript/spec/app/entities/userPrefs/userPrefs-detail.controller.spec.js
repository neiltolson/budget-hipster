'use strict';

describe('Controller Tests', function() {

    describe('UserPrefs Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockUserPrefs;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockUserPrefs = jasmine.createSpy('MockUserPrefs');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'UserPrefs': MockUserPrefs
            };
            createController = function() {
                $injector.get('$controller')("UserPrefsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'budgetApp:userPrefsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
