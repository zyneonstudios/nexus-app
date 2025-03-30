document.addEventListener('contextmenu',function(e){e.preventDefault();});document.addEventListener('dragstart', function(e){e.preventDefault();});

let theme = "dark"; let menuEnabled = false; let panelEnabled = false; let storage = false;
const root = document.querySelector(':root');

if(localStorage.getItem("enabled")) {
    storage = true;
}

if(getStorageItem("settings.menu.enabled")) {
    menuEnabled = getStorageItem("settings.menu.enabled") === "true";
}

let panelMode = "click";
function initPanel() {
    if(document.querySelector(".menu-panel")) {
        const panel = document.querySelector(".menu-panel");
        const modeIcon = panel.querySelector("i.mode");
        let enable = false;

        if(getStorageItem("settings.panel.enabled")) {
            panelEnabled = getStorageItem("settings.panel.enabled") === "true";
            enable = getStorageItem("settings.panel.enabled") === "true";
        }

        if (getStorageItem("settings.panel.mode")) {
            panelMode = getStorageItem("settings.panel.mode");
            if (!(panelMode === "click" || panelMode === "pinned" || panelMode === "hover")) {
                setStorageItem("settings.panel.mode", "click");
                panelMode = "click";
            }
        }

        if (panelMode === "click") {
            modeIcon.className = "mode bi bi-pin-angle";
        } else if (panelMode === "pinned") {
            modeIcon.className = "mode bi bi-pin-angle-fill";
            if(enable) {
                enablePanel(true);
            } else {
                disablePanel(true);
            }
        } else if (panelMode === "hover") {
            if(!panel.classList.contains("hover")) {
                panel.classList.add("hover");
            }
            modeIcon.className = "mode bi bi-cursor-fill";
        }
    }
}

function changePanelMode() {
    if(document.querySelector(".menu-panel")) {
        const panel = document.querySelector(".menu-panel");
        const modeIcon = panel.querySelector("i.mode");
        if (panelMode === "click") {
            if(panel.classList.contains("hover")) {
                panel.classList.remove("hover");
            }
            modeIcon.className = "mode bi bi-pin-angle-fill";
            panelMode = "pinned";
        } else if (panelMode === "pinned") {
            if(!panel.classList.contains("hover")) {
                panel.classList.add("hover");
            }
            if(panel.classList.contains("active")) {
                panel.classList.remove("active");
            }
            modeIcon.className = "mode bi bi-cursor-fill";
            panelMode = "hover";
        } else if (panelMode === "hover") {
            if(panel.classList.contains("hover")) {
                panel.classList.remove("hover");
            }
            modeIcon.className = "mode bi bi-pin-angle";
            panelMode = "click";
        }
        setStorageItem("settings.panel.mode", panelMode);
    }
}

function enablePanel(save) {
    const panel = document.querySelector(".menu-panel");
    if(panel) {
        if(!panel.classList.contains('active')) {
            panel.classList.add('active');
            if(save) {
                if(save === true) {
                    setStorageItem("settings.panel.enabled", "true");
                    panelEnabled = true;
                }
            }
        }
    }
}

function disablePanel(save) {
    const panel = document.querySelector(".menu-panel");
    if(panel) {
        if(panel.classList.contains('active')) {
            panel.classList.remove('active');
            if(save) {
                if(save === true) {
                    setStorageItem("settings.panel.enabled", "false");
                    panelEnabled = false;
                }
            }
        }
    }
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
    if(panelMode !== "pinned") {
        enablePanel(true);
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
    if(panelMode !== "pinned") {
        disablePanel(true);
    }
}

function togglePanel() {
    const panel = document.querySelector(".menu-panel");
    if(panel) {
        if(panel.classList.contains('active')) {
            disablePanel(true);
        } else {
            enablePanel(true);
        }
        if(!panel.classList.contains("transition")) {
            panel.classList.add("transition");
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
    if (true||(window.matchMedia('(prefers-color-scheme: dark)').matches||style==="dark")&&style!=="light") {
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
    initPanel();
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