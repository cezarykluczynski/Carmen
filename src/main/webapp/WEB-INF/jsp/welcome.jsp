<jsp:include page="fragments/staticFiles.jsp"/>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
    </div>
</nav>

    <div class="container user-search-container">
        <div class="row">
            <div class="col-xs-12">
                <div class="input-group input-group-lg">
                    <input type="text" class="form-control"  placeholder="Enter Github login and press enter">
                    <div class="input-group-btn">
                        <button type="submit" class="btn btn-success">Submit</button>
                    </div>
                </div>
            </div>
            <div class="col-xs-12">
                <p class="text-muted users-count-summary">${usersCount}</p>
            </div>
        </div>
    </div>

</body>
</html>