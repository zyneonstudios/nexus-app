/**
 * @fileoverview This file contains shared JavaScript functions and variables used throughout the NEXUS app.
 */

// --- Global Variables ---

/**
 * The current application version/type (web, desktop, v3.x.x).
 * @type {string}
 */
let version = "Web app";

/**
 * URL parameters from the current document's location.
 * @type {URLSearchParams}
 */
const urlParams = new URLSearchParams(document.location.search);

/**
 * Currently highlighted menu item.
 * @type {HTMLElement|null}
 */
let glow = null;

/**
 * Indicates if the main menu is enabled.
 * @type {boolean}
 */
let menuEnabled = false;

/**
 * Indicates if the side panel is enabled.
 * @type {boolean}
 */
let panelEnabled = false;

/**
 * Indicates if local storage is enabled and available.
 * @type {boolean}
 */
let storage = false;

/**
 * Indicates if the app is running in a standalone application mode.
 * @type {boolean}
 */
let app = false;

/**
 * The root element of the document.
 * @type {HTMLElement}
 */
const root = document.querySelector(':root');

// --- Default Appearance Settings ---

/**
 * The current theme (dark, light, auto).
 * @type {string}
 */
let theme = "dark";

/**
 * Indicates if animations are enabled.
 * @type {boolean}
 */
let animations = true;

/**
 * Indicates if render effects (glass, shadows) are enabled.
 * @type {boolean}
 */
let renderEffects = true;

/**
 * The current accent color (hex or CSS variable).
 * @type {string}
 */
let accentColor = "#8732EC";

/**
 * The current border radius value.
 * @type {number}
 */
let borderRadius = 1.0;

/**
 * Indicates if the panel is floating.
 * @type {boolean}
 */
let panelFloating = true;

/**
 * Indicates if the panel is inlined.
 * @type {boolean}
 */
let panelInlined = false;

/**
 * Indicates if a custom accent color is enabled.
 * @type {boolean}
 */
let enableCustomAccentColor = false;

// --- Event Listeners ---

/**
 * Prevents the default context menu from appearing.
 */
document.addEventListener('contextmenu', function (e) {
    e.preventDefault();
});

/**
 * Prevents the default drag-and-drop behavior.
 */
document.addEventListener('dragstart', function (e) {
    e.preventDefault();
});

// --- Local Storage Check ---

/**
 * Checks if local storage is enabled and sets the 'storage' variable accordingly.
 */
if (localStorage.getItem("enabled") || app) {
    if (localStorage.getItem("enabled").toLowerCase() === "true") {
        storage = true;
    }
}

// --- Appearance Settings Initialization ---

/**
 * Initializes the appearance settings based on stored preferences or default values.
 */
function initAppearanceSettings() {
    // Load theme from storage or use default
    theme = getStorageItem("settings.appearance.theme") || theme;

    // Load animations setting from storage or use default
    if(getStorageItem("settings.appearance.animations")) {
        animations = getStorageItem("settings.appearance.animations") === "true";
    }
    if (!animations) {
        document.head.innerHTML += "<style>* { transition: all 0s !important; }</style>";
    }

    // Load render effects setting from storage or use default
    if(getStorageItem("settings.appearance.renderEffects")) {
        renderEffects = getStorageItem("settings.appearance.renderEffects") === "true";
    }
    if (!renderEffects) {
        document.head.innerHTML += "<style>:root,[data-bs-theme='light'],[data-bs-theme='dark'] * { box-shadow: none !important; text-shadow: none !important; --bs-box-shadow: none !important; }</style>";
    }

    // Load custom accent color setting from storage or use default
    enableCustomAccentColor = getStorageItem("settings.appearance.customAccentColor") === "true" || enableCustomAccentColor;

    // Load accent color from storage or use default
    if (enableCustomAccentColor) {
        accentColor = getStorageItem("settings.appearance.accentColor") || accentColor;
        setStorageItem("settings.appearance.accentColor", accentColor); // Ensure it's stored if it was the default
    } else {
        accentColor = getStorageItem("settings.appearance.color") || accentColor;
        setStorageItem("settings.appearance.color", accentColor); // Ensure it's stored if it was the default
    }

    // Load border radius from storage or use default
    if(getStorageItem("settings.appearance.borderRadius")) {
        borderRadius = parseFloat(getStorageItem("settings.appearance.borderRadius"));
    }

    // Load panel floating setting from storage or use default
    if(getStorageItem("settings.appearance.panelFloating")) {
        panelFloating = getStorageItem("settings.appearance.panelFloating") === "true"
    }

    // Load panel inlined setting from storage or use default
    panelInlined = getStorageItem("settings.appearance.panelInlined") === "true" || panelInlined;
}
initAppearanceSettings();

// --- Menu and Panel Settings ---

