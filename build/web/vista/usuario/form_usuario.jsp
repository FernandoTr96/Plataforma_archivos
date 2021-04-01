<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="../includes/head.jsp" %>
<main class="main_form_usuario">
    <form action="<%out.print(BASE_URL);%>/usuario?action=guardar_usuario" method="POST" id="form_usuario" enctype="multipart/form-data">
        <div class="form_group legend">
            <h1>Formulario de registro</h1>
        </div>
        <div class="form_group input-nombre">
            <input type="text" name="nombre" id="nombre"  
            required pattern="^[A-Za-zÑñÁáÉéÍíÓóÚúÜü\s]+$" 
            title="*No se admiten caracteres o números" maxlength="100" autocomplete="off">
            <label for="nombre">Nombre</label>
        </div>
        <div class="form_group input-paterno">
            <input type="text" name="paterno" id="paterno"  
            required pattern="^[A-Za-zÑñÁáÉéÍíÓóÚúÜü\s]+$" 
            title="*No se admiten caracteres o números" maxlength="100" autocomplete="off">
            <label for="paterno">Apellido paterno</label>
        </div>
        <div class="form_group input-materno">
            <input type="text" name="materno" id="materno" 
            required pattern="^[A-Za-zÑñÁáÉéÍíÓóÚúÜü\s]+$" 
            title="*No se admiten caracteres o números" maxlength="100" autocomplete="off">
            <label for="materno">Apellido materno</label>
        </div>
        <div class="form_group input-correo">
            <input type="email" name="correo" id="correo"  
            required pattern="^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$" 
            title="*La estructura debe ser correcta" maxlength="255" autocomplete="off">
            <label for="correo">Correo</label>
        </div>
        <div class="form_group input-clave">
            <i class="fas fa-eye btn-ver" title="ver clave"></i>
            <input type="password" name="clave" id="clave"  
            required pattern="^(?=\w*\d)(?=\w*[A-Z])(?=\w*[a-z])\S{8,16}$" maxlength="16" minlength="8"
            title="*Debe tener una mayuscula,una minuscula,un número y entre {8,16 caracteres}" autocomplete="off">
            <label for="clave">Clave</label>
        </div>
        <div class="form_group input-perfil">
            <span><i class="fas fa-images"></i> Imagen de perfil</span>
            <input type="file" name="perfil" accept="img/*" id="input_file_perfil">
            <label for="input_file_perfil"><img src='<% out.print(BASE_URL); %>/public/res/profile-user.svg' data-default="<% out.print(BASE_URL); %>/public/res/profile-user.svg" id="img_perfil" title="selecciona una imagen"/></label>
        </div>
        <div class="form_group input-submit">
            <button type="submit">
                <i class="fas fa-user-plus"></i>  Registrarse 
            </button>
        </div>
    </form>
</main>
<%@include file="../includes/endDoc.jsp" %>