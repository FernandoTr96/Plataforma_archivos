<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="helpers/getTitle.jsp" %>
<%@include file="helpers/constantes.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <link rel="shortcut icon" href="<% out.print(BASE_URL); %>/public/res/civil-right.svg"/>
        <link rel="stylesheet" href="<% out.print(BASE_URL); %>/public/lib/fontawesome/css/all.css"/>
        <link rel="stylesheet" href="<%out.print(BASE_URL);%>/public/css/global_config.css"/>
        <link rel="stylesheet" href="<%out.print(BASE_URL);%>/public/css/login.css"/>
        <title> Juridico | <% out.print(title); %> </title>
    </head>
    <body>
        <main>
            <form action="<%out.print(BASE_URL);%>/usuario?action=inicio" method="POST" autocomplete="nope" id="form-login">
                <div class="form_group imagen">
                    <img src="<%out.print(BASE_URL);%>/public/res/logo.png" alt="logo"/> 
                </div>
                <div class="form_group input-correo">
                    <div class="form-icon"><i class="fas fa-user"></i></div>
                    <input type="email" placeholder="Correo" name="correo" id="correo" required>
                </div>
                <div class="form_group input-clave">
                    <div class="form-icon"><i class="fas fa-key"></i></div>
                    <input type="password" placeholder="Contraseña" name="clave" id="clave" required>
                </div>
                <div class="form_group error-log">
                    <%
                        HttpSession http = request.getSession();
                    %>
                    <!--Mensajes del login-->
                    <%  if (http.getAttribute("login_user_notfound") != null && http.getAttribute("login_user_notfound").equals(true)) { %>
                    <p class="error mensaje"><% out.print("❌ Este usuario no esta registrado !!"); %></p>
                    <% } else if (http.getAttribute("login_pwd_error") != null && http.getAttribute("login_pwd_error").equals(true)) { %>
                    <p class="error mensaje"><% out.print("❌ Contraseña equivocada !!"); %></p>
                    <% } else { %>
                    <p class="error mensaje none"></p>
                    <% } %>
                    <% http.removeAttribute("login_user_notfound"); %>
                    <% http.removeAttribute("login_pwd_error");%>
                    <!--Mensajes del login-->
                </div>
                <div class="form_group btn-submit">
                    <button type="submit">
                        Iniciar sesión  <i class="fas fa-sign-in-alt"></i>
                    </button>
                </div>
                <div class="form_group links">
                    <a href="usuario?action=registro">Registrate</a>
                    <a href="usuario?action=cambiar_pwd">¿olvidaste tu contraseña?</a>
                </div>
            </form>
        </main>
                    
        <script src="public/js/app.js" type="module"></script>
        <!--evitar que los iconos se reemplacen con svg-->
        <script type="text/javascript">window.FontAwesomeConfig = { autoReplaceSvg: false }</script>
        <script src="public/lib/fontawesome/js/all.js"></script>
    </body>
</html>

