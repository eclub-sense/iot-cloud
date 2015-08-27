(function () {
    var app = angular.module('ESC_WebApp', []);

    app.controller('PublicSensorController', ['$http', '$log', '$window', function ($http, $log, $window) {
        var pub_con = this;
        this.sensors = [];

        this.loadSensors = function () {
            $http.get('http://147.32.107.139:8081/registered_sensors')
                .then(function (response) {
                    pub_con.sensors = response.data._public;
                });
        };

        this.loadSensors();

        this.showData = function (index) {
            $log.log("Index: " + index);
            //$window.alert("Index: " + index);
            $http.get('http://147.32.107.139:8081/registered_sensors/' + pub_con.sensors[index].uuid)
                .then(function (response) {

                    var text = "Data:";


                    response.data.measured.forEach(function (entry) {
                        text += "\n" + entry.name + ": " + entry.value;
                    });


                    $window.alert(text);
                }, function (response) {
                    $log.warn("ERROR");
                });

        };
    }]);

    app.controller('PrivateSensorController', ['$http', '$log', '$window', function ($http, $log, $window) {
        this.alertLoggedPerson = function () {
            var auth2 = gapi.auth2.getAuthInstance();
            var profile = auth2.currentUser.get().getBasicProfile();
            console.log(profile);
            console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
            console.log('Name: ' + profile.getName());
            console.log('Image URL: ' + profile.getImageUrl());
            console.log('Email: ' + profile.getEmail());
            console.log('ID_token: '+ gapi.auth2.getAuthInstance().currentUser.get().getAuthResponse().id_token);
        };
    }]);


    app.controller('RegistrationController', ['$http', '$log', function ($http, $log) {
        var reg_con = this;
        this.registration = {};

        this.register = function () {
            $log.log('START');
            $log.log(reg_con.registration);
            $http.post('http://147.32.107.139:8081/sensor_registration?access_token=ya29.1wFuoBApLzhK0zgFi4C9Mu3lbqxA10AdZ2b1ihQLctssG8YgZ0ghyOymdZ3I9QIktWvu', reg_con.registration)
                .then(function () {
                    $log.log('END');
                });
        };
    }]);
})();
