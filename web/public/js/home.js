import {BASE_URL} from './constantes.js';
import {$ajax} from './ajax.js';

export function btnMenuMovil(nav,btn){
    const $nav = document.querySelector(nav);
    const $hamburger = document.querySelector(btn);
    $nav.classList.toggle("show-nav");
    $hamburger.classList.toggle("is-active");
}

export function btnMenuFiltrosMovil(asideRight,btn){
    const $asideRight = document.querySelector(asideRight);
    const $btn = document.querySelector(btn);
    $asideRight.classList.toggle("show-nav");
    $btn.classList.toggle("filtro-active");
    if($btn.classList.contains("filtro-active")){
        $btn.innerHTML = '<i class="fas fa-times"></i>';
    }
    else{
        $btn.innerHTML = '<i class="fas fa-search"></i>';
    }
}

export function btnLeerMas($btnLeerMas){
    
    const $p = $btnLeerMas.parentElement.querySelector("p");
    $p.classList.toggle("cortar_texto");
    
    if($p.classList.contains("cortar_texto")){
        $btnLeerMas.textContent = "[Leer mas]";   
    }else{
        $btnLeerMas.textContent = "[Leer menos]";
    }
    
}

//variables para infinite scroll al listar en el home
var cantidad = 3;
var index = 0;

export function listarPublicacionesHome(contenedor,template){
    if(document.querySelector(contenedor) && document.getElementById(template)){
        
        const $contenedor = document.querySelector(contenedor);
        const $card = document.getElementById(template).content;
        const datos = new FormData();
        datos.append("index",index);
        datos.append("cantidad",cantidad);
        
        document.querySelector(".loader-home").classList.remove("none");
        $ajax({
            url: BASE_URL+"/publicaciones?action=listarPublicacionesHome",
            metodo:"POST",
            data:datos,
            cs:(json)=>{

                if(json[0].estado === 201){
                    swal("No hay mas publicaciones");
                    document.querySelector(".loader-home").classList.add("none");
                }
                else if(json.length > 0){
                    
                    let fragment  = document.createDocumentFragment();
                    
                    json.forEach(item => {
                        let clon = $card.cloneNode(true);
                        clon.querySelector(".card-header img").src = item.img;
                        clon.querySelector(".card-header img").alt = "foto-perfil";
                        clon.querySelector(".card-header span h5").textContent = item.nombre;
                        clon.querySelector(".card-header span small").textContent = `Publicado ${item.fecha_alta}`;
                        clon.querySelector(".card-body h2").textContent = item.titulo;
                        clon.querySelector(".card-body p").textContent = item.descripcion;
                        clon.querySelector(".card-footer .icono-estado").textContent = item.estatus;
                        clon.querySelector(".card-footer .btnMarcar").dataset.id_publicacion = item.id;
                        clon.querySelector(".card-footer .btnMarcar").dataset.id_usuario = item.id_usuario;
                        clon.querySelector(".card-footer .btnVisualizar").href = BASE_URL+"/publicaciones?action=detalles&id="+item.id;
                        
                        if(item.descripcion.length < 150){
                            clon.querySelector(".card-body a").remove();
                        }
                        
                        if(item.marcada === true){
                            clon.querySelector(".card-footer .btnMarcar").classList.add("marcada");
                            clon.querySelector(".card-footer .btnMarcar").innerHTML = "<i class='fas fa-bookmark'></i> marcada";
                        }
                        
                        if(item.estatus === "abierta"){
                            clon.querySelector(".card-footer .icono-estado").innerHTML = `<i class="fas fa-flag estado-abierta"></i> ${item.estatus}`;
                        }
                        else if(item.estatus === "programada"){
                            clon.querySelector(".card-footer .icono-estado").innerHTML = `<i class="fas fa-flag estado-programada"></i> ${item.estatus}`;
                        }
                        else if(item.estatus === "cerrada"){
                            clon.querySelector(".card-footer .icono-estado").innerHTML = `<i class="fas fa-flag estado-cerrada"></i> ${item.estatus}`;
                        }
                        
                        fragment.appendChild(clon);
                    });
                    
                    $contenedor.appendChild(fragment);
                    index = (index+cantidad);
                    
                    limpiarFiltros();
                    document.querySelector(".loader-home").classList.add("none");
                }
                else{
                    swal("Error","Hay un error en el codigo o la conexion que no deja funcionar correctamente, informe de este error !!","error");
                }
            },
            cf:(error)=>{
                swal("Error de peticion",`Error: ${error} : ${error.status} : ${error.statusText}`,"error");
            }
        });
    }
}