/**
 * Loads the menu enabled state from storage or uses default.
 */
menuEnabled = getStorageItem("settings.menu.enabled") === "true" || menuEnabled;

/**
 * The current panel mode (click, pinned, hover).
 * @type {string}
 */
let panelMode = "click";

/**
 * Initializes the panel based on stored preferences or default values.
 */
function initPanel() {
    const panel = document.querySelector(".menu-panel");
    if (!panel) return; // Exit if panel doesn't exist

    const modeIcon = panel.querySelector("i.mode");
    let enable = false;

    // Handle inlined panel
    if (panelInlined) {
        panel.classList.add("inlined", "floating");
        return;
    }

    // Handle floating panel
    if (panelFloating) {
        panel.classList.add("floating");
    }

    // Load panel enabled state from storage or use default
    panelEnabled = getStorageItem("settings.panel.enabled") === "true" || panelEnabled;
    enable = panelEnabled;

    // Load panel mode from storage or use default
    panelMode = getStorageItem("settings.panel.mode") || panelMode;
    if (!(panelMode === "click" || panelMode === "pinned" || panelMode === "hover")) {
        setStorageItem("settings.panel.mode", "click");
        panelMode = "click";
    }

    // Set panel mode icon and behavior
    if (panelMode === "click") {
        modeIcon.className = "mode bi bi-pin-angle";
    } else if (panelMode === "pinned") {
        modeIcon.className = "mode bi bi-pin-angle-fill";
        enablePanel(true); // Ensure panel is enabled
    } else if (panelMode === "hover") {
        panel.classList.add("hover");
        modeIcon.className = "mode bi bi-cursor-fill";
    }
}

/**
 * Changes the panel mode (click, pinned, hover).
 */
function changePanelMode() {
    const panel = document.querySelector(".menu-panel");
    if (!panel) return; // Exit if panel doesn't exist

    const modeIcon = panel.querySelector("i.mode");

    if (panelMode === "click") {
        panel.classList.remove("hover");
        modeIcon.className = "mode bi bi-pin-angle-fill";
        panelMode = "pinned";
    } else if (panelMode === "pinned") {
        panel.classList.add("hover");
        panel.classList.remove("active");
        modeIcon.className = "mode bi bi-cursor-fill";
        panelMode = "hover";
    } else if (panelMode === "hover") {
        panel.classList.remove("hover");
        modeIcon.className = "mode bi bi-pin-angle";
        panelMode = "click";
        enablePanel(true);
    }
    setStorageItem("settings.panel.mode", panelMode);
}

/**
 * Enables the side panel.
 * @param {boolean} save - Whether to save the state to local storage.
 */
function enablePanel(save) {
    const panel = document.querySelector(".menu-panel");
    if (!panel) return; // Exit if panel doesn't exist

    if (!panel.classList.contains('active')) {
        panel.classList.add('active');
        if (save === true) {
            setStorageItem("settings.panel.enabled", "true");
            panelEnabled = true;
        }
    }
}

/**
 * Disables the side panel.
 * @param {boolean} save - Whether to save the state to local storage.
 */
function disablePanel(save) {
    const panel = document.querySelector(".menu-panel");
    if (!panel) return; // Exit if panel doesn't exist

    if (panel.classList.contains('active')) {
        panel.classList.remove('active');
        if (save === true) {
            setStorageItem("settings.panel.enabled", "false");
            panelEnabled = false;
        }
    }
}

/**
 * Enables the main menu.
 * @param {boolean} save - Whether to save the state to local storage.
 */
function enableMenu(save) {
    const menu = document.getElementById("menu");
    if (!menu) return; // Exit if menu doesn't exist

    if (!menu.classList.contains('active')) {
        menu.classList.add('active');
        menuEnabled = true;
        if (save === true) {
            setStorageItem("settings.menu.enabled", "true");
        }
    }
    if (panelMode !== "pinned") {
        enablePanel(true);
    }
}

/**
 * Disables the main menu.
 * @param {boolean} save - Whether to save the state to local storage.
 */
function disableMenu(save) {
    const menu = document.getElementById("menu");
    if (!menu) return; // Exit if menu doesn't exist

    if (menu.classList.contains('active')) {
        menu.classList.remove('active');
        menuEnabled = false;
        if (save === true) {
            setStorageItem("settings.menu.enabled", "false");
        }
    }
    if (panelMode !== "pinned") {
        disablePanel(true);
    }
}

/**
 * Toggles the side panel's visibility.
 */
function togglePanel() {
    const panel = document.querySelector(".menu-panel");
    if (!panel) return; // Exit if panel doesn't exist

    if (panelMode !== "hover") {
        if (panel.classList.contains('active')) {
            disablePanel(true);
        } else {
            enablePanel(true);
        }
    }
}

/**
 * Toggles the main menu's visibility.
 */
