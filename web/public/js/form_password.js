import {$ajax} from './ajax.js';
import {BASE_URL} from './constantes.js';

export function validarCamposRecuperarPwd(){
    
    const $campos = document.querySelectorAll(".main_password .form_items input[required]");
    
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
        
        if(e.target.matches(".main_password .form_items form input[required]")){
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
                    document.querySelector(`.main_password .form_items form span#${input.name}`).classList.remove("none");               
                }
                else
                {                    
                    //si coincide escondemos la alerta
                    document.querySelector(`.main_password .form_items form span#${input.name}`).classList.add("none"); 
                } 
                
                return false;
            }

            //si no hay patron pero es required
            //evaluara si el input tiene datos y de igual forma te avisara si es requerido
            if(!patron){
                
                if(input.value === ""){
                    
                  document.querySelector(`.main_password .form_items form span#${input.name}`).classList.remove("none");
                 
                }
                else
                {
                    
                  document.querySelector(`.main_password .form_items form span#${input.name}`).classList.add("none");  
                  
                }
                
                return false;
            }
        }
    });
    
}


export function enviarCodigo(){
    
    const $form = document.getElementById("form_correo");
    const $loader = document.querySelector(".main_password .loader");
    const $tempo = document.querySelector(".main_password .tempo");
    $loader.classList.remove("none");
    const datos = new FormData($form);
    
    $ajax({
        url: $form.action,
        metodo: $form.method,
        data: datos,
        cs: (json)=>{

            if(json.correoEnviado === 200)
            {                
                document.querySelector('.form_container .form_items').classList.add("translate-100");
                $tempo.classList.remove("none");
                timer();
            }
            else if(json.correoEnviado === 201)
            {
                swal("Correo no registrado","Este correo no esta registrado en el sistema","warning");
            }
            else{
                
                swal("Error",`Error al intentar enviar el correo: ${json.correoEnviado}`,"error");
            }
            
            $loader.classList.add("none");
            
        },
        cf: (error)=>{
            
            swal("Error de peticion",`Error ${error.status} : ${error.text} : ${error}`,"error");
            
        }
    });
}

function timer(){
    
    let minutos = 3;
    let segundos = 60;
    const $tempo = document.querySelector(".main_password .tempo");
   
    var interval = setInterval(() => {
      
      if(segundos === 0 && minutos !== 0) { 
        segundos = 60; 
        minutos--; 
      }
      
      if(minutos === 0 && segundos === 0){
        $tempo.innerHTML = "El codigo expiro !!";
        clearInterval(interval);
        resetCodigo();
        document.querySelector('.form_container .form_items').classList.remove("translate-100");
      }

      if(segundos < 10){
        segundos = '0' + segundos;
      }
     
      if(segundos > 0 && minutos >= 0){
        $tempo.innerHTML = `Se ha enviado un codigo a su correo, ingreselo en menos de ${minutos}:${segundos} minutos.`;
        segundos--;
      }
            
    }, 1000);
}

export function verificarCodigo(){
    
    const $form = document.getElementById("form_codigo");
    const $loader = document.querySelector(".main_password .loader");
    const $tempo = document.querySelector(".main_password .tempo");
    $loader.classList.remove("none");
    const datos = new FormData($form);
    
    $ajax({
        url: $form.action,
        metodo: $form.method,
        data: datos,
        cs: (json)=>{
            if(json.estado === 200)
            {                
                document.querySelector('.form_container .form_items').classList.add("translate-200");
                $tempo.classList.add("none");
            }
            else if(json.estado === 201)
            {
                swal("Codigo no encontrado","Este codigo no esta relacionado con el sistema, lo has escrito bien ?","warning");
            }
            else{
                
                swal("Error",`Error al intentar enviar el correo: ${json.correoEnviado}`,"error");
            }
            
            $loader.classList.add("none");
            
        },
        cf: (error)=>{
            
            swal("Error de peticion",`Error ${error.status} : ${error.text} : ${error}`,"error");
            
        }
    });
}

function resetCodigo(){
    
    const correo = document.querySelector("#form_correo #correo").value;
    const datos = new FormData();
    datos.append("correo",correo);
    
    $ajax({
        url: BASE_URL+"usuario?action=resetCodigo",
        metodo: "POST",
        data: datos,
        cs: (json)=>{
            
            if(json.estado !== 200){
                swal("Codigo no reseteado","El codigo no se reseteo al expirar el tiempo, esto puede afectar a la recuperacion de la contraseña, avise sobre este incidente !! Error:"+json.estado,"warning");
            }
            
        },
        cf: (error)=>{
           
            swal("Error en la peticion","La peticion para resetear el codigo ha salido mal... avise sobre este incidente !! Error:"+error,"error");
        }
    });
}

export function cambiarPwd(){
 
    const $form = document.getElementById("form_pwd");
    const $form_email = document.getElementById("form_correo");
    const $form_codigo = document.getElementById("form_codigo");
    const $loader = document.querySelector(".main_password .loader");
    $loader.classList.remove("none");
    const correo = document.querySelector("#form_correo #correo").value;
    const datos = new FormData($form);
    datos.append("correo",correo);
    
    $ajax({
        url: $form.action,
        metodo: $form.method,
        data: datos,
        cs: (json)=>{
            if(json.estado === 200)
            {   
                document.querySelector('.form_container .form_items').classList.remove("translate-200");
                document.querySelector('.form_container .form_items').classList.remove("translate-100");
                $form_email.reset();
                $form_email.reset();
                $form_codigo.reset();
                swal("Contraseña cambiada","La contraseña se reestablecio con exito","success");
            }
            else if(json.estado === 400)
            {
                swal("Contraseña no cambiada","algo sucedio, no se efectuaron los cambios, informe sobre este error !!","warning");
            }
            else{
                
                swal("Error",`Error al intentar cambiar la contraseña: ${json.correoEnviado}`,"error");
            }
            
            $loader.classList.add("none");
            
        },
        cf: (error)=>{
            
            swal("Error de peticion",`Error ${error.status} : ${error.text} : ${error}`,"error");
            
        }
    });
}