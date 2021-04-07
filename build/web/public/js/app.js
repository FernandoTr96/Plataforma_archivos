//importar funciones de otros archivos
import {validarCamposLogin,deshabilitaRetroceso} from './login.js';
import {validarCamposRegistro,verImagFormRegistro,registrar_usuario,mostrarClaveFomRegistro} from './form_registro.js';
import {validarCamposRecuperarPwd,enviarCodigo,verificarCodigo,cambiarPwd} from './form_password.js';
import {btnMenuMovil,btnMenuFiltrosMovil} from './home.js';
import {validarCamposPerfil,updatePerfil} from './editar_perfil.js';
import {listarUsuarios,eliminarUsuario,buscarUsuario} from './adminUsuarios.js';

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
});

d.addEventListener('click',e=>{
    //ver contraseñas en form registro
    if(e.target.matches('#form_usuario .input-clave .btn-ver')){
        mostrarClaveFomRegistro(e.target);
    }
    //mostrar menu movil
    if(e.target.matches(".hamburger")|| e.target.matches(".hamburger *")){
        btnMenuMovil(".root-container .nav",".hamburger");
    }
    //menu filtros movil
    if(e.target.matches(".btn-filtro") || e.target.matches(".btn-filtro *")){
       btnMenuFiltrosMovil(".root-container .asideRight",".root-container .btn-filtro");
    }
    //eliminar usuario
    if(e.target.matches("#btnEliminarUsuario")){
        eliminarUsuario(e.target);
    }
});

d.addEventListener('keyup',e=>{
    //buscar usuario al escribir en adminUsuarios
    if(e.target.matches("#usuario")){
        buscarUsuario();
    }
});