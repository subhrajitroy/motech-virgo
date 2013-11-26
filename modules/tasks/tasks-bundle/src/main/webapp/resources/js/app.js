(function () {
    'use strict';

    /* App Module */

    angular.module('motech-tasks', ['motech-dashboard', 'channelServices', 'taskServices', 'activityServices',
                                    'manageTaskUtils', 'dataSourceServices', 'settingsServices', 'ngCookies', 'ngRoute', 'bootstrap',
                                    'motech-widgets']).config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/dashboard', {templateUrl: '../tasks/api/resources/partials/tasks.html', controller: 'DashboardCtrl'}).
                when('/task/new', {templateUrl: '../tasks/api/resources/partials/form.html', controller: 'ManageTaskCtrl'}).
                when('/task/:taskId/edit', {templateUrl: '../tasks/api/resources/partials/form.html', controller: 'ManageTaskCtrl'}).
                when('/task/:taskId/log', {templateUrl: '../tasks/api/resources/partials/history.html', controller: 'LogCtrl'}).
                when('/settings', {templateUrl: '../tasks/api/resources/partials/settings.html', controller: 'SettingsCtrl'}).
                otherwise({redirectTo: '/dashboard'});
        }]);
}());
