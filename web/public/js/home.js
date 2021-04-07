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
