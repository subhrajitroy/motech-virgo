(function () {
    'use strict';

    /* App Module */

    angular.module('motech-admin', ['motech-dashboard', 'bundleServices', 'messageServices', 'configurationServices',
        'moduleSettingsServices', 'logService', 'ngCookies', 'ngRoute', 'bootstrap', "notificationRuleServices", "notificationRuleDtoServices"])
        .config(['$routeProvider', function($routeProvider) {
          $routeProvider.
              when('/bundles', {templateUrl: 'resources/partials/bundles.html', controller: 'BundleListCtrl'}).
              when('/messages', {templateUrl: 'resources/partials/messages.html', controller: 'StatusMsgCtrl'}).
              when('/platform-settings', {templateUrl: 'resources/partials/settings.html', controller: 'SettingsCtrl'}).
              when('/bundle/:bundleId', {templateUrl: 'resources/partials/bundle.html', controller: 'ModuleCtrl'}).
              when('/bundleSettings/:bundleId', {templateUrl: 'resources/partials/bundleSettings.html', controller: 'BundleSettingsCtrl'}).
              when('/log', {templateUrl: 'resources/partials/log.html', controller: 'ServerLogCtrl'}).
              when('/queues', {templateUrl: 'resources/partials/queue_stats.html', controller: 'QueueStatisticsCtrl'}).
              when('/queues/browse', {templateUrl: 'resources/partials/queue_message_stats.html', controller: 'MessageStatisticsCtrl'}).
              when('/logOptions', {templateUrl: 'resources/partials/logOptions.html', controller: 'ServerLogOptionsCtrl'}).
              when('/notificationRules', {templateUrl: 'resources/partials/notificationRules.html', controller: 'NotificationRuleCtrl'}).
              otherwise({redirectTo: '/bundles'});
    }]).filter('moduleName', function () {
        return function (input) {
            return input.replace(/(motech\s|\sapi|\sbundle)/ig, '');
        };
    });
}());
