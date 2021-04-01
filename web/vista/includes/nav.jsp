<aside class="nav">
    <% String hola = "holi"; %>
    ${hola}
    <div class="nav-container">
        <img src="${usuario.img}" alt="foto"/>
        <span><i class="fas fa-circle conectado"></i> ${usuario.nombre} ${usuario.paterno} ${usuario.materno}</span>
        <nav class="menu">
            <ul>
                <li><a href="<%out.print(BASE_URL);%>/usuario?action=editarPerfil"><i class="fas fa-id-card"></i> Editar perfil</a></li>
                <li><a href="#"><i class="fas fa-comment-dots"></i> Mensajes</a></li>
                <li><a href="#"><i class="fas fa-bookmark"></i> Acceso rapido</a></li>
                <li><a href="#"><i class="fas fa-file-download"></i> Descargar documentos</a></li>
                <li><a href="#"><i class="fas fa-mail-bulk"></i> Admin publicaciones</a></li>
                <li><a href="#"><i class="fas fa-users-cog"></i> Admin usuarios</a></li>
                <li><a href="<%out.print(BASE_URL);%>/usuario?action=cerrar_sesion"><i class="fas fa-power-off"></i> Cerrar sesión</a></li>
            </ul>
        </nav>
    </div>
</aside>
    