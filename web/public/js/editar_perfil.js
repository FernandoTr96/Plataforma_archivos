import {$ajax} from './ajax.js';

export function validarCamposPerfil(){
    
    //obtenemos todos los campos dentro del form que sean required
    const $campos = document.querySelectorAll("#form_editar_perfil [required]");
    
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
        $span.classList.add("error","none");
        //lo acomodamos despues del label del input
        input.insertAdjacentElement("afterend",$span);
    });
    
    
    document.addEventListener("keyup",e=>{
        
        if(e.target.matches("#form_editar_perfil [required]")){
            //mediante la e del evento que pasamos obtenemos el input e.target y el patron del input que es el atributo
            //e.target: lo que origina el evento en este caso un input que es cuando escribimos
            let input = e.target;
            let patron = input.pattern || input.dataset.pattern;

            //si hay un patron y un valor en el input
            if(patron && input.value!==""){
                
                //creamos la regex con el valor del pattern 
                let regex = new RegExp(patron);
                
                //evaluamos la expresion con la cadena
                if (!regex.exec(input.value)){ 

                    //si no coinciden mostramos el error en el span
                    document.querySelector(`#form_editar_perfil span#${input.name}`).classList.remove("none");
               
                }
                else
                {                    
                    //si coincide escondemos la alerta
                    document.querySelector(`#form_editar_perfil span#${input.name}`).classList.add("none");
                    
                } 
                
                return false;
            }

            //si no hay patron pero es required
            //evaluara si el input tiene datos y de igual forma te avisara si es requerido
            if(!patron){
                
                if(input.value === ""){
                    
                 document.querySelector(`#form_editar_perfil span#${input.name}`).classList.remove("none");
                 
                }
                else
                {
                    
                  document.querySelector(`#form_editar_perfil span#${input.name}`).classList.add("none"); 
                  
                }
                
                return false;
            }
        }
    });
}

export function updatePerfil($formulario){
   
    const datos = new FormData($formulario);
    const $input_file_img = document.querySelector("#form_editar_perfil .input-perfil label > img");
    
    $ajax({
        url: $formulario.action,
        metodo: $formulario.method,
        data: datos,
        cs:(data)=>{
            
            if(data.estado === 200)
            {
                swal("Datos actualizados","Los datos se actualizaron con exito...","success");
                $input_file_img.src = $input_file_img.dataset.default;
                setTimeout(()=>{
                    window.location.reload()
                },3000);
            }
            else if(data.estado === 400)
            {
                swal("Datos no actualizados","Hubo un problema al intentar actualizar los datos !! ningun registro fue alterado","warning");
            }
            else if(data.estado === 500)
            {
                swal("Correo duplicado","El correo ingresado ya esta registrado, utilice otro !!","error");
            }
            else{
                swal("Error",`${data.estado}`,"error");
            }

        },
        cf:(err)=>{
            swal("Error de peticion",`Error ${err} : ${err.status} : ${err.text}`, "error");
        }
    });
}