//importar funciones de otros archivos
import {validarCamposLogin,deshabilitaRetroceso} from './login.js';
import {validarCamposRegistro,verImagFormRegistro,registrar_usuario,mostrarClaveFomRegistro} from './form_registro.js';
import {validarCamposRecuperarPwd,enviarCodigo,verificarCodigo,cambiarPwd} from './form_password.js';
import {btnMenuMovil,btnMenuFiltrosMovil,btnLeerMas,listarPublicacionesHome,buscarPublicacionesHome,filtrarPublicacionesHome} from './home.js';
import {validarCamposPerfil,updatePerfil} from './editar_perfil.js';
import {listarUsuarios,eliminarUsuario,buscarUsuario} from './adminUsuarios.js';
import {modal_publicaciones_toggle,guardar_publicacion,listarPublicaciones,eliminarPublicacion,modal_para_update,resetModal,buscadorPublicaciones} from './adminPublicaciones.js';
import {procesar_archivos_drop_change,listar_detalles,eliminarArchivo,crearZip} from './detalles.js';
import {btnAccesoDirecto,showDescripcion,listarAccesos,borrarAccesoDirecto} from './accesoDirecto.js';

//variables que usare mucho
const d = document;
const w = window;

//eventos donde invocare las funciones para aplicarlas
d.addEventListener('DOMContentLoaded',e=>{
    deshabilitaRetroceso();
    validarCamposRegistro();
    validarCamposLogin();
    validarCamposRecuperarPwd();
    validarCamposPerfil();
    listarUsuarios();
    listarPublicaciones();
    listarPublicacionesHome(".home-publicaciones","tmp-publicacion");
    procesar_archivos_drop_change(".dropzone","input-file");
    listar_detalles(".tabla_detalles","id_publicacion");
    listarAccesos(".accesos-container");
});

d.addEventListener('scroll',e=>{
    
    let {scrollTop,clientHeight,scrollHeight} = document.documentElement;
    
    //detectar final del scroll y mostrar publicaciones
    if((scrollTop + clientHeight) >= scrollHeight){
        
        //valores de los inputs del filtro de busqueda
        if(document.querySelector("#form-buscar-home #palabra")){
            
            const buscar = document.querySelector("#form-buscar-home #palabra").value;
            const prioridad = document.querySelector("#prioridad").value;
            const estado = document.querySelector("#estado").value;
            const fecha = document.querySelector("#fecha").value;
            const orden = document.querySelector("#orden").value;
            
            //console.log(buscar,prioridad,estado,fecha,orden);
            //si los filtros estan vacios podemos hacer infinite scroll con la consulta de inicio
            if(buscar === "" || prioridad === "" || estado === "" || fecha === ""){
                listarPublicacionesHome(".home-publicaciones","tmp-publicacion");
            }
        }
    }
    
});

d.addEventListener('submit',e=>{
    //guardar uruario en formulario de registro
    if(e.target.matches('#form_usuario')){
        e.preventDefault();
       registrar_usuario(e.target);
    }
    
    //enviar codigo al correo electronico
    if(e.target.matches('#form_correo')){
       e.preventDefault();
       enviarCodigo(e.target);
    }
    
    //enviar codigo al correo electronico
    if(e.target.matches('#form_codigo')){
       e.preventDefault();
       verificarCodigo(e.target);
    }
    
    //cambiar contraseña al verificar codigo
    if(e.target.matches('#form_pwd')){
       e.preventDefault();
       cambiarPwd(e.target);
    }
    
    //editar perfil
    if(e.target.matches('#form_editar_perfil')){
       e.preventDefault();
       updatePerfil(e.target);
    }
    
    //guardar publicacion
    if(e.target.matches('#form-publicaciones')){
        e.preventDefault();
        guardar_publicacion("form-publicaciones");
    }
    
    //buscar publicacion
    if(e.target.matches('#buscar_publicacion')){
        e.preventDefault();
        buscadorPublicaciones(e.target);
    }
    
    //buscar publicacion home
    if(e.target.matches('#form-buscar-home')){
        e.preventDefault();
        buscarPublicacionesHome(".home-publicaciones","tmp-publicacion",e.target);
    }
});

