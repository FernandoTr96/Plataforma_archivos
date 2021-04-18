
<%@page import="java.time.LocalDate"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="../includes/head.jsp" %>
<div class="root-container adminPublicaciones-container">
    <%@include file="../includes/header.jsp" %>
    <%@include file="../includes/nav.jsp" %>
    <main class="main">
        <form action="<%out.print(BASE_URL);%>/publicaciones?action=buscarPublicaciones" method="POST" id="buscar_publicacion">
            <div class="buscadorPublicaciones">
                <label for="palabra">Buscar publicaciones</label>
                <input type="search" name="palabra" id="palabra" placeholder="...">
                <button id="btnBuscarPublicacion" title="Buscar"><i class="fas fa-search"></i></button>
                <img src="<%out.print(BASE_URL);%>/public/res/puff.svg" class="loader-tabla-publicaciones none" alt="loader"/>
            </div>
            <div class="nuevaPublicacion">
                <a href="#" title="Nueva publicacion" id="btnModalPublicacion"><i class="fas fa-folder-plus"></i></a>
            </div>
        </form>
        <div class="table-container">
        <table id="table-publicaciones" class="table">
            <thead>
                <tr>
                    <th>Admin</th>
                    <th>Titulo</th>
                    <th>Fecha inicial</th>
                    <th>Fecha final</th>
                    <th>Prioridad</th>
                    <th>Estado</th>
                    <th>Editar</th>
                    <th>Borrar</th>
                </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
        </div>
    </main>
    <!-- modal -->
    <div class="modal _hidden" id="modal-publicaciones">
        <form action="<%out.print(BASE_URL);%>/publicaciones?action=guardarPublicacion" method="POST" id="form-publicaciones">
            <div class="close-modal" title="cerrar modal">
                <i class="fas fa-times"></i>
            </div>
            <div class="legend form_group">
                <h1><i class="fas fa-sticky-note"></i> Nueva publicación</h1>
            </div>
            <div class="titulo form_group">
                <label for="titulo">Titulo de la publicacion</label>
                <input type="text" name="titulo" id="titulo" placeholder="..." required>
            </div>
            <div class="descripcion form_group">
                <textarea id="descripcion" name="descripcion" rows="5" cols="10" placeholder="descripcion" maxlength="1000" required></textarea>
            </div>
            <div class="prioridad form_group">
                <label for="prioridad">Asigna una prioridad</label>
                <select id="prioridad" name="prioridad" required>
                    <option value="normal">Normal</option>
                    <option value="urgente">Urgente</option>
                </select>
            </div>
            <%
                LocalDate hoy = LocalDate.now();
                String fecha = hoy.toString();
            %>
            <div class="fecha_inicial form_group">
                <label for="fecha_inicial">Fecha de inicio</label>
                <input type="date" name="fecha_inicial" id="fecha_inicial" required value="<%out.print(fecha);%>">
            </div>
            <div class="fecha_final form_group">
                <label for="fecha_final">Fecha de finalizacion</label>
                <input type="date" name="fecha_final" id="fecha_final" required>
            </div>
            <div class="submit form_group">
                <button id="btnGuardarPublicacion">
                    <i class="fas fa-save"></i> Guardar
                </button>
                <img src="<%out.print(BASE_URL);%>/public/res/puff.svg" class="loader-publicaciones none" alt="loader"/>
            </div>
        </form>
    </div>
</div>
<%@include file="../includes/endDoc.jsp" %>