function toggleMenu() {
    const menu = document.getElementById("menu");
    if (!menu) return; // Exit if menu doesn't exist

    if (menu.classList.contains('active')) {
        disableMenu(true);
    } else {
        enableMenu(true);
    }
}

// --- Theme Management ---

/**
 * Updates the application's theme based on the 'theme' variable.
 */
function updateTheme() {
    let style = theme;
    if (style === "auto" || style === "automatic") {
        style = window.matchMedia('(prefers-color-scheme: dark)').matches ? "dark" : "light";
    }

    root.style.setProperty('--nex-bg', style === "light" ? 'white' : 'black');
    console.log(`[CONNECTOR] event.theme.changed.${style}`);
    document.body.setAttribute('data-bs-theme', style);
    setStorageItem("settings.appearance.theme", theme);
}

// --- Local Storage Helpers ---

/**
 * Sets an item in local storage if storage is enabled.
 * @param {string} path - The key for the storage item.
 * @param {string} content - The value to store.
 * @returns {boolean} - True if the item was stored, false otherwise.
 */
function setStorageItem(path, content) {
    if (storage) {
        localStorage.setItem(path, content);
        return true;
    }
    return false;
}

/**
 * Gets an item from local storage if storage is enabled.
 * @param {string} path - The key for the storage item.
 * @returns {string|undefined} - The stored value or undefined if not found.
 */
function getStorageItem(path) {
    if (storage) {
        return localStorage.getItem(path);
    }
    return undefined;
}

// --- Notifications ---

/**
 * Opens the notifications panel (if in app mode).
 */
function openNotifications() {
    if (app) {
        const notifications = document.getElementById("notifications");
        notifications.querySelector(".offcanvas-body").innerHTML = "Loading...";
        new bootstrap.Offcanvas(notifications).show("notificationsLabel");
    }
}

// --- DOMContentLoaded Event ---

/**
 * Initializes the application after the DOM is fully loaded.
 */
addEventListener("DOMContentLoaded", () => {
    initPanel();
    if (menuEnabled || panelInlined) {
        enableMenu(false);
    }

    if (renderEffects) {
        const panel = document.querySelector(".menu-panel");
        if (panel) {
            panel.classList.add("glass");
        }
    }

    window.matchMedia('(prefers-color-scheme: dark)')
        .addEventListener('change', () => {
            updateTheme();
        });

    updateTheme();
    setBorderRadius_dev(borderRadius);
    setAccentColor_dev(accentColor);

    // Load a page if specified in the URL
    if (urlParams.has("page")) {
        const page = urlParams.get("page");
        const menu = urlParams.has("menu") ? urlParams.get("menu") === "true" : false;
        loadPage(page, menu);
        try {
            highlight(document.getElementById(page.toLowerCase().replace(".html", "-button")));
        } catch (ignore) { }
    } else {
        loadPage("loading.html", false);
    }

    // Apply bottom border if specified in the URL
    if (urlParams.has("bottom-border") && urlParams.get("bottom-border") === "true") {
        const content = document.getElementById("content");
        content.style.borderBottomLeftRadius = "calc(var(--nex-border-radius) * 1.5)";
        content.style.borderBottom = "1px solid var(--bs-border-color)";
        document.querySelector(".menu-panel").classList.add("always-floating");
    }

    // Set app mode if specified in the URL
    if (urlParams.has("app") && urlParams.get("app") === "true") {
        app = true;
    }

    // Hide titlebar if specified in the URL
    const titlebar = !(urlParams.has("titlebar") && urlParams.get("titlebar") === "false");
    if (!app && titlebar) {
        document.getElementById("titlebar").style.display = "unset";
    }

    // Show cookie consent toast if storage is not enabled
    if (!storage && !app) {
        const toastEl = document.querySelector('.toast');
        new bootstrap.Toast(toastEl, { autohide: false }).show();
    }

    // Add devtools button if enabled in storage
    if (getStorageItem("devtools") === "true") {
        const buttons = document.querySelector(".menu-panel .card-header .buttons");
        buttons.innerHTML = "<i class='bi bi-arrow-clockwise' onClick='location.reload();'></i>" + buttons.innerHTML;
    }
});

// --- Appearance Settings Helpers ---

/**
 * Applies the border radius to the given element and its children.
 * @param {HTMLElement} e - The element to apply the border radius to.
 * @param {number} r - The border radius value.
 */
function setBorderRadius_dev(r) {
    document.querySelectorAll('[data-bs-theme="dark"]').forEach((e) => {
        applyBorderRadiusToElement(e, r);
    });
    document.querySelectorAll('[data-bs-theme="light"]').forEach((e) => {
        applyBorderRadiusToElement(e, r);
    });
    applyBorderRadiusToElement(root, r);
}

