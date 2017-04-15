<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:url var="sendAction" value="/sendMessage"/>

<html>
<head>
    <title>Webspere MQ Send Page</title>

    <spring:url value="/resources/css/bootstrap.css" var="bootstrapCss"/>
    <spring:url value="/resources/css/sendpage.css" var="sendpageCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>
    <link href="${sendpageCss}" rel="stylesheet"/>
</head>
<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Websphere MQ Logger</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <form:form method="POST" action="${sendAction}" modelAttribute="MessageQueueRequest"
                       class="navbar-form navbar-right">
                <div class="form-group">
                    <form:input path="message" id="message" type="text" placeholder="Message" class="form-control"/>
                </div>
                <div class="form-group">
                    <form:input path="queueName" id="queueName" type="text" placeholder="Queue Name"
                                class="form-control"/>
                </div>
                <button type="submit" value="Submit" class="btn btn-success">Send</button>
            </form:form>
        </div>
    </div>
</nav>
<spring:url value="/resources/js/bootstrap.js" var="bootstrapJs"/>
<spring:url value="/resources/js/sendpage.js" var="sendpageJs"/>
<script src="${bootstrapJs}"/>
<script src="${sendpageJs}"/>
</body>
</html>
