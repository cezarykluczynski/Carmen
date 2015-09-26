<jsp:include page="../../fragments/staticFiles.jsp"/>
<body ng-app="carmenGitHubUser">
<jsp:include page="../../fragments/topbar.jsp"/>

<div class="container">
    <div class="row">
        <div class="col-md-6">
            <div class="panel panel-default" ng-controller="BasicProfileController">
                <div class="panel-heading"><a href="https://github.com/${login}">@${login}</a> on GitHub</div>
                <div class="panel-body">
                    <div class="text-center" ng-show="loading">
                        <h2 class="glyphicon glyphicon-hourglass rotating"></h2>
                    </div>
                    <div ng-cloak ng-show="!loading"></div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4">
            <div class="panel panel-default" ng-controller="FollowersFollowingController">
                <div class="panel-heading">Followers and following</div>
                <div class="panel-body">
                    <div class="text-center" ng-show="loading">
                        <h2 class="glyphicon glyphicon-hourglass"></h2>
                    </div>
                    <div ng-cloak ng-show="!loading"></div>
                </div>
            </div>
        </div>

        <div class="col-md-4">
        </div>

        <div class="col-md-4">
        </div>
    </div>
</div>

</body>
</html>