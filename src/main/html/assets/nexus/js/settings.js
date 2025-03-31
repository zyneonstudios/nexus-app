function initAppearanceValues() {
    document.querySelector(".appearance-theme").innerText = theme;
    document.querySelector(".appearance-animations").checked = animations;
    document.querySelector(".appearance-renderEffects").checked = renderEffects;
    document.querySelector(".appearance-accentColor").value = accentColor;
    document.querySelector(".appearance-borderRadiusDisplay").value = borderRadius;
    document.querySelector(".appearance-borderRadius").value = borderRadius;
    document.querySelector(".appearance-panelMenuColors").checked = panelMenuColors;
    document.querySelector(".appearance-panelFloating").checked = panelFloating;
    document.querySelector(".appearance-panelInlined").checked = panelInlined;
}

function setAnimations(bool) {
    animations = bool;
    setStorageItem("settings.appearance.animations", bool);
    document.querySelector(".appearance-animations").checked = animations;
    if(!animations) {
        if(!document.head.innerHTML.includes("<style>* { transition: all 0s !important; }</style>")) {
            document.head.innerHTML += "<style>* { transition: all 0s !important; }</style>"
        }
    } else {
        document.head.innerHTML = document.head.innerHTML.replaceAll("<style>* { transition: all 0s !important; }</style>","");
    }
}

function setRenderEffects(bool) {
    renderEffects = bool;
    setStorageItem("settings.appearance.renderEffects", bool);
    document.querySelector(".appearance-renderEffects").checked = renderEffects;
    if(!renderEffects) {
        if(!document.head.innerHTML.includes("<style>:root,[data-bs-theme='light'],[data-bs-theme='dark'] * { box-shadow: none !important; text-shadow: none !important; --bs-box-shadow: none !important; }</style>")) {
            document.head.innerHTML += "<style>:root,[data-bs-theme='light'],[data-bs-theme='dark'] * { box-shadow: none !important; text-shadow: none !important; --bs-box-shadow: none !important; }</style>"
        }
    } else {
        document.head.innerHTML = document.head.innerHTML.replaceAll("<style>:root,[data-bs-theme='light'],[data-bs-theme='dark'] * { box-shadow: none !important; text-shadow: none !important; --bs-box-shadow: none !important; }</style>","");
    }
}

function setAccentColor(color) {
    accentColor = color;
    setStorageItem("settings.appearance.accentColor", color);
    document.querySelector(".appearance-accentColor").value = accentColor;
}

function setBorderRadius(number) {
    if(number>2) {
        number = 2;
    }
    if(number<0) {
        number = 0;
    }
    borderRadius = number;
    setStorageItem("settings.appearance.borderRadius", number);
    document.querySelector(".appearance-borderRadiusDisplay").value = borderRadius;
    document.querySelector(".appearance-borderRadius").value = borderRadius;
    setBorderRadius_dev(number);
}

function setPanelMenuColors(bool) {
    panelMenuColors = bool;
    setStorageItem("settings.appearance.panelMenuColors", bool);
    document.querySelector(".appearance-panelMenuColors").checked = panelMenuColors;
    const panel = document.querySelector(".menu-panel");
    if(panelMenuColors) {
        if(!panel.classList.contains("menu-colors")) {
            panel.classList.add("menu-colors");
        }
    } else {
        if (panel.classList.contains("menu-colors")) {
            panel.classList.remove("menu-colors");
        }

    }
}

function setPanelFloating(bool) {
    panelFloating = bool;
    setStorageItem("settings.appearance.panelFloating", bool);
    document.querySelector(".appearance-panelFloating").checked = panelFloating;
    const panel = document.querySelector(".menu-panel");
    if(panelFloating) {
        if(!panel.classList.contains("floating")) {
            panel.classList.add("floating");
        }
    } else {
        if(!panelInlined) {
            if (panel.classList.contains("floating")) {
                panel.classList.remove("floating");
            }
        }
    }
}

function setPanelInlined(bool) {
    panelInlined = bool;
    setStorageItem("settings.appearance.panelInlined", bool);
    document.querySelector(".appearance-panelInlined").checked = panelInlined;
    const panel = document.querySelector(".menu-panel");
    if(panelInlined) {
        panelMode = "hover"; changePanelMode();
        if(!panel.classList.contains("inlined")) {
            panel.classList.add("inlined");
        }
        if(!panel.classList.contains("floating")) {
            panel.classList.add("floating");
        }
        enableMenu(true);
        enablePanel(true);
    } else {
        if(panel.classList.contains("inlined")) {
            panel.classList.remove("inlined");
            initPanel();
        }
        if(panel.classList.contains("floating")) {
            if(!panelFloating) {
                panel.classList.remove("floating");
            }
        }
    }
}

addEventListener("DOMContentLoaded", () => {
    initAppearanceValues();
});