export function buscarPublicacionesHome(contenedor,template,form){
    if(document.querySelector(contenedor)){
       
        const $contenedor = document.querySelector(contenedor);
        const $card = document.getElementById(template).content;
        const datos = new FormData(form);
        const $loader = '<img src="../../public/res/puff.svg" class="loader-home" alt="loader"/>';

        $contenedor.innerHTML = $loader;
        $ajax({
            url: BASE_URL+"/publicaciones?action=buscarPublicacionesHome",
            metodo:"POST",
            data:datos,
            cs:(json)=>{

                if(json[0].estado === 201){
                    $contenedor.innerHTML = "<p class='busqueda-error'><i class='fas fa-search'></i> No se encontraron resultados :( </p>";
                    limpiarFiltros();
                }
                else if(json.length >= 1){
                    
                    let fragment  = document.createDocumentFragment();
                    
                    json.forEach(item => {
                        let clon = $card.cloneNode(true);
                        clon.querySelector(".card-header img").src = item.img;
                        clon.querySelector(".card-header img").alt = "foto-perfil";
                        clon.querySelector(".card-header span h5").textContent = item.nombre;
                        clon.querySelector(".card-header span small").textContent = `Publicado ${item.fecha_alta}`;
                        clon.querySelector(".card-body h2").textContent = item.titulo;
                        clon.querySelector(".card-body p").textContent = item.descripcion;
                        clon.querySelector(".card-footer .icono-estado").textContent = item.estatus;
                        clon.querySelector(".card-footer .btnMarcar").dataset.id = item.id;
                        clon.querySelector(".card-footer .btnVisualizar").dataset.id = item.id;
                        
                        if(item.descripcion.length < 150){
                            clon.querySelector(".card-body a").remove();
                        }
                        
                        if(item.marcada === true){
                            clon.querySelector(".card-footer .btnMarcar").classList.add("marcada");
                            clon.querySelector(".card-footer .btnMarcar").innerHTML = "<i class='fas fa-bookmark'></i> marcada";
                        }
                        
                        if(item.estatus === "abierta"){
                            clon.querySelector(".card-footer .icono-estado").innerHTML = `<i class="fas fa-flag estado-abierta"></i> ${item.estatus}`;
                        }
                        else if(item.estatus === "programada"){
                            clon.querySelector(".card-footer .icono-estado").innerHTML = `<i class="fas fa-flag estado-programada"></i> ${item.estatus}`;
                        }
                        else if(item.estatus === "cerrada"){
                            clon.querySelector(".card-footer .icono-estado").innerHTML = `<i class="fas fa-flag estado-cerrada"></i> ${item.estatus}`;
                        }
                        
                        fragment.appendChild(clon);
                    });
                    
                    $contenedor.innerHTML = null;
                    $contenedor.appendChild(fragment);
                }
                else{
                    swal("Error","Hay un error en el codigo o la conexion que no deja funcionar correctamente, informe de este error !!","error");
                }
            },
            cf:(error)=>{
                swal("Error de peticion",`Error: ${error} : ${error.status} : ${error.statusText}`,"error");
            }
        });
    }
}


export function filtrarPublicacionesHome(contenedor,template,dato){
    if(document.querySelector(contenedor)){
       
        const $contenedor = document.querySelector(contenedor);
        const $card = document.getElementById(template).content;
        const datos = new FormData();
        datos.append("dato",dato);
        const $loader = '<img src="<%out.print(BASE_URL);%>/public/res/puff.svg" class="loader-home" alt="loader"/>';

        $contenedor.innerHTML = $loader;
        $ajax({
            url: BASE_URL+"/publicaciones?action=filtrarPublicacionesHome",
            metodo:"POST",
            data:datos,
            cs:(json)=>{
                
                if(json[0].estado === 201){
                    $contenedor.innerHTML = "<p class='busqueda-error'><i class='fas fa-search'></i> No se encontraron resultados :( </p>";
                    limpiarFiltros();
                }
                else if(json.length >= 1){
                    
                    let fragment  = document.createDocumentFragment();
                    
                    json.forEach(item => {
                        let clon = $card.cloneNode(true);
                        clon.querySelector(".card-header img").src = item.img;
                        clon.querySelector(".card-header img").alt = "foto-perfil";
                        clon.querySelector(".card-header span h5").textContent = item.nombre;
                        clon.querySelector(".card-header span small").textContent = `Publicado ${item.fecha_alta}`;
                        clon.querySelector(".card-body h2").textContent = item.titulo;
                        clon.querySelector(".card-body p").textContent = item.descripcion;
                        clon.querySelector(".card-footer .icono-estado").textContent = item.estatus;
                        clon.querySelector(".card-footer .btnMarcar").dataset.id = item.id;
                        clon.querySelector(".card-footer .btnVisualizar").dataset.id = item.id;
                        
                        if(item.descripcion.length < 150){
                            clon.querySelector(".card-body a").remove();
                        }
                        
                        if(item.marcada === true){
                            clon.querySelector(".card-footer .btnMarcar").classList.add("marcada");
                            clon.querySelector(".card-footer .btnMarcar").innerHTML = "<i class='fas fa-bookmark'></i> marcada";
                        }
                        
                        if(item.estatus === "abierta"){
                            clon.querySelector(".card-footer .icono-estado").innerHTML = `<i class="fas fa-flag estado-abierta"></i> ${item.estatus}`;
                        }
                        else if(item.estatus === "programada"){
                            clon.querySelector(".card-footer .icono-estado").innerHTML = `<i class="fas fa-flag estado-programada"></i> ${item.estatus}`;
                        }
                        else if(item.estatus === "cerrada"){
                            clon.querySelector(".card-footer .icono-estado").innerHTML = `<i class="fas fa-flag estado-cerrada"></i> ${item.estatus}`;
                        }
                        
                        fragment.appendChild(clon);
                    });
                    
                    $contenedor.innerHTML = null;
                    $contenedor.appendChild(fragment);
                }
                else{
                    swal("Error","Hay un error en el codigo o la conexion que no deja funcionar correctamente, informe de este error !!","error");
                }
            },
            cf:(error)=>{
                swal("Error de peticion",`Error: ${error} : ${error.status} : ${error.statusText}`,"error");
            }
        });
    }
}

export function limpiarFiltros(){
    const buscar = document.querySelector("#form-buscar-home #palabra").value = "";
    const prioridad = document.querySelector("#prioridad").selectedIndex = -1;
    const estado = document.querySelector("#estado").selectedIndex = -1;
    const fecha = document.querySelector("#fecha").value = "";
    const orden = document.querySelector("#orden").selectedIndex = -1;
}