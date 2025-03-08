let desktop = false;
let colors = "automatic";
let accentColor = "#00000000";

document.addEventListener('contextmenu',function(e){
    e.preventDefault();
});

document.addEventListener('dragstart', function(e){
    e.preventDefault();
});

function init() {
    const urlParams = new URLSearchParams(window.location.search);
    if(location.href.includes("http://localhost:63342/Zyneon-Application/")) {
        if (!urlParams.get("_ij_reload")) {
            if(location.href.includes("?")) {
                location.href = location.href + "&_ij_reload=RELOAD_ON_SAVE";
            } else {
                location.href = location.href + "?_ij_reload=RELOAD_ON_SAVE";
            }
        }
    }
    let theme = colors;
    if(localStorage.getItem("theme.colors")) {
        theme = localStorage.getItem("theme.colors");
    }
    if(urlParams.get("theme.colors")) {
        theme = urlParams.get("theme.colors");
    }
    if(urlParams.get("theme.colors.accent")) {
        setAccentColor(urlParams.get("theme.colors.accent"));
    } else if(localStorage.getItem("theme.colors.accent")) {
        setAccentColor(localStorage.getItem("theme.colors.accent"));
    } else {
        setAccentColor("#8732ec");
    }
    setColors(theme);
}

function connector(request) {
    console.log("[CONNECTOR] " + request);
}

function setColors(newColors,fromApp) {
    colors=newColors;
    localStorage.setItem('theme.colors', newColors);
    if(newColors==="automatic") {
        if (window.matchMedia('(prefers-color-scheme: dark)').matches) {
            document.getElementById("css-colors").href = "../assets/cronos/css/app-colors-dark.css";
            connector("sync.title.automatic-dark-.-" + document.title);
            return;
        }
        document.getElementById("css-colors").href = "../assets/cronos/css/app-colors-light.css";
        addEventListener("DOMContentLoaded", () => {
            const logo = document.querySelector(".menu-title").querySelector("img");
            if(logo) {
                logo.src = logo.src.replace("light.png","dark.png")
            }
        });
        connector("sync.title.automatic-light-.-" + document.title);
        const logo = document.querySelector(".menu-title").querySelector("img");
        if(logo) {
            logo.src = logo.src.replace("light.png","dark.png")
        }
    } else {
        document.getElementById("css-colors").href = newColors;
        connector("sync.title." + colors + "-.-" + document.title);
        addEventListener("DOMContentLoaded", () => {
            if(newColors.includes("light")) {
                const logo = document.querySelector(".menu-title").querySelector("img");
                if (logo) {
                    logo.src = logo.src.replace("light.png", "dark.png")
                }
            }
        });
        if(newColors.includes("light")) {
            const logo = document.querySelector(".menu-title").querySelector("img");
            if(logo) {
                logo.src = logo.src.replace("light.png","dark.png")
            }
        }
    }
}

function setColors_(newColors,fromApp) {
    colors=newColors;
    localStorage.setItem('theme.colors', newColors);
    if(newColors==="automatic") {
        if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
            document.getElementById("css-colors").href = "../assets/cronos/css/app-colors-dark.css";
            connector("sync.title.automatic-dark-.-" + document.title);
        } else {
            document.getElementById("css-colors").href = "../assets/cronos/css/app-colors-light.css";
            connector("sync.title.automatic-light-.-" + document.title);
        }
    } else {
        document.getElementById("css-colors").href = newColors;
        connector("sync.title." + colors + "-.-" + document.title);
    }
}

function changeTheme() {
    if(colors==="../assets/cronos/css/app-colors-dark.css") {
        setColors("../assets/cronos/css/app-colors-light.css");
    } else {
        setColors("../assets/cronos/css/app-colors-dark.css");
    }
}

function syncDesktop() {
    desktop = true;
}

function enableOverlay(url) {
    if(url) {
        if(typeof url === "string") {
            const overlay = document.getElementById("overlay");
            const frame = document.getElementById("overlay-frame");
            frame.src = url;
            if (!overlay.classList.contains("active")) {
                overlay.classList.add("active");
            }
        }
    }
}

function disableOverlay() {
    const overlay = document.getElementById("overlay");
    if(overlay.classList.contains("active")) {
        overlay.classList.remove("active");
    }
    const frame = document.getElementById("overlay-frame");
    frame.src = "";
}

addEventListener("DOMContentLoaded", () => {
    connector("event.shared.page.loaded");
});

function adjustColorBrightness(col, amt) {
    let usePound = false;
    if (col[0] === "#") {
        col = col.slice(1);
        usePound = true;
    }
    const num = parseInt(col, 16);
    let r = (num >> 16) + amt;
    if (r > 255) r = 255;
    else if  (r < 0) r = 0;
    let b = ((num >> 8) & 0x00FF) + amt;
    if (b > 255) b = 255;
    else if  (b < 0) b = 0;
    let g = (num & 0x0000FF) + amt;
    if (g > 255) g = 255;
    else if (g < 0) g = 0;
    return (usePound?"#":"") + (g | (b << 8) | (r << 16)).toString(16);
}

function setAccentColor(color) {
    accentColor = color;

    document.querySelector(':root').style.setProperty('--accent', color);
    document.querySelector(':root').style.setProperty('--accent2', adjustColorBrightness(color,50));
    document.querySelector(':root').style.setProperty('--accent3', color+"90");

    localStorage.setItem("theme.colors.accent",color);
}