(function() {
    'use strict';

    angular
        .module('jHipChatApp')
        .config(myStateConfig);

    myStateConfig.$inject = ['$stateProvider'];

    function myStateConfig($stateProvider) {
        $stateProvider.state('tchat', {
            parent: 'app',
            url: '/tchat',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/tchat/tchat.html',
                    controller: 'TchatController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
