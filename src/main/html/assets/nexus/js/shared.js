let menuEnabled = false; let panelEnabled = false; let storage = false;
const root = document.querySelector(':root');
document.addEventListener('contextmenu',function(e){e.preventDefault();});document.addEventListener('dragstart', function(e){e.preventDefault();});

let theme = "dark";
let animations = true;
let renderEffects = true;
let accentColor = "#8732EC";
let borderRadius = 1.0;
let panelMenuColors = false;
let panelFloating = true;
let panelInlined = false;

if(localStorage.getItem("enabled")) {
    storage = true;
}

function initAppearanceSettings() {
    if(getStorageItem("settings.appearance.theme")) {
        theme = getStorageItem("settings.appearance.theme");
    }
    if(getStorageItem("settings.appearance.animations")) {
        animations = getStorageItem("settings.appearance.animations") === "true";
        if(!animations) {
            document.head.innerHTML += "<style>* { transition: all 0s !important; }</style>"
        }
    }
    if(getStorageItem("settings.appearance.renderEffects")) {
        renderEffects = getStorageItem("settings.appearance.renderEffects") === "true";
        if(!renderEffects) {
            document.head.innerHTML += "<style>:root,[data-bs-theme='light'],[data-bs-theme='dark'] * { box-shadow: none !important; text-shadow: none !important; --bs-box-shadow: none !important; }</style>"
        }
    }
    if(getStorageItem("settings.appearance.accentColor")) {
        accentColor = getStorageItem("settings.appearance.accentColor");
    }
    if(getStorageItem("settings.appearance.borderRadius")) {
        borderRadius = parseFloat(getStorageItem("settings.appearance.borderRadius"));
    }
    if (getStorageItem("settings.appearance.panelMenuColors")) {
        panelMenuColors = getStorageItem("settings.appearance.panelMenuColors") === "true";
    }
    if (getStorageItem("settings.appearance.panelFloating")) {
        panelFloating = getStorageItem("settings.appearance.panelFloating") === "true";
    }
    if (getStorageItem("settings.appearance.panelInlined")) {
        panelInlined = getStorageItem("settings.appearance.panelInlined") === "true";
    }
} initAppearanceSettings();

if(getStorageItem("settings.menu.enabled")) {
    menuEnabled = getStorageItem("settings.menu.enabled") === "true";
}

let panelMode = "click";
function initPanel() {
    if(document.querySelector(".menu-panel")) {
        const panel = document.querySelector(".menu-panel");
        const modeIcon = panel.querySelector("i.mode");
        let enable = false;

        if(panelMenuColors) {
            if(!panel.classList.contains("menu-colors")) {
                panel.classList.add("menu-colors");
            }
        }

        if(panelInlined) {
            if(!panel.classList.contains("inlined")) {
                panel.classList.add("inlined");
            }
            if(!panel.classList.contains("floating")) {
                panel.classList.add("floating");
            }
            return;
        }

        if(panelFloating) {
            if(!panel.classList.contains("floating")) {
                panel.classList.add("floating");
            }
        }

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
            enablePanel(true);
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
        const panel = document.querySelector(".menu-panel");
        if(!panel.classList.contains("transition")) {
            panel.classList.add("transition");
        }
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
        if(!panel.classList.contains("transition")) {
            panel.classList.add("transition");
        }
        if(panelMode !== "hover") {
            if (panel.classList.contains('active')) {
                disablePanel(true);
            } else {
                enablePanel(true);
            }
        }
    }
}

function toggleMenu() {
    const menu = document.getElementById("menu");
    if(menu) {
        if(!menu.classList.contains("transition")) {
            menu.classList.add("transition");
        }
        if(menu.classList.contains('active')) {
            disableMenu(true);
        } else {
            enableMenu(true);
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
    if(menuEnabled||panelInlined) {
        enableMenu(false);
    }

    window.matchMedia('(prefers-color-scheme: dark)')
        .addEventListener('change',() => {
            updateTheme();
        })

    updateTheme();
    setBorderRadius_dev(borderRadius);
    setAccentColor_dev(accentColor);
});

function setBorderRadius_dev(r) {
    document.querySelectorAll('[data-bs-theme="dark"]').forEach((e) => {
        applyBorderRadiusToElement(e,r);
    });
    document.querySelectorAll('[data-bs-theme="light"]').forEach((e) => {
        applyBorderRadiusToElement(e,r);
    });
    applyBorderRadiusToElement(root,r);
}

function setAccentColor_dev(c) {
    document.querySelectorAll('[data-bs-theme="dark"]').forEach((e) => {
        applyAccentColorToElement(e,c);
    });
    document.querySelectorAll('[data-bs-theme="light"]').forEach((e) => {
        applyAccentColorToElement(e,c);
    });
    applyAccentColorToElement(root,c);
}

function applyBorderRadiusToElement(e,r) {
    if(e) {
        if (typeof r === 'number') {
            e.style.setProperty("--bs-border-radius-sm", (r * 0.25) + "rem");
            e.style.setProperty("--bs-border-radius-lg", (r * 0.5) + "rem");
            e.style.setProperty("--nex-border-radius", r + "rem");
            e.style.setProperty("--bs-border-radius", r + "rem");
            e.style.setProperty("--bs-border-radius-xl", (r * 1.5) + "rem");
            e.style.setProperty("--bs-border-radius-xxl", (r * 1.75) + "rem");
            e.style.setProperty("--bs-border-radius-2xl", (r * 2) + "rem");
        }
    }
}

function applyAccentColorToElement(e,c) {
    e.style.setProperty("--nex-primary", c);
}