export function btnMenuMovil(nav,btn){
    const $nav = document.querySelector(nav);
    const $hamburger = document.querySelector(btn);
    $nav.classList.toggle("show-nav");
    $hamburger.classList.toggle("is-active");
}