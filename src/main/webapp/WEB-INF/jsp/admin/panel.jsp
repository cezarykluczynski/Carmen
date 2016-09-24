<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<head>
	<meta charset="utf-8" />
	<spring:url value="/" var="appBase"/>
	<spring:url value="/static/dist/javascript" var="appBaseJavaScript"/>
	<spring:url value="/static/node_modules/rxjs" var="rxjs"/>
	<spring:url value="/static/node_modules/@angular" var="angular2"/>
	<spring:url value="/static/dist/javascript/vendor.js" var="vendorJs"/>
	<spring:url value="/static/dist/css/vendor.css" var="vendorCss"/>
	<spring:url value="/static/dist/css/admin.css" var="adminCss"/>
	<link href="${vendorCss}" rel="stylesheet"/>
	<link href="${adminCss}" rel="stylesheet"/>
	<script src="${vendorJs}"></script>
	<script>
		var map = {
			'app': "${appBaseJavaScript}",
			'rxjs': "${rxjs}",
			'@angular': "${angular2}",
		};

		var packages = {
			'app': {
				main: 'main.js',
				defaultExtension: 'js',
				format: 'register'
			},
			'rxjs': {
				defaultExtension: 'js'
			},
		};

		var packageNames = [
			'@angular/common',
			'@angular/compiler',
			'@angular/core',
			'@angular/http',
			'@angular/platform-browser',
			'@angular/platform-browser-dynamic',
			'@angular/router',
			'@angular/router-deprecated',
			'@angular/testing',
			'@angular/upgrade',
		];

		packageNames.forEach(function(pkgName) {
			packages[pkgName] = {
				main: 'index.js',
				defaultExtension: 'js'
			};
		});

		var config = {
			map: map,
			packages: packages
		};

		System.config(config);

		System.import('app/admin/appAdminBootstrap');
		window.__carmenConfig = {
			appBaseUrl: "${appBase}"
		};
	</script>
</head>
<body>
	<app-admin></app-admin>
</body>
</html>
