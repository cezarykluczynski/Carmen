"use strict";

carmen
    .controller( "MainPageSearchController", [ "$scope", "$http", "$window", function( $scope, $http, $window ) {
        $scope.search = "";
        $scope.searching = false;

        $scope.submit = function ( $event ) {
            $event.preventDefault();
            $scope.searching = true;

            $http.get("rest/analyze/github/" + $scope.search )
                .then( function ( response ) {
                    var status = response.data.status;

                    if ( "found" === status ) {
                        $window.location += "github/" + $scope.search
                    } else {
                        $scope.searching = false;
                    }
                });
        };
    }]);
