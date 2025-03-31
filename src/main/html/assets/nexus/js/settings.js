function initAppearanceValues() {
    document.querySelector(".appearance-theme").value = theme;
    document.querySelector(".appearance-animations").checked = animations;
    document.querySelector(".appearance-renderEffects").checked = renderEffects;
    document.querySelector(".appearance-customAccentColor").checked = enableCustomAccentColor;
    document.querySelector(".appearance-accentColor").value = accentColor;
    document.querySelector(".appearance-borderRadiusDisplay").value = borderRadius;
    document.querySelector(".appearance-borderRadius").value = borderRadius;
    document.querySelector(".appearance-panelMenuColors").checked = panelMenuColors;
    document.querySelector(".appearance-panelFloating").checked = panelFloating;
    document.querySelector(".appearance-panelInlined").checked = panelInlined;

    if(enableCustomAccentColor) {
        document.querySelector(".color-input").style.display = "flex";
        document.querySelector(".colors").style.display = "none";
    } else {
        document.querySelector(".color-input").style.display = "none";
        document.querySelector(".colors").style.display = "flex";
    }

    if(accentColor.toLowerCase() !== "#8732ec") {
        document.querySelector(".color-input").querySelector("i").style.display = "flex";
    } else {
        document.querySelector(".color-input").querySelector("i").style.display = "none";
    }
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

function setCustomAccentColor(bool) {
    enableCustomAccentColor = bool;
    setStorageItem("settings.appearance.customAccentColor", bool);
    document.querySelector(".appearance-customAccentColor").checked = enableCustomAccentColor;
    if(bool) {
        document.querySelector(".color-input").style.display = "flex";
        document.querySelector(".colors").style.display = "none";
        if(getStorageItem("settings.appearance.accentColor")) {
            accentColor = getStorageItem("settings.appearance.accentColor");
        }
        setAccentColor(accentColor);
    } else {
        document.querySelector(".color-input").style.display = "none";
        document.querySelector(".colors").style.display = "flex";
        if(getStorageItem("settings.appearance.color")) {
            accentColor = getStorageItem("settings.appearance.color");
        } else {
            accentColor = root.style.getPropertyValue('--nex-purple');
        }
        setAccentColor(accentColor);
    }
    document.querySelector(".appearance-accentColor").value = accentColor;
}

function setAccentColor(color) {
    accentColor = color;
    if(enableCustomAccentColor) {
        setStorageItem("settings.appearance.accentColor", color);
    } else {
        setStorageItem("settings.appearance.color", color);
    }
    document.querySelector(".appearance-accentColor").value = accentColor;
    setAccentColor_dev(accentColor);
    if(enableCustomAccentColor) {
        if (accentColor.toLowerCase() !== "#8732ec") {
            document.querySelector(".color-input").querySelector("i").style.display = "flex";
        } else {
            document.querySelector(".color-input").querySelector("i").style.display = "none";
        }
    }
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