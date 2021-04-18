import {$ajax} from './ajax.js';
import {BASE_URL,DOMINIO} from './constantes.js';


export  function listar_detalles(tabla, id_publicacion) {
    if (document.querySelector(tabla)) {

        const $tabla = document.querySelector(tabla);
        const id = document.getElementById(id_publicacion).textContent;
        const datos = new FormData();
        datos.append("id", id);

        $ajax({
            url: BASE_URL + "/publicaciones?action=tablaDetalles",
            metodo: "POST",
            data: datos,
            cs: (publicacion) => {

                if (publicacion.estado === 201)
                {
                    swal("Error", "No se pudo rellenar la tabla con los datos de la publicacion, falta el id", "error");
                } else
                {
                    document.querySelector(".container-files .carpeta h2").textContent = publicacion.titulo_pub;
                    
                    $tabla.querySelector("tbody").innerHTML = `
                    <tr>
                        <td class="align-left"><i class="fas fa-calendar-day"></i> <b>Fecha de inicio</b></td>
                        <td>${publicacion.fecha_inicio_format}</td>
                    </tr>
                    <tr>
                        <td class="align-left"><i class="fas fa-calendar-week"></i> <b>Fecha de finalizacion</b></td>
                        <td>${publicacion.fecha_final_format}</td>
                    </tr>
                    <tr>
                        <td class="align-left"><i class="fas fa-exclamation-circle ${publicacion.prioridad_clase}"></i> <b>Prioridad</b></td>
                        <td>${publicacion.prioridad}</td>
                    </tr>
                    <tr>
                        <td class="align-left td-estado"><i class="fas fa-exclamation-triangle ${publicacion.estado_clase}"></i> <b>Estado</b></td>
                        <td>${publicacion.estado}</td>
                    </tr>
                    <tr>
                        <td class="align-left"><i class="far fa-clock icon-danger"></i> <b>Tiempo restante</b></td>
                        <td class="icon-danger cuentaAtras"></td>
                    </tr>
                    `;

                    cuentaAtras(
                        id,
                        publicacion.fecha_inicio,
                        publicacion.fecha_final,
                        ".cuentaAtras",
                        "El tiempo se ha terminado...",
                        "En espera hasta la fecha de inicio..."
                    );
            
                    listarArchivos(".lista-archivos",".carpeta h2");
                }
            },
            cf(error) {
                swal("Error de peticion", "La peticion de la tablaDetalles no se realizo: " + error, "error");
            }
        });

    }
}

function cuentaAtras(id_publicacion,fechainicio, fechafinal, contenedor, mensaje_final, mensaje_espera) {

    //contenedor para meter el conteo
    const $cuentaAtras = document.querySelector(contenedor);
    //fecha de inicio
    let fechaInicio = new Date(fechainicio.split('-').join("/")).getTime();
    //fecha a la que hay que llegar
    let fechaLimite = new Date(fechafinal.split('-').join("/")).getTime();

    //intervalo para contar cada segundo
    let intervalo_fechas = setInterval(() => {

        //fecha actual en ms
        let now = new Date().getTime();

        if (now >= fechaInicio) {

            //para saber el tiempo de diferencia entre la fecha que hay que llegar 
            let tiempoLimite = fechaLimite - now;
            //al tener la diferencia en milisegundos hacemos las conversiones
            let dias = Math.floor(tiempoLimite / (1000 * 60 * 60 * 24)),
            horas = ('0' + Math.floor((tiempoLimite % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))).slice(-2),
            minutos = ('0' + Math.floor((tiempoLimite % (1000 * 60 * 60)) / (1000 * 60))).slice(-2),
            segundos = ('0' + Math.floor((tiempoLimite % (1000 * 60)) / (1000))).slice(-2);
            //cambiamos el contenido del div con el conteo en cada segundo
            $cuentaAtras.innerHTML = `${dias} dias ${horas} horas ${minutos} minutos ${segundos} segundos`;

            //checamos que cuando llegue a la fecha se quite el interval y muestre mensaje
            if (tiempoLimite < 0) {
                clearInterval(intervalo_fechas);
                $cuentaAtras.innerHTML = mensaje_final;
                document.querySelector('.dropzone').outerHTML = "<div class='dropzone-block'>No puedes subir mas archivos, el periodo ha vencido !! <i class='fas fa-frown'></i></div>";
                cerrarPublicacion(id_publicacion);
            }
            
        } else
        {
            clearInterval(intervalo_fechas);
            $cuentaAtras.innerHTML = mensaje_espera;
            document.querySelector('.dropzone').outerHTML = "<div class='dropzone-block'>No puedes subir archivos, hasta la fecha de inicio programada !! <i class='fas fa-frown'></i></div>";
        }

    }, 1000);
}


