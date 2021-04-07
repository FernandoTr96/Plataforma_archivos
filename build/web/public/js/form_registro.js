import {$ajax} from './ajax.js';

export function validarCamposRegistro() {

    //obtenemos todos los campos dentro del form que sean required
    const $campos = document.querySelectorAll("#form_usuario [required]");

    //con un ciclo recorremos cada uno de los input que son required y creamos un span
    //el cual acomodaremos despues del input 
    $campos.forEach(input => {
        //crea un span
        const $span = document.createElement("span");
        //setea un id con valor del nombre del input deben ser iguales
        $span.id = input.name;
        //texto que lleva el espan es el title del input
        $span.textContent = input.title;
        //añadimos clases de estilo de la alerra
        $span.classList.add("error", "none");
        //lo acomodamos despues del label del input
        input.nextElementSibling.insertAdjacentElement("afterend", $span);

        //si esta guardada la contraseña por el navegador y los campos estan llenos les levanto el label
        if (input.value !== "") {
            input.nextElementSibling.classList.remove("labelDown");
            input.nextElementSibling.classList.add("labelUp");
        }
    });


    document.addEventListener("keyup", e => {

        if (e.target.matches("#form_usuario [required]")) {
            //mediante la e del evento que pasamos obtenemos el input e.target y el patron del input que es el atributo
            //e.target: lo que origina el evento en este caso un input que es cuando escribimos
            let input = e.target;
            let patron = input.pattern || input.dataset.pattern;

            //si hay un patron y un valor en el input
            if (patron && input.value !== "") {

                //creamos la regex con el valor del pattern 
                let regex = new RegExp(patron);

                //evaluamos la expresion con la cadena
                if (!regex.exec(input.value)) {

                    //si no coinciden mostramos el error en el span
                    document.querySelector(`#form_usuario span#${input.name}`).classList.remove("none");
                    //como hay contenido en el input subimos el label
                    input.nextElementSibling.classList.remove("labelDown");
                    input.nextElementSibling.classList.add("labelUp");

                } else
                {
                    //si coincide escondemos la alerta
                    document.querySelector(`#form_usuario span#${input.name}`).classList.add("none");
                    input.nextElementSibling.classList.remove("labelDown");
                    input.nextElementSibling.classList.add("labelUp");

                }

                return false;
            }

            //si no hay patron pero es required
            //evaluara si el input tiene datos y de igual forma te avisara si es requerido
            if (!patron) {

                if (input.value === "") {

                    document.querySelector(`#form_usuario span#${input.name}`).classList.remove("none");
                    input.nextElementSibling.classList.remove("labelDown");
                    input.nextElementSibling.classList.add("labelDown");

                } else
                {

                    document.querySelector(`#form_usuario span#${input.name}`).classList.add("none");
                    input.nextElementSibling.classList.remove("labelDown");
                    input.nextElementSibling.classList.add("labelUp");

                }

                return false;
            }

            //si no hay contenido en el input baja el label
            if (patron && input.value === "") {
                input.nextElementSibling.classList.remove("labelUp");
                input.nextElementSibling.classList.add("labelDown");

            }

            //LOS INPUTS DEL FORMULARIO DEBEN TENER EL ATRIBUTO REQUIRED Y DECIDIR 
            //SI TENDRA VALIDACION CON EL PATTERN
            //SI NO TIENE EL REQUIRED ESTA VALIDACION LO IGNORARA Y NO TENDRA QUE SEGUIR UNA VALIDACION
        }
    });
}

export function verImagFormRegistro(inputFile, idImagen) {

    let file = inputFile.files[0];
    let reader = new FileReader();

    reader.onload = function (e) {
        let img = document.getElementById(idImagen);
        img.src = e.target.result;
    };

    reader.readAsDataURL(file);
}

export function mostrarClaveFomRegistro(btnVer) {

    const $clave = document.querySelector("#form_usuario #clave");

    if (btnVer.classList.contains("btn-ver-active")) {
        btnVer.classList.remove("btn-ver-active");
        $clave.type = "password";
    } else
    {
        btnVer.classList.add("btn-ver-active");
        $clave.type = "text";
    }
}


export function registrar_usuario($formulario) {

    const datos = new FormData($formulario);
    const $input_file_img = document.querySelector("#form_usuario .input-perfil label > img");
    const $loader = document.querySelector(".loader-registro");

    swal({
        title: "¿Estas seguro que quieres registrarte?",
        text: "Asegurate de que tu registro haya sido acordado, recuerda que podemos ver lo que haces en el sistema !!",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
    .then((ok) => {
        if(ok) 
        {
            $loader.classList.remove("none");
            $ajax({
            url: $formulario.action,
            metodo: $formulario.method,
            data: datos,
            cs: (data) => {

                if (data.estado === 200)
                {
                    swal("Usuario registrado", "El usuario fue registrado con exito...", "success");
                    $formulario.reset();
                    $input_file_img.src = $input_file_img.dataset.default;
                    $loader.classList.add("none");
                } else if (data.estado === 400)
                {
                    swal("Usuario no registrado", "Hubo un problema al intentar guardar el usuario !! ningun registro fue alterado", "warning");
                } else if (data.estado === 500)
                {
                    swal("Correo duplicado", "El correo ingresado ya esta registrado, utilice otro !!", "error");
                } else if (data.estado === 600) {
                    swal("Usuario duplicado", "El nombre y apellidos ya estan registrados !! tienes una cuenta ?", "info");
                } else {
                    swal("Error", `${data.estado}`, "error");
                }

            },
            cf: (err) => {
                swal("Error de peticion", `Error ${err} : ${err.status} : ${err.text}`, "error");
            }
            });
        }
    });
}