d.addEventListener('change',e=>{ 
    //cargar imagen en el formulario de registro
    if(e.target.matches('#form_usuario #input_file_perfil')){
        verImagFormRegistro(e.target,'img_perfil');
    }
    //cargar imagen en el formulario de perfil
    if(e.target.matches('#form_editar_perfil #input_file_perfil')){
        verImagFormRegistro(e.target,'img_perfil');
    }
    //filtrar publicaciones
    if(e.target.matches('#prioridad') || e.target.matches('#estado') || e.target.matches('#fecha') || e.target.matches('#orden')){
        filtrarPublicacionesHome(".home-publicaciones","tmp-publicacion",e.target.value);
    }
});

d.addEventListener('click',e=>{
    //ver contraseñas en form registro
    if(e.target.matches('#form_usuario .input-clave .btn-ver')){
        e.preventDefault();
        mostrarClaveFomRegistro(e.target);
    }
    //mostrar menu movil
    if(e.target.matches(".hamburger")|| e.target.matches(".hamburger *")){
        e.preventDefault();
        btnMenuMovil(".root-container .nav",".hamburger");
    }
    //menu filtros movil
    if(e.target.matches(".btn-filtro") || e.target.matches(".btn-filtro *")){
       e.preventDefault();
       btnMenuFiltrosMovil(".root-container .asideRight",".root-container .btn-filtro");
    }
    //eliminar usuario
    if(e.target.matches("#btnEliminarUsuario")){
        e.preventDefault();
        eliminarUsuario(e.target);
    }
    //abrir modal de publicaciones
    if(e.target.matches("#btnModalPublicacion") || e.target.matches("#btnModalPublicacion *")){
        e.preventDefault();
        modal_publicaciones_toggle("modal-publicaciones");
    }
    //cerrar modal
    if(e.target.matches(".close-modal") || e.target.matches(".close-modal *")){
        e.preventDefault();
        modal_publicaciones_toggle("modal-publicaciones");
        resetModal("form-publicaciones");
    }
    //btneliminar publicacion
    if(e.target.matches(".btnBorrarPublicacion")){
        e.preventDefault();
        eliminarPublicacion(e.target);
    }
    //btnUpdate para abrir el modal con los datos y cambiar el action del form
    if(e.target.matches(".btnEditarPublicacion")){
        e.preventDefault();
        modal_para_update("form-publicaciones",e.target);
    }
    //btn leer mas
    if(e.target.matches("#leermas")){
        e.preventDefault();
        btnLeerMas(e.target);
    }
    //input-file
    if(e.target.matches(".btn-file")){
        document.getElementById("input-file").click();
    }
    
    //eliminar un archivo
    if(e.target.matches(".btnBorrarDoc") || e.target.matches(".btnBorrarDoc *")){
        e.preventDefault();
        eliminarArchivo(".carpeta h2",e.target.dataset.nombre);
    }
    
    //crear zip
    if(e.target.matches(".btnDescargarCarpeta") || e.target.matches(".btnDescargarCarpeta *")){
        crearZip(e.target);
    }
    
    //btn marcar acceso directo
    if(e.target.matches(".btnMarcar")){
        e.preventDefault();
        btnAccesoDirecto(e.target);
    }
    
    //mostrar descripcion en accesos directos
    if(e.target.matches(".btnDescripcion")){
        showDescripcion(e.target);
    }
    
    //borrar acceso directo
    if(e.target.matches(".btnBorrarAcceso")){
        borrarAccesoDirecto(e.target);
    }
});

d.addEventListener('keyup',e=>{
    //buscar usuario al escribir en adminUsuarios
    if(e.target.matches("#usuario")){
        buscarUsuario();
    }
});