function cerrarPublicacion(id){
    
    const datos = new FormData();
    datos.append("id",id);

    $ajax({
        url:BASE_URL+"/publicaciones?action=cerrarPublicacion",
        metodo:"POST",
        data:datos,
        cs:(json)=>{

            if(json.estado === 200){
                document.querySelector(".td-estado i").classList.add("icon-danger");
                document.querySelector(".td-estado").nextElementSibling.textContent = "CERRADA";
            }
            
            if(json.estado !== 200){
                swal("Error","Error al cerrar la publicacion, no hay id ? informe sobre este error !!","error");
            }
        },
        cf:(error)=>{
            swal("Error de peticion", "Error: " + error, "error");
        }
    });
}

export function listarArchivos(contenedor, titulo) {
    if (document.querySelector(contenedor) && document.querySelector(titulo)) {

        const $contenedor = document.querySelector(contenedor);
        const $titulo = document.querySelector(titulo).textContent;
        const datos = new FormData();
        datos.append("nombre", $titulo);

        $ajax({
            url: BASE_URL + "/publicaciones?action=listarArchivos",
            metodo: "POST",
            data: datos,
            cs: (archivos) => {

                let html = "";
                let img = "";
                
                archivos.forEach(item => {

                    img = asignarImagen(item.nombre);

                    html += `
                        <div class="archivo">
                            <img src="${img}" alt="alt"/>
                            <span class="txt-3" title="${item.nombre}">${item.nombre}</span>
                            <a href="${item.url}" download="${item.nombre}" class="btnDescargarDoc" title="descargar ${item.nombre}"><i class="fas fa-download"></i></a>
                            <a href="#" class="btnBorrarDoc" data-nombre="${item.nombre}" title="eliminar ${item.nombre}"><i class="fas fa-times" data-nombre="${item.nombre}"></i></a>
                        </div>
                    `;
                    
                });
                
                $contenedor.innerHTML = html;
            },
            cf: (error) => {
                swal("Error de peticion", "Error: " + error, "error");
            }
        });

    }
}


function asignarImagen(nombre) {

    let img = "";

    if (nombre.endsWith('.jpg') || nombre.endsWith('.png') || nombre.endsWith('.jpeg') || nombre.endsWith('.svg')) {
        img = BASE_URL+"/public/res/imagen.svg";
    } else if (nombre.endsWith('.xls') || nombre.endsWith('.xlsx') || nombre.endsWith('.csv')) {
        img = BASE_URL+"/public/res/excel.svg";
    } else if (nombre.endsWith('.doc') || nombre.endsWith('.docx')) {
        img = BASE_URL+"/public/res/word.svg";
    } else if (nombre.endsWith('.avi') || nombre.endsWith('.mp4') || nombre.endsWith('.mwv') || nombre.endsWith('.mpeg')) {
        img = BASE_URL+"/public/res/multimedia.svg";
    } else if (nombre.endsWith('.mp3') || nombre.endsWith('.wav') || nombre.endsWith('.wma')) {
        img = BASE_URL+"/public/res/sound.svg";
    } else if (nombre.endsWith('.zip') || nombre.endsWith('.rar') || nombre.endsWith('.tar')) {
        img = BASE_URL+"/public/res/rar.svg";
    } else if (nombre.endsWith('.pdf')) {
        img = BASE_URL+"/public/res/pdf.svg";
    } else if (nombre.endsWith('.pptx') || nombre.endsWith('.pptm') || nombre.endsWith('.ppt')) {
        img = BASE_URL+"/public/res/powerpoint.svg";
    } else if (nombre.endsWith('.txt')) {
        img = BASE_URL+"/public/res/txt.svg";
    } else {
        img = BASE_URL+"/public/res/document.svg";
    }
    
    return img;
}


export function eliminarArchivo(carpeta,nombre){
    if(document.querySelector(carpeta)){
        
        const $carpeta = document.querySelector(carpeta).textContent;
        const datos = new FormData();
        datos.append("carpeta", $carpeta);
        datos.append("nombre", nombre);
        
        swal({
            title: "¿Estas seguro?",
            text: "El documento "+nombre+" sera eliminado !!",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        })
        .then((ok) => {
            if(ok)
            {

                $ajax({
                    url: BASE_URL + "/publicaciones?action=eliminarArchivo",
                    metodo: "POST",
                    data: datos,
                    cs: (json) => {

                        if(json.estado === 200){
                            swal("Archivo eliminado","El archivo "+nombre+" fue eliminado con exito !!","success");
                            listarArchivos(".lista-archivos",".carpeta h2");
                        }
                        else
                        {
                            swal("Error","Error al intentar eliminar el archivo, informe sobre este error ","error");
                        }

                    },
                    cf: (error) => {
                        swal("Error de peticion", "Error: " + error, "error");
                    }
                });  
            }
        });
    }
}

