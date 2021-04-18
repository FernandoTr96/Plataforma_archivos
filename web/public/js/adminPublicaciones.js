import {$ajax} from './ajax.js';
import {BASE_URL} from './constantes.js';

export function modal_publicaciones_toggle(modal){
    const $modal = document.getElementById(modal);
    $modal.classList.toggle("_hidden");
}

export function guardar_publicacion(form){
    
    const $form = document.getElementById(form);
    const datos = new FormData($form);
    
    const descripcion_length = $form.descripcion.length;
    
    if(descripcion_length > 1000)
    {
        swal("Descripcion larga","La descripcion es muy larga el maximo es de 1000 caracteres !! Ingrese otra","info");
    }
    else{
        
        swal({
            title: "¿Estas seguro?",
            text: "Se guardaran los cambios en el sistema",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        })
        .then((ok) => {
            if(ok)
            {
                document.querySelector('.loader-publicaciones').classList.remove("none");
                $ajax({
                    url:$form.action,
                    metodo:$form.method,
                    data:datos,
                    cs:(json)=>{

                        if(json.estado === 200){
                            swal("Cambios guardados", "La publicacion se guardo correctamente !!","success");
                            $form.reset();
                            $form.parentElement.classList.toggle("_hidden");
                            listarPublicaciones();
                        }
                        else if(json.estado === 400){
                            swal("Peticion sin cambios", "La peticion se realizo con exito pero no afecto ningun registro, informe sobre este error !!","warning");
                        }
                        else if(json.estado === 500){
                            swal("Error de fechas", "Las fechas no tienen un periodo valido!! \n\
                            *La fecha inicial no puede ser antes de si misma o despues de la fecha de finalizacion.\n\
                            *La fecha final no puede ser antes de la fecha de inicio ni puede ser el mismo dia de la fecha de inicio.\n\
                            ","error");
                        }
                        else if(json.estado === 600){
                            swal("Error titulo","Este titulo ya fue ingresado, ingrese otro porfavor !!","error");
                        }
                        else{
                            swal("Error de modelo","No se pudo ejecutar la funcion, existe algun error en el codigo informe sobre este error !!","error");
                        }
                        document.querySelector('.loader-publicaciones').classList.add("none");
                    },
                    cf:(error)=>{
                        swal("Error de peticion",`Error: ${error} : ${error.text} : ${error.statusText}`,"error");
                    }
                });
            }
        });
    }
}



export function listarPublicaciones(){
    if(document.getElementById("table-publicaciones")){
        
        const $tabla = document.getElementById("table-publicaciones");

        $ajax({
            url:BASE_URL+"/publicaciones?action=listarPublicaciones",
            metodo:"POST",
            cs:(json)=>{
                
                if (json[0].estado === 201)
                {
                    $tabla.insertAdjacentHTML('afterend','<h3 class="error">No hay publicaciones que mostrar...</h3>');
                
                } else if (json.length > 0) {
                   
                    let html = "";

                    json.forEach(publicacion =>{
                        html += `
                            <tr>
                               <td>${publicacion.admin}</td>
                               <td>${publicacion.titulo}</td>
                               <td>${publicacion.fecha_inicio}</td>
                               <td>${publicacion.fecha_final}</td>
                               <td>${publicacion.prioridad}</td>
                               <td>${publicacion.estatus}</td>
                               <td><i class="fas fa-edit btnEditarPublicacion" data-id="${publicacion.id}"></i></td>
                               <td><i class="fas fa-trash-alt btnBorrarPublicacion" data-id="${publicacion.id}" data-titulo="${publicacion.titulo}"></i></td>
                            </tr>
                        `;
                    });

                    if($tabla.nextElementSibling){$tabla.nextElementSibling.remove();}
                    $tabla.querySelector("tbody").innerHTML = html;
                    
                } else {
                    $tabla.insertAdjacentHTML('afterend', `<h3 class="error">${json[0].estado}</h3>`);
                }

            },
            cf:(error)=>{
                swal("Error de peticion",`${error} : ${error.status} : ${error.statusText} : error al ejecutar el codigo no hay json`,"error");
            }
        });
    }
}

