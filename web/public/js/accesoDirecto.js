import {$ajax} from './ajax.js';
import {BASE_URL} from './constantes.js';


export function btnAccesoDirecto($btn){
    
    const id_usuario = $btn.dataset.id_usuario;
    const id_publicacion = $btn.dataset.id_publicacion;
    const datos = new FormData();
    datos.append("id_usuario",id_usuario);
    datos.append("id_publicacion",id_publicacion);
    let _url = "";
    
    
    if($btn.classList.contains("marcada"))
    {
        _url = BASE_URL+"/accesoDirecto?action=desmarcar";
        $btn.classList.remove("marcada");
        $btn.innerHTML = "<i class='far fa-bookmark'></i> marcar";
    }
    else
    {
        _url = BASE_URL+"/accesoDirecto?action=marcar";
        $btn.classList.add("marcada");
        $btn.innerHTML = "<i class='fas fa-bookmark'></i> marcada";
    }

    $ajax({
        url:_url,
        metodo:"POST",
        data:datos,
        cs:(json)=>{
            console.log(json.estado);
        },
        cf:(error)=>{
            swal("Error de peticion", "Error: " + error, "error");
        }
    });
}

export function listarAccesos(contenedor){
    if(document.querySelector(contenedor)){
        
        const $contenedor = document.querySelector(contenedor);
        
        $ajax({
            url:BASE_URL+"/accesoDirecto?action=listarAccesos",
            metodo:"POST",
            cs:(json)=>{
                
                if(json[0].estado === 201){
                    $contenedor.innerHTML = null;
                    $contenedor.innerHTML = "<p class='busqueda-error'>No hay accesos directos...</p>";
                }
                else if(json[0].estado === 400){
                    swal("Error","No se puedieron cargar los accesos directos...","error");
                }
                else{
                    
                    let html = "";
                    let clase = "";
                    $contenedor.innerHTML = null;

                    json.forEach(item =>{

                        if (item.estatus === "abierta") {
                            clase = "icon-success";
                        }

                        if (item.equals === "cerrada") {
                            clase = "icon-danger";
                        }

                        if (item.equals === "programada") {
                            clase = "icon-alert";
                        }

                        html += `
                            <div class="card-main">
                                <div class="card-acceso">
                                    <img src="${BASE_URL}/public/res/folder.svg" alt="icono"/>
                                    <span class="titulo txt-3">${item.titulo}</span>
                                    <span class="fecha_alta">${item.fecha_alta}</span>
                                    <i class="fas fa-plus btnDescripcion" title="ver descripcion"></i>
                                    <a href="${BASE_URL}/publicaciones?action=detalles&id=${item.id_publicacion}" class="btnDetalles" title="ir a la carpeta"><i class="fas fa-link"></i></a>
                                    <i class="fas fa-trash-alt btnBorrarAcceso" title="eliminar acceso directo" data-id="${item.id}"></i>
                                    <i class="fas fa-flag ${clase}" title="${item.estatus}"></i>
                                </div>
                                <div class="descripcion hidde-desc">
                                    <p>${item.descripcion}</p>  
                                </div>
                            </div>
                        `;
                    });

                    $contenedor.innerHTML = html;
                }
            },
            cf:(error)=>{
                swal("Error de peticion", "Error: " + error, "error");
            }
        });
        
    }
}

export function showDescripcion($btn){
    $btn.parentElement.nextElementSibling.classList.toggle("hidde-desc");
}


export function borrarAccesoDirecto($btn){
    
    const id = $btn.dataset.id;
    const datos = new FormData();
    datos.append("id",id);
    
    swal({
            title: "¿Estas seguro?",
            text: "Se eliminara este acceso directo",
            icon: "warning",
            buttons: true,
            dangerMode: false,
    })
    .then((ok) => {
        if(ok)
        {
            $ajax({
                url:BASE_URL+"/accesoDirecto?action=borrarAccesoDirecto",
                metodo:"POST",
                data:datos,
                cs:(json)=>{
                    if(json.estado === 200){
                        swal("ELiminado","Acceso directo eliminado con exito !!","success");
                        listarAccesos(".accesos-container");
                    }
                    else{
                        swal("Error","No se elimino el acceso directo...","error");
                    }
                },
                cf:(error)=>{
                    swal("Error de peticion", "Error: " + error, "error");
                }
            });
        }
    });
}