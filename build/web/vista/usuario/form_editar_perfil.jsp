<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="../includes/head.jsp" %>
<div class="root-container">
    <%@include file="../includes/header.jsp" %>
    <%@include file="../includes/nav.jsp" %>
    <main class="main">
        <form action="<%out.print(BASE_URL);%>/usuario?action=updatePerfil" method="POST" id="form_editar_perfil" enctype="multipart/form-data">
                <div class="form_group legend">
                    <h1>Editar datos de usuario</h1>
                </div>
                <div class="form_group input-correo">
                    <label for="correo">Correo</label>
                    <input type="email" name="correo" id="correo"  
                    required pattern="^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$" 
                    title="*La estructura debe ser correcta" maxlength="255" autocomplete="off"
                    value="${usuario.correo}">
                </div>
                <div class="form_group input-clave">
                    <label for="clave">Clave</label>
                    <input type="password" name="clave" id="clave" 
                           required pattern="^(?=\w*\d)(?=\w*[A-Z])(?=\w*[a-z])\S{8,16}$" maxlength="16" minlength="8" placeholder="Nueva contraseña"
                    title="*Debe tener una mayuscula,una minuscula,un número y entre {8,16 caracteres}" autocomplete="off">
                </div>
                <div class="form_group input-perfil">
                    <span><i class="fas fa-images"></i> Imagen de perfil</span>
                    <input type="file" name="perfil" accept="img/*" id="input_file_perfil" class="none">
                    <label for="input_file_perfil"><img src='${usuario.img}' data-default="<% out.print(BASE_URL); %>/public/res/profile-user.svg" id="img_perfil" title="selecciona una imagen"/></label>
                </div>
                <div class="form_group input-submit">
                    <button type="submit">
                        <i class="fas fa-user-edit"></i> Actualizar datos 
                    </button>
                </div>
            </form>
    </main>
    <aside class="asideRight"></aside>
</div>
<%@include file="../includes/endDoc.jsp" %>
