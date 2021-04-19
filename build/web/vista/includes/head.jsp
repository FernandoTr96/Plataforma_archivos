<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@include file="../../helpers/getTitle.jsp" %>
<%@include file="../../helpers/constantes.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <!--Redireccionar al login cuando la sesion expire-->
        <meta http-equiv="refresh"content="${pageContext.session.maxInactiveInterval}; url=${pageContext.servletContext.contextPath}/index.jsp?reason=expired"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <link rel="shortcut icon" href="<% out.print(BASE_URL); %>/public/res/civil-right.svg"/>
        <link rel="stylesheet" href="<% out.print(BASE_URL); %>/public/lib/hamburgerjs/hamburgers.css"/>
        <link rel="stylesheet" href="<% out.print(BASE_URL); %>/public/lib/fontawesome/css/all.css"/>
        <link rel="stylesheet" href="<% out.print(BASE_URL); %>/public/css/global_config.css"/>
        <link rel="stylesheet" href="<% out.print(BASE_URL); %>/public/css/form_usuario.css"/>
        <link rel="stylesheet" href="<% out.print(BASE_URL); %>/public/css/form_password.css"/>
        <link rel="stylesheet" href="<% out.print(BASE_URL); %>/public/css/home.css"/>
        <link rel="stylesheet" href="<% out.print(BASE_URL); %>/public/css/editar_perfil.css"/>
        <link rel="stylesheet" href="<% out.print(BASE_URL); %>/public/css/adminUsuarios.css"/>
        <link rel="stylesheet" href="<% out.print(BASE_URL); %>/public/css/adminPublicaciones.css"/>
        <link rel="stylesheet" href="<% out.print(BASE_URL); %>/public/css/detalles.css"/>
        <link rel="stylesheet" href="<% out.print(BASE_URL); %>/public/css/accesoDirecto.css"/>
        <title>Juridico | <% out.print(title); %></title>
    </head>
    <body>

