import {$ajax} from './ajax.js';
import {BASE_URL} from './constantes.js';

export function listarUsuarios() {
    if (document.getElementById("table-usuarios")) {
        const $tabla = document.getElementById("table-usuarios");
        $ajax({
            url: BASE_URL + "/usuario?action=listarUsuarios",
            metodo: "POST",
            cs: (json) => {

                if (json[0].estado === 201)
                {
                    $tabla.querySelector("tbody").innerHTML = null;
                    $tabla.insertAdjacentHTML('afterend', '<h3 class="error">No hay registros que mostrar...</h3>');
                } else if (json.length > 0) {

                    let html = "";

                    json.forEach(dato => {
                        html += `
                           <tr>
                             <td><img src="${dato.img}" alt="foto"></td>
                             <td>${dato.nombre.toLowerCase()}</td>
                             <td>${dato.correo}</td>
                             <td>${dato.rol}</td>
                             <td><i class="fas fa-trash-alt" id="btnEliminarUsuario" data-id="${dato.id}" title="Eliminar a ${dato.nombre}"></i></td>
                           </tr>
                        `;
                    });

                    $tabla.querySelector("tbody").innerHTML = html;
                } else {
                    $tabla.insertAdjacentHTML('afterend', `<h3 class="error">${json[0].estado}</h3>`);
                }
            },
            cf: (error) => {
                $tabla.insertAdjacentHTML('afterend', `<h3 class="error">Error ${error.status} : ${error.text} : ${error}</h3>`);
            }
        });
    }
}


export function eliminarUsuario($btnEliminar) {

    const datos = new FormData();
    datos.append("id", $btnEliminar.dataset.id);
    const $loader = document.querySelector(".loader-usuarios");

    swal({
        title: "¿Estas seguro de eliminar este usuario?",
        text: "Este usuario se eliminara para siempre",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
            .then((ok) => {
                if (ok)
                {
                    $loader.classList.remove("none");
                    $ajax({
                        url: BASE_URL + "/usuario?action=eliminarUsuario",
                        metodo: "POST",
                        data: datos,
                        cs: (json) => {
                            console.log(json);
                            if (json.estado === 200) {
                                swal("Eliminado", "El usuario fue eliminado con exito", "success");
                                listarUsuarios();
                                $loader.classList.add("none");
                            } else if (json.estado === 400) {
                                swal("No eliminado", "No se elimino el usuario no hubo cambios !!", "info");
                            } else {
                                swal("Error", `Error: ${json.estado} : ${json.e}`, "error");
                            }
                        },
                        cf: (error) => {
                            swal("Error", `Error: ${error} : ${error.status} : ${error.text}`, "error");
                        }
                    });
                }
            });

}

export function buscarUsuario() {
    
    const $tabla = document.getElementById("table-usuarios");
    const $form = document.getElementById("buscar_usuario");
    const datos = new FormData($form);
    
    $ajax({
        url: BASE_URL + "/usuario?action=buscarUsuario",
        metodo: "POST",
        data: datos,
        cs: (json) => {
            if (json[0].estado === 201)
            {
                $tabla.querySelector("tbody").innerHTML = null;
                $tabla.querySelector("tbody").innerHTML = '<h3 class="error"><i class="fas fa-search"></i> No se encontraron datos...</h3>';
            
            } else if (json.length > 0) {

                let html = "";

                json.forEach(dato => {
                    html += `
                           <tr>
                             <td><img src="${dato.img}" alt="foto"></td>
                             <td>${dato.nombre.toLowerCase()}</td>
                             <td>${dato.correo}</td>
                             <td>${dato.rol}</td>
                             <td><i class="fas fa-trash-alt" id="btnEliminarUsuario" data-id="${dato.id}" title="Eliminar a ${dato.nombre}"></i></td>
                           </tr>
                        `;
                });

                $tabla.querySelector("tbody").innerHTML = html;
            } else {
                $tabla.insertAdjacentHTML('afterend', `<h3 class="error">${json[0].estado}</h3>`);
            }
        },
        cf: (error) => {
            swal("Error de peticion", `Error: ${error} : ${error.status} : ${error.statusText}`,"error");
        }
    });
}