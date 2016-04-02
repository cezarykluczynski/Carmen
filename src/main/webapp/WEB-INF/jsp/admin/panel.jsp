<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<head>
	<meta charset="utf-8" />
	<spring:url value="/" var="appBase"/>
	<spring:url value="/frontend/dist/javascript" var="appBaseJavaScript"/>
	<spring:url value="/frontend/dist/javascript/vendor.js" var="vendorJs"/>
	<spring:url value="/frontend/dist/css/vendor.css" var="vendorCss"/>
	<spring:url value="/frontend/dist/css/admin.css" var="adminCss"/>
	<link href="${vendorCss}" rel="stylesheet"/>
	<link href="${adminCss}" rel="stylesheet"/>
	<script src="${vendorJs}"></script>
	<script>
		System.config({
			map: {
				app: "${appBaseJavaScript}"
			},
			packages: {
				app: {
					format: 'register',
					defaultExtension: 'js'
				}
			}
		});
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
