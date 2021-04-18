<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="../includes/head.jsp" %>
<div class="root-container">
    <%@include file="../includes/header.jsp" %>
    <%@include file="../includes/nav.jsp" %>
    <main class="main home-publicaciones">
        <img src="<%out.print(BASE_URL);%>/public/res/puff.svg" class="loader-home none" alt="loader"/>
    </main>
    <aside class="asideRight">
        <%@include file="../includes/filtros.jsp" %>
    </aside>
    <div class="btn-filtro">
        <i class="fas fa-search"></i>
    </div>
</div>
<!-- template para la estructura de cada post, la etiqueta template no afecta al html solo es una plantilla--> 
<template id="tmp-publicacion">
    <div class="card">
        <div class="card-header">
            <img/>
            <span>
                <h5></h5>
                <small></small>
            </span>
        </div>
        <div class="card-body">
            <h2></h2>
            <p class="cortar_texto"></p>
            <a href="#" id="leermas">[Leer mas]</a>
        </div>
        <div class="card-footer">
            <a href="#" class="btnMarcar" title="Marcar acceso directo"><i class="far fa-bookmark"></i> marcar</a>
            <a href="#" class="btnVisualizar" title="Visualizar"><i class="fas fa-link"></i> ver detalles</a>
            <a href="#" class="icono-estado"></a>
        </div>
    </div>
</template>
<%@include file="../includes/endDoc.jsp" %>
