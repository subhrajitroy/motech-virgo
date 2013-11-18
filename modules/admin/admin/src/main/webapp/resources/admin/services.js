(function () {
    'use strict';

    /* Services */

    angular.module('bundleServices', ['ngResource']).factory('Bundle', function($resource) {
        return $resource('../admin/api/bundles/:bundleId/:action', {bundleId:'@bundleId'}, {
            start: {method:'POST', params: {action: 'start'}},
            stop: {method:'POST', params: {action: 'stop'}},
            restart: {method:'POST', params: {action: 'restart'}},
            uninstall: {method: 'POST', params: {action: 'uninstall'}},
            details: {method: 'GET', params: {action: 'detail'}}
        });
    });

    angular.module('messageServices', ['ngResource']).factory('StatusMessage', function($resource) {
        return $resource('../admin/api/messages');
    });

    angular.module('notificationRuleServices', ['ngResource']).factory('NotificationRule', function($resource) {
        return $resource('../admin/api/messages/rules/:ruleId', {ruleId: '@_id'});
    });

    angular.module('notificationRuleDtoServices', ['ngResource']).factory('NotificationRuleDto', function($resource) {
        return $resource('../admin/api/messages/rules/dto');
    });

    angular.module('configurationServices', ['ngResource']).factory('PlatformSettings', function($resource) {
        return $resource('../admin/api/settings/platform');
    });

    angular.module('moduleSettingsServices', ['ngResource']).factory('ModuleSettings', function($resource) {
        return $resource('../admin/api/settings/:bundleId');
    });

    angular.module('logService', ['ngResource']).factory('LogService', function($resource) {
        return $resource('../admin/api/log/level');
    });

}());
