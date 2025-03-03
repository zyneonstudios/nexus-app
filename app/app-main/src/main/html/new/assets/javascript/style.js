let theme = "../assets/style/themes/default-rounded.css";
let colors = "../assets/style/colors/default-light.css";

function initStyle() {
    if(urlParams.get("theme")) {
        theme = urlParams.get("theme");
    } else if(localStorage.getItem("settings.style.theme")) {
        theme = localStorage.getItem("settings.style.theme");
    }

    if(urlParams.get("colors")) {
        colors = urlParams.get("colors");
    } else if(urlParams.get("color")) {
        colors = urlParams.get("color");
    } else if(localStorage.getItem("settings.style.colors")) {
        colors = localStorage.getItem("settings.style.colors");
    }

    if(document.getElementById("settings.style.theme")) {
        document.getElementById("settings.style.theme").href = theme;
    } else {
        document.head.innerHTML += "<link rel='stylesheet' id='settings.style.theme' href=\""+theme+"\"/>";
    }

    if(document.getElementById("settings.style.colors")) {
        document.getElementById("settings.style.colors").href = colors;
    } else {
        document.head.innerHTML += "<link rel='stylesheet' id='settings.style.colors' href=\""+colors+"\"/>";
    }
}
initStyle();