(function () {
    'use strict';

    /* Services */

    angular.module('channelServices', ['ngResource']).factory('Channels', function ($resource) {
        return $resource('../tasks/api/channel');
    });

    angular.module('taskServices', ['ngResource']).factory('Tasks', function ($resource) {
        return $resource('../tasks/api/task/:taskId', {taskId: '@_id'});
    });

    angular.module('activityServices', ['ngResource']).factory('Activities', function ($resource) {
        return $resource('../tasks/api/activity/:taskId');
    });

    angular.module('dataSourceServices', ['ngResource']).factory('DataSources', function ($resource) {
        return $resource('../tasks/api/datasource');
    });

    angular.module('settingsServices', ['ngResource']).factory('Settings', function ($resource) {
        return $resource('../tasks/api/settings');
    });

}());