/**
 * Applies the accent color to the given element and its children.
 * @param {HTMLElement} e - The element to apply the accent color to.
 * @param {string} c - The accent color value.
 */
function setAccentColor_dev(c) {
    document.querySelectorAll('[data-bs-theme="dark"]').forEach((e) => {
        applyAccentColorToElement(e, c);
    });
    document.querySelectorAll('[data-bs-theme="light"]').forEach((e) => {
        applyAccentColorToElement(e, c);
    });
    applyAccentColorToElement(root, c);
}

/**
 * Applies the border radius to a single element.
 * @param {HTMLElement} e - The element to apply the border radius to.
 * @param {number} r - The border radius value.
 */
function applyBorderRadiusToElement(e, r) {
    if (e && typeof r === 'number') {
        e.style.setProperty("--bs-border-radius-sm", (r * 0.25) + "rem");
        e.style.setProperty("--bs-border-radius-lg", (r * 0.5) + "rem");
        e.style.setProperty("--nex-border-radius", r + "rem");
        e.style.setProperty("--bs-border-radius", r + "rem");
        e.style.setProperty("--bs-border-radius-xl", (r * 1.5) + "rem");
        e.style.setProperty("--bs-border-radius-xxl", (r * 1.75) + "rem");
        e.style.setProperty("--bs-border-radius-2xl", (r * 2) + "rem");
    }
}

/**
 * Applies the accent color to a single element.
 * @param {HTMLElement} e - The element to apply the accent color to.
 * @param {string} c - The accent color value.
 */
function applyAccentColorToElement(e, c) {
    if (e) {
        e.style.setProperty("--nex-primary", c);
    }
}

// --- Page Loading ---

/**
 * Loads a page into the content area.
 * @param {string} page - The name of the page to load (e.g., "settings.html").
 * @param {boolean} menu - Whether to enable the menu after loading the page.
 */
function loadPage(page, menu) {
    const contentDiv = document.getElementById('content');
    document.querySelector("#page-specific").innerHTML = "";
    fetch(page)
        .then(response => response.text())
        .then(html => {
            contentDiv.innerHTML = html;
        })
        .then(() => {
            try {
                document.querySelector(".menu-panel").querySelector(".card-body").innerHTML = "<i onclick='window.open(`https://discord.gg/Awwh6JrJBS`,`_blank`);' class='bi bi-discord'></i><i onclick='window.open(`https://github.com/zyneonstudios/nexus-app`,`_blank`);' class='bi bi-github'></i><i onclick='window.open(`https://nexus.zyneonstudios.org/app`,`_blank`);' class='bi bi-globe'></i><i onclick='console.log(`[CONNECTOR] exit`)' class='bi bi-door-open'></i>";
            } catch (ignore) {}
            if (page === "settings.html") {
                if (urlParams.has("bottom-border") && urlParams.get("bottom-border") === "true") {
                    const floatingSwitch = document.querySelector(".floating-switch");
                    if (floatingSwitch) {
                        floatingSwitch.remove();
                    }
                }
            }
            const onloadElement = contentDiv.querySelector('.onload');
            if (onloadElement) {
                onloadElement.click(); // Trigger onload event if present
            }
        })
        .catch(error => {
            console.error('Error:', error);
            contentDiv.innerHTML = "<h3 class='p-4 text-danger-emphasis'>" + error + "</h3>";
        });

    if (menu) {
        enableMenu(true);
    } else {
        disableMenu(true);
    }
    window.history.pushState({}, document.title, window.location.pathname + "?page=" + page);
    console.log("[CONNECTOR] event.page.loaded");
}

/**
 * Loads a new javascript file and initializes it asynchronously.
 * @param {string} url - The URL of the new javascript file.
 * @param {function} callback - A function to be called after the script has been loaded successfully
 */
function loadScript(url, callback) {
    const script = document.createElement('script');
    script.src = url;
    script.type = 'text/javascript';
    script.onload = function() {
        if (callback) {
            callback();
        }
    };
    script.onerror = function() {
        console.error(`Couldn't load script ${url}.`);
    };
    document.querySelector("#page-specific").appendChild(script);
}

// --- Menu Highlighting ---

/**
 * Highlights the currently active menu item.
 * @param {HTMLElement} element - The menu item to highlight.
 */
function highlight(element) {
    if (glow) {
        glow.classList.remove("active");
    }
    element.classList.add("active");
    glow = element;
}

/**
 * Enabled or disabled the testing/dev tools
 * @param {boolean} enable - Whether to enable or disable the testing/dev tools
 */
function enableDevTools(enable) {
    if(localStorage.getItem("enabled")) {
        let tools = false;
        if(getStorageItem("devtools")) {
            tools = getStorageItem("devtools") === "true";
        }
        setStorageItem("devtools",enable+"");
        if(tools!==enable) {
            location.reload();
        }
    }
}