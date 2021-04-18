<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%  String id = request.getParameter("id");  %>

<%@include file="../includes/head.jsp" %>
<div class="root-container detalles">
    <%@include file="../includes/header.jsp" %>
    <%@include file="../includes/nav.jsp" %>
    <main class="main">
        <br/>
        <br/>
        <div class="table-container">
        <table class="table-bordered tabla_detalles">
            <thead>
                <tr>
                    <th colspan="2">DETALLES DE LA PUBLICACIÓN  N° <span id="id_publicacion"><%out.print(id);%></span></th>
                </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
        </div>
        <br/>
        
        <div class="container-files">

            <div class="carpeta">
                <h2></h2>
                <br/>
            </div>
                                                       
            <div class="archivos">
                <div class="lista-archivos"></div>
            </div> 

            <div class="formulario-docs">
                <div class="dropzone">
                    <i class="fas fa-cloud-upload-alt"></i>
                    <p>Arrastra aqui el documento</p>
                    <span>o</span>
                    <button class="btn-file">selecciona un archivo</button>
                    <input type="file" multiple="" class="none" id="input-file">
                </div>
                <div class="progreso">
                    <img class="none" src="<%out.print(BASE_URL);%>/public/res/puff.svg" alt="loader" width="30px" height="30px"/> 
                </div>
                <div class="botonera">
                    <button class="btnDescargarCarpeta">
                        <i class="fas fa-folder"></i>  Descargar carpeta
                    </button>
                </div>
            </div>

        </div>
    </main>
</div>
<%@include file="../includes/endDoc.jsp" %>
