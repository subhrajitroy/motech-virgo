(function () {
    'use strict';

    var mds = angular.module('mds', [
        'motech-dashboard', 'entityService', 'fieldService', 'ngCookies', 'ui.directives', 'ngRoute', 'bootstrap',
        'ui.directives'
    ]);

    mds.constant('AVAILABLE_TABS', ['schemaEditor', 'dataBrowser', 'settings']);

    mds.run(function ($rootScope, AVAILABLE_TABS) {
        $rootScope.AVAILABLE_TABS = AVAILABLE_TABS;
    });

    mds.config(function ($routeProvider, AVAILABLE_TABS) {
        angular.forEach(AVAILABLE_TABS, function (tab) {
            $routeProvider.when(
                '/{0}'.format(tab),
                {
                    templateUrl: '../mds/resources/partials/{0}.html'.format(tab),
                    controller: '{0}Ctrl'.format(tab.capitalize())
                }
            );
        });

        $routeProvider.otherwise({
            redirectTo: '/{0}'.format(AVAILABLE_TABS[0])
        });
    });
}());
