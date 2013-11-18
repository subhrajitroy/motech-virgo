<%@ page language="java" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <%@include file="head.jsp" %>

    <c:if test="${! empty currentModule}">
       ${currentModule.header}
    </c:if>

    <c:if test="${empty currentModule}">
        <script type="text/javascript">
            $(window).load(function() {
                initAngular();
            });
        </script>
    </c:if>
</head>

<body ng-controller="MasterCtrl"  ng-class="showDashboardLogo.backgroudUpDown()">
    <div id="content-header" ng-include="'resources/partials/header.html'"></div>

    <div id="content" class="container-fluid">
        <div class="row-fluid">
            <div id="main-content" class="span10">
                <div>
                    <div class="splash splash-alert">
                        <div class="splash-logo"></div>
                        <div class="clearfix"></div>
                        <div class="splash-loader splash-header-alert">{{msg('security.accessDenied')}}</div>
                        <div class="clearfix"></div>
                        <div class="splash-msg splash-msg-alert">{{msg('security.accessDeniedAlert')}}</div>
                        <div class="clearfix"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div ng-include="'resources/partials/footer.html'"></div>
</body>
</html>
