function setDownload(title,state,elapsedTime,downloadSpeed,remainingTime,downloadSize,fileSize,path,url,id,progress,percent) {
    const template = document.getElementById("template-download-card");
    let download = template.cloneNode(true);
    if(document.getElementById(id)) {
        download = document.getElementById(id);
    } else {
        download.id = id;
        if(state) {
            if(state==="RUNNING") {
                const doc = document.getElementById("waiting-downloads");
                doc.parentNode.insertBefore(download,doc);
            } else if(state==="WAITING") {
                const doc = document.getElementById("failed-downloads");
                doc.parentNode.insertBefore(download,doc);
            } else if(state==="FAILED") {
                const doc = document.getElementById("finished-downloads");
                doc.parentNode.insertBefore(download,doc);
            } else {
                template.parentNode.insertBefore(download,template);
            }
        } else {
            template.parentNode.insertBefore(download,template);
        }
    }
    download.querySelector(".download-id").innerText = id;

    if(title) {
        download.querySelector(".download-title").innerText = title;
    }

    if(state) {
        download.querySelector(".download-state").innerText = state;
    }

    if(elapsedTime) {
        download.querySelector(".download-elapsed-time").innerText = elapsedTime;
    }

    if(remainingTime) {
        download.querySelector(".download-estimated-time").innerText = remainingTime;
    }

    if(downloadSize) {
        download.querySelector(".download-size").innerText = downloadSize;
    }

    if(fileSize) {
        download.querySelector(".download-file-size").innerText = fileSize;
    }

    if(path) {
        download.querySelector(".download-path").innerText = path;
    }

    if(url) {
        download.querySelector(".download-url").innerText = url;
    }

    if(progress) {
        download.querySelector(".download-progress").innerText = progress;
    }

    if(percent) {
        download.querySelector(".progress-bar").style.width = percent+"%";
    }

    if(downloadSpeed) {
        download.querySelector(".download-speed").innerText = downloadSpeed;
    }
}

function addDownload(title,state,elapsedTime,downloadSpeed,remainingTime,downloadSize,fileSize,path,url,id,progress,percent) {

}

function initDownloads() {
    const urlParams = new URLSearchParams(window.location.search);
    if(urlParams.get("reInit")!=null) {
        if(urlParams.get("reInit")==="false") {
            return;
        }
    }
    console.log("[CONNECTOR] init.downloads");
}
initDownloads();