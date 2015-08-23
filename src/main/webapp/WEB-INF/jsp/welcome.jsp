<jsp:include page="fragments/staticFiles.jsp"/>
<body ng-app="carmen">
<jsp:include page="fragments/topbar.jsp"/>

    <div class="container">
        <form class="row user-search-container" ng-controller="MainPageSearchController" ng-submit="submit($event)" ng-disabled="searching">
            <div class="col-xs-12">
                <div class="input-group input-group-lg">
                    <input type="text" class="form-control" ng-model="search" placeholder="Enter GitHub login">
                    <div class="input-group-btn">
                        <button type="submit" class="btn btn-success">Submit</button>
                    </div>
                </div>
            </div>
        </form>
        <div class="row user-count-container">
            <div class="col-xs-12">
                <p class="text-muted users-count-summary">${analyzedUsersCount}</p>
                <p class="text-muted users-count-summary">${connectedUsersCount}</p>
            </div>
        </div>
    </div>

</body>
</html>