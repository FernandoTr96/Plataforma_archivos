<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="../includes/head.jsp" %>
<div class="root-container adminUsuarios-container">
    <%@include file="../includes/header.jsp" %>
    <%@include file="../includes/nav.jsp" %>
    <main class="main">
        <form id="buscar_usuario">
            <label for="usuario">Buscar un usuario</label>
            <input type="search" name="usuario" id="usuario" placeholder="...">
        </form>
        <div class="table-container">
        <table id="table-usuarios" class="table">
            <thead>
                <tr>
                    <th><img src="<%out.print(BASE_URL);%>/public/res/puff.svg" class="loader-usuarios none" alt="alt"/></th>
                    <th>Nombre</th>
                    <th>Correo</th>
                    <th>rol</th>
                    <th>Borrar</th>
                </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
        <div/>
    </main>
</div>
<%@include file="../includes/endDoc.jsp" %>
