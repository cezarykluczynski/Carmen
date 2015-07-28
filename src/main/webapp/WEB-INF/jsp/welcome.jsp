<jsp:include page="fragments/staticFiles.jsp"/>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
    </div>
</nav>

    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div>
                    <h1>
                        ${message}
                    </h1>
                </div>
                <div class="input-group input-group-lg">
                    <span class="input-group-addon" id="sizing-addon1">@</span>
                    <input type="text" class="form-control" placeholder="Username" aria-describedby="sizing-addon1">
                </div>
            </div>
        </div>
    </div>
</body>
</html>