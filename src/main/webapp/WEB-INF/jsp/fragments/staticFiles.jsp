<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<head>
    <meta charset="utf-8" />

    <spring:url value="/webjars/bootstrap/3.3.5/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

    <spring:url value="/webjars/jquery/2.1.3/jquery.js" var="jQuery"/>
    <script src="${jQuery}"></script>

    <spring:url value="/webjars/angularjs/1.4.3/angular.js" var="Angular"/>
    <script src="${Angular}"></script>

    <spring:url value="/resources/javascript/module/carmenMainPage.js" var="moduleCarmen"/>
    <script src="${moduleCarmen}"></script>

    <spring:url value="/resources/javascript/module/carmenGitHubUser.js" var="moduleCarmenGitHubUser"/>
    <script src="${moduleCarmenGitHubUser}"></script>

    <spring:url value="/resources/javascript/controller/carmenMainPage/MainPageSearchController.js" var="MainPageSearchController"/>
    <script src="${MainPageSearchController}"></script>

    <spring:url value="/resources/javascript/controller/carmenGitHubUser/FollowersFollowingController.js" var="FollowersFollowingController"/>
    <script src="${FollowersFollowingController}"></script>

    <spring:url value="/resources/javascript/controller/carmenGitHubUser/BasicProfileController.js" var="BasicProfileController"/>
    <script src="${BasicProfileController}"></script>

    <spring:url value="/resources/css/common.css" var="commonCss"/>
    <link href="${commonCss}" rel="stylesheet"/>

    <spring:url value="/resources/css/main-page.css" var="mainPageCss"/>
    <link href="${mainPageCss}" rel="stylesheet"/>
</head>