document.addEventListener('contextmenu',function(e){e.preventDefault();});document.addEventListener('dragstart', function(e){e.preventDefault();});

let theme = "dark"; let menuEnabled = false; let storage = false;
const root = document.querySelector(':root');

if(localStorage.getItem("enabled")) {
    storage = true;
}

if(getStorageItem("settings.menu.enabled")) {
    menuEnabled = getStorageItem("settings.menu.enabled") === "true";
}

function enableMenu(save) {
    const menu = document.getElementById("menu");
    if(menu) {
        if(!menu.classList.contains('active')) {
            menu.classList.add('active');
            menuEnabled = true;
            if(save) {
                if(save === true) {
                    setStorageItem("settings.menu.enabled", "true");
                }
            }
        }
    }
}

function disableMenu(save) {
    const menu = document.getElementById("menu");
    if(menu) {
        if(menu.classList.contains('active')) {
            menu.classList.remove('active');
            menuEnabled = false;
            if(save) {
                if(save === true) {
                    setStorageItem("settings.menu.enabled", "false");
                }
            }
        }
    }
}

function toggleMenu() {
    const menu = document.getElementById("menu");
    if(menu) {
        if(menu.classList.contains('active')) {
            disableMenu(true);
        } else {
            enableMenu(true);
        }
        console.log(getStorageItem("settings.menu.enabled"));
        if(!menu.classList.contains("transition")) {
            menu.classList.add("transition");
        }
    }
}

function updateTheme() {
    let style = null;
    if(getStorageItem('theme')) {
        style = getStorageItem('theme');
    }
    if ((window.matchMedia('(prefers-color-scheme: dark)').matches||style==="dark")&&style!=="light") {
        theme = "dark";
        root.style.setProperty('--nex-bg', 'black');
    } else {
        theme = "light";
        root.style.setProperty('--nex-bg', 'white');
    }
    document.body.setAttribute('data-bs-theme', theme);
}

function setStorageItem(path,content) {
    if(storage) {
        localStorage.setItem(path,content);
        return true;
    }
    return false;
}

function getStorageItem(path) {
    if(storage) {
        if(localStorage.getItem(path)) {
            return localStorage.getItem(path);
        }
    }
    return undefined;
}

function openNotifications() {
    let notifications = document.getElementById("notifications");
    notifications.querySelector(".offcanvas-body").innerHTML = "";
    notifications.querySelector(".offcanvas-body").innerHTML += "Loading...";
}

addEventListener("DOMContentLoaded", () => {
    if(menuEnabled) {
        enableMenu();
    }

    window.matchMedia('(prefers-color-scheme: dark)')
        .addEventListener('change',() => {
            updateTheme();
        })

    updateTheme();
    root.style.setProperty('--bs-border-radius', '1rem');
});