export function eliminarPublicacion(btnEliminar){
    
    const id = btnEliminar.dataset.id;
    const titulo = btnEliminar.dataset.titulo;
    const datos = new FormData();
    datos.append("id",id);
    datos.append("titulo",titulo);
    
    swal({
        title: "¿Estas seguro?",
        text: "Si eliminas esta publicacion tambien se eliminara la carpeta y los documentos subidos a esta misma",
        icon: "warning",
        buttons: true,
        dangerMode: true
    })
    .then((ok) => {
        if (ok)
        {
            
            $ajax({
                url:BASE_URL+"/publicaciones?action=eliminarPublicacion",
                metodo:"POST",
                data:datos,
                cs:(json)=>{
                    
                    if(json.estado === 200){
                        swal("Eliminada","La publicacion fue eliminada con exito !!","success");
                        listarPublicaciones();
                    }
                    else if(json.estado === 400){
                        swal("No hubo cambios","La peticion se realizo pero no se elimino la publicacion, informe sobre este error","info");
                    }
                    else{
                        swal("Error","Hubo un error al intentar eliminar la publicacion, informe de este error","error");
                    }
                    
                }
                ,
                cf:(json)=>{
                    swal("Error de peticion",`${json} : ${json.status} : ${json.statusText} : no hay json ?`,"error");
                }
            });

        }
    });
}


export function modal_para_update(form,btnEditar){
    
    const $form = document.getElementById(form);
    $form.parentElement.classList.toggle("_hidden");
    
    const id = btnEditar.dataset.id;
    const datos = new FormData();
    datos.append("id",id);
    
    const action_update = BASE_URL+"/publicaciones?action=updatePublicacion&id="+id;
    
    $ajax({
        url:BASE_URL+"/publicaciones?action=getPublicacion",
        metodo:"POST",
        data:datos,
        cs:(json)=>{

            if(json.estado === 201){
                swal("No hay datos","No se encontraron datos de la publicacion","info");
            }
            else{
                
                $form.action = action_update;
                $form.titulo.value = json.titulo;
                $form.descripcion.value = json.descripcion;
                $form.prioridad.value = json.prioridad;
                $form.fecha_inicial.value = json.fecha_inicio;
                $form.fecha_final.value = json.fecha_final;
                $form.querySelector("button").innerHTML = "<i class='fas fa-retweet'></i> Actualizar";
            }
            
        },
        cf:(error)=>{
            swal("Error de peticion",`${error} : ${error.status} : ${error.statusText}`,"error");
        }
    });
}

export function resetModal(form){
    const $form = document.getElementById(form);
    $form.reset();
    $form.action = BASE_URL+"/publicaciones?action=guardarPublicacion";
}


export function buscadorPublicaciones($form){
    
    const datos = new FormData($form);
    const $tabla = document.getElementById("table-publicaciones");
    
    document.querySelector(".loader-tabla-publicaciones").classList.remove("none");
    
    $ajax({
        url: $form.action,
        metodo: $form.method,
        data: datos,
        cs: (json) => {
            
            if (json[0].estado === 201)
            {
                $tabla.querySelector("tbody").innerHTML = null;
                $tabla.insertAdjacentHTML('afterend','<h3 class="error">No se encontraron resultados...</h3>');
                document.querySelector(".loader-tabla-publicaciones").classList.add("none");

            } else if (json.length > 0) {

                let html = "";

                json.forEach(publicacion => {
                    html += `
                            <tr>
                               <td>${publicacion.admin}</td>
                               <td>${publicacion.titulo}</td>
                               <td>${publicacion.fecha_inicio}</td>
                               <td>${publicacion.fecha_final}</td>
                               <td>${publicacion.prioridad}</td>
                               <td>${publicacion.estatus}</td>
                               <td><i class="fas fa-edit btnEditarPublicacion" data-id="${publicacion.id}"></i></td>
                               <td><i class="fas fa-trash-alt btnBorrarPublicacion" data-id="${publicacion.id}" data-titulo="${publicacion.titulo}"></i></td>
                            </tr>
                        `;
                });
                
                if($tabla.nextElementSibling){$tabla.nextElementSibling.remove();}
                $tabla.querySelector("tbody").innerHTML = html;
                document.querySelector(".loader-tabla-publicaciones").classList.add("none");

            } else {
                $tabla.querySelector("tbody").innerHTML = null;
                $tabla.insertAdjacentHTML('afterend', `<h3 class="error">${json[0].estado}</h3>`);
            }

        }
        ,
        cf: (json) => {
            swal("Error de peticion", `${json} : ${json.status} : ${json.statusText} `, "error");
        }
    });
}