export function procesar_archivos_drop_change(dropzone, input_file) {
    if(document.querySelector(dropzone)){

        const $dropzone = document.querySelector(dropzone);
        const $input = document.getElementById(input_file);

        $dropzone.addEventListener('dragover', e => {
            e.preventDefault();
            $dropzone.classList.add("dropzone-over");
            $dropzone.querySelector("p").innerHTML = "suelta el documento !!";
        });
        
        $dropzone.addEventListener('dragleave', e => {
            e.preventDefault();
            $dropzone.classList.remove("dropzone-over");
            $dropzone.querySelector("p").innerHTML = "Arrastra aqui el documento";
        });
        
        $dropzone.addEventListener('drop', e => {
            e.preventDefault();

            let archivos = e.dataTransfer.files;
            //$input.files = archivos;
            $dropzone.classList.remove("dropzone-over");
            $dropzone.querySelector("p").innerHTML = "Arrastra aqui el documento";

            //ejecutar funcion para enviar los archivos al servidor
            Array.from(archivos).forEach(archivo => {
                subirDocumento(archivo);
            });
            
            listarArchivos(".lista-archivos",".carpeta h2");
        });
       
        $input.addEventListener('change', e => {
            
            //ejecutar funcion para enviar los archivos al servidor
            Array.from($input.files).forEach(archivo => {
                subirDocumento(archivo);
            });
            
            listarArchivos(".lista-archivos",".carpeta h2");
        });
    }
}

export function subirDocumento(archivo){
    
    const titulo = document.querySelector(".carpeta h2").textContent;
    const datos = new FormData();
    datos.append("archivo",archivo);
    datos.append("carpeta",titulo);
    
    document.querySelector(".progreso img").classList.remove("none");
    $ajax({
        url:BASE_URL+"/publicaciones?action=subirDocumento",
        metodo:"POST",
        data:datos,
        cs:(json)=>{
            
           if(json.estado !== 200 && json.estado !== 201){
               swal("Error","No se pudo subir los archivos... [ "+json.estado+" ]","error");
               document.querySelector(".progreso img").classList.add("none");
           }
           
           if(json.estado === 200){
               document.querySelector(".progreso img").classList.add("none");
           }
           
           if(json.estado === 201){
               swal("Atencion","Este documento ya fue subido !!","info");
               document.querySelector(".progreso img").classList.add("none");
           }
        },
        cf:(error)=>{
            swal("Error de peticion", "Error: " + error, "error");
        }
    });
}


export function crearZip(btnCrearZip){
    
    const titulo = document.querySelector(".carpeta h2").textContent;
    const datos = new FormData();
    datos.append("carpeta",titulo);
    const $link = document.createElement("a");
    $link.href = DOMINIO+"/appData/documentos/"+titulo.toUpperCase()+"/"+titulo.toUpperCase()+".zip";
    $link.download = titulo.toUpperCase()+".zip";
    
    btnCrearZip.innerHTML = '<i class="fas fa-spinner"></i> creando...';
    $ajax({
        url:BASE_URL+"/publicaciones?action=crearZip",
        metodo:"POST",
        data:datos,
        cs:(json)=>{
           
           if(json.estado !== 200){
                swal("Error descarga","Error al descargar archivo zip...","error");
           }
           
           if(json.estado === 200){
                
                $link.click();
                btnCrearZip.innerHTML = '<i class="fas fa-folder"></i>  Descargar carpeta';
                eliminarZip(titulo);

           }
           
        },
        cf:(error)=>{
            swal("Error de peticion", "Error: " + error, "error");
        }
    });
}


export function eliminarZip(carpeta){
    
    const datos = new FormData();
    datos.append("carpeta",carpeta);
    
    $ajax({
        url:BASE_URL+"/publicaciones?action=eliminarZip",
        metodo:"POST",
        data:datos,
        cs:(json)=>{
           
           if(json.estado !== 200){
                swal("Error descarga","Error al borrar archivo zip...","error");
           }
           
        },
        cf:(error)=>{
            swal("Error de peticion", "Error: " + error, "error");
        }
    });
}