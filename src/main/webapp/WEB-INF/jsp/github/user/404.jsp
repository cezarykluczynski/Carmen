<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.cezarykluczynski.carmen.model.github.User" %>

<jsp:include page="../../fragments/staticFiles.jsp"/>
<body>
<jsp:include page="../../fragments/topbar.jsp"/>

<div class="container">
    <div class="row">
        <div class="col-md-12 text-center">
            <c:if test="${user ne null}">
                Last time we checked, <strong>${login}</strong> was not a valid GitHub user.
            </c:if>
            <c:if test="${user eq null}">
                We never checked if <strong>${login}</strong> is a valid GitHub user.
            </c:if>

            <c:if test="${user ne null and user.canBeUpdated() or user eq null}">
                <br><br>

                <button class="btn btn-success" href="javascript:void(0)" ng-click="inspectNow()">Check now</button>
            </c:if>
        </div>
    </div>
</div>

</body>
</html>