(function() {
    'use strict';

    angular
        .module('jHipChatApp')
        .controller('TchatController', TchatController);

    TchatController.$inject = ['$scope', 'Principal', 'LoginService', 'ChatSocket'];

    function TchatController ($scope, Principal, LoginService, chatSocket) {
        var vm = this;

        vm.tchat = "";
        vm.movies = null;

        vm.messages = [];
        vm.message = "";
        vm.max = 140;

        vm.username     = '';
        vm.participants = [];

        vm.sendMessage = function() {
			var destination = "/app/chat/message";

			// if($scope.sendTo != "everyone") {
			// 	destination = "/app/chat.private." + $scope.sendTo;
			// 	vm.messages.unshift({message: vm.message, username: 'you', priv: true, to: $scope.sendTo});
			// }

			chatSocket.send(destination, {}, JSON.stringify({message: vm.message}));
			vm.message = '';
		};

        var initStompClient = function() {
			chatSocket.init('/websocket/tchat');

			chatSocket.connect(function(frame) {

				vm.username = "Test";

				chatSocket.subscribe("/topic/online/connect", function(message) {
					vm.participants.unshift({username: JSON.parse(message.body).username, typing : false});
				});

				chatSocket.subscribe("/topic/online/disconnect", function(message) {
					var username = JSON.parse(message.body).username;
					for(var index in $scope.participants) {
						if(vm.participants[index].username == username) {
							vm.participants.splice(index, 1);
						}
					}
		        });

                chatSocket.subscribe("/app/chat/participants", function(message) {
					vm.participants = JSON.parse(message.body);
				});

                chatSocket.subscribe("/topic/chat/message", function(message) {
					vm.messages.unshift(JSON.parse(message.body));
		        });

			}, function(error) {
				//toaster.pop('error', 'Error', 'Connection error ' + error);

		    });
		};

		initStompClient();
    }
})();
