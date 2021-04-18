<aside class="nav">
    <div class="nav-container">
        <img src="${usuario.img}" alt="foto"/>
        <span><i class="fas fa-circle conectado"></i> ${usuario.nombre} ${usuario.paterno} ${usuario.materno}</span>
        <nav class="menu">
            <ul>
                <li><a href="<%out.print(BASE_URL);%>/vista/home/?action=home"><i class="fas fa-house-user"></i> Inicio</a></li>
                <li><a href="<%out.print(BASE_URL);%>/usuario?action=editarPerfil"><i class="fas fa-id-card"></i> Editar perfil</a></li>
                <li><a href="<%out.print(BASE_URL);%>/accesoDirecto?action=accesoDirecto"><i class="fas fa-bookmark"></i> Acceso directo <b class="num_acc"></b></a></li>
                <li><a href="<%out.print(BASE_URL);%>/publicaciones?action=adminPublicaciones"><i class="fas fa-mail-bulk"></i> Admin publicaciones</a></li>
                <li><a href="<%out.print(BASE_URL);%>/usuario?action=adminUsuarios"><i class="fas fa-users-cog"></i> Admin usuarios</a></li>
                <li><a href="<%out.print(BASE_URL);%>/usuario?action=cerrar_sesion"><i class="fas fa-power-off"></i> Cerrar sesión</a></li>
            </ul>
        </nav>
    </div>
</aside>
    