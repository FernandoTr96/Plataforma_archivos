export function validarCamposLogin(){
    
    const $btnSubmit = document.querySelector("#form-login button");
    const $span_msg = document.querySelector("#form-login .mensaje");
    const regex_correo = /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/g;


    document.addEventListener('keyup',e=>{
        
        if(e.target.matches('#form-login #correo')){
            
            let correo = e.target.value;
            let clave = document.querySelector("#form-login #clave").value;
            
            if(!regex_correo.exec(correo)){
                
                $span_msg.innerHTML = "<p>❌ el correo ingresado no es valido</p>";
                $span_msg.classList.remove("none");
                $btnSubmit.disabled = true;
                $btnSubmit.style.cursor = "not-allowed";
            }
            else{

                $span_msg.innerHTML = "";
                $span_msg.classList.add("none");
                $btnSubmit.disabled = false;
                $btnSubmit.style.cursor = "pointer";
                
                if(clave === "" || clave === null || clave.lenght === 0){
                    $span_msg.innerHTML = "<p>❌ la clave es requerida</p>";
                    $span_msg.classList.remove("none");
                    $btnSubmit.disabled = true;
                    $btnSubmit.style.cursor = "not-allowed";
                }
            }
        }
        
        if(e.target.matches('#form-login #clave')){
            
            let clave = e.target.value;
            
            if(clave === "" || clave === null || clave.lenght === 0){
                $span_msg.innerHTML = "<p>❌ se requiere la contraseña</p>";
                $span_msg.classList.remove("none");
                $btnSubmit.disabled = true;
                $btnSubmit.style.cursor = "not-allowed";
            }
            else{
                $span_msg.innerHTML = "";
                $span_msg.classList.add("none");
                $btnSubmit.disabled = false;
                $btnSubmit.style.cursor = "pointer";
            }
        }
        
    });
    
}

export function deshabilitaRetroceso(){
    
    let path = window.location.pathname;
    
    if(path.includes('/plataforma_archivos/vista/home/')){
        window.location.hash="no-back-button";
        window.location.hash="Again-No-back-button"; //chrome
        window.onhashchange=function(){window.location.hash="";};
    }
}