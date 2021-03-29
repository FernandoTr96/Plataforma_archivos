<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="../includes/head.jsp" %>
<main class="main_password">
    <div class="form_container">
        <p class="tempo none"></p>
        <div class="form_items">
            <form action="<%out.print(BASE_URL);%>/usuario?action=verificarCorreoCambioPwd" method="POST" id="form_correo">
                <div class="form-group legend">
                    <legend class="title">1.- Comprobar usuario</legend>
                </div>
                <div class="form-group input-correo">
                    <label for="correo" class="subtitle">Correo</label>
                    <input type="email" name="correo" id="correo"
                    required pattern="^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$" 
                    title="*La estructura debe ser correcta" autocomplete="off" autofocus="on" class="input-style">
                </div>
                <div class="form-group btn">
                    <button type="submit" class="btn-form">
                        Enviar codigo
                    </button>
                </div>
            </form>
            <!---->
            <form action="<%out.print(BASE_URL);%>/usuario?action=verificarCodigoCambioPwd" method="POST" id="form_codigo">
                <div class="form-group legend">
                    <legend class="title">2.- Ingresar codigo</legend>
                </div>
                <div class="form-group input-codigo">
                    <label for="codigo" class="subtitle">Codigo</label>
                    <input type="text" name="codigo" id="codigo" required title="*Es un campo requerido">
                </div>
                <div class="form-group btn">
                    <button type="submit" class="btn-form">
                        Verificar
                    </button>
                    <br>
                </div>
            </form>
            <!---->
            <form action="<%out.print(BASE_URL);%>/usuario?action=cambiarPwd" method="POST" id="form_pwd" >
                <div class="form-group legend">
                    <legend>3.- Nueva contraseña</legend>
                </div>
                <div class="form-group input-pwd">
                    <label for="pwd">Contraseña</label>
                    <input type="password" name="pwd" id="pwd" required pattern="^(?=\w*\d)(?=\w*[A-Z])(?=\w*[a-z])\S{8,16}$" maxlength="16" minlength="8"
                    title="*Debe tener una mayuscula,una minuscula,un número y entre {8,16 caracteres}">
                </div>
                <div class="form-group btn">
                    <button type="submit">
                        Cambiar clave
                    </button>
                </div>
            </form>
        </div>
    </div>
    <img src="<%out.print(BASE_URL);%>/public/res/puff.svg" class="loader none" alt="loader"/>
</main>
<%@include file="../includes/endDoc.jsp" %>
