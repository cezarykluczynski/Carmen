<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<head>
    <meta charset="utf-8" />

    <spring:url value="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

    <spring:url value="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js" var="jQuery"/>
    <script src="${jQuery}"></script>

    <spring:url value="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular.min.js" var="Angular"/>
    <script src="${Angular}"></script>

    <spring:url value="/static/javascript/module/carmenMainPage.js" var="moduleCarmen"/>
    <script src="${moduleCarmen}"></script>

    <spring:url value="/static/javascript/module/carmenGitHubUser.js" var="moduleCarmenGitHubUser"/>
    <script src="${moduleCarmenGitHubUser}"></script>

    <spring:url value="/static/javascript/controller/carmenMainPage/MainPageSearchController.js" var="MainPageSearchController"/>
    <script src="${MainPageSearchController}"></script>

    <spring:url value="/static/javascript/controller/carmenGitHubUser/FollowersFollowingController.js" var="FollowersFollowingController"/>
    <script src="${FollowersFollowingController}"></script>

    <spring:url value="/static/javascript/controller/carmenGitHubUser/BasicProfileController.js" var="BasicProfileController"/>
    <script src="${BasicProfileController}"></script>

    <spring:url value="/static/dist/css/admin.css" var="adminCss"/>
    <link href="${adminCss}" rel="stylesheet"/>

    <spring:url value="/static/dist/css/mainPage.css" var="mainPageCss"/>
    <link href="${mainPageCss}" rel="stylesheet"/>
</head>
