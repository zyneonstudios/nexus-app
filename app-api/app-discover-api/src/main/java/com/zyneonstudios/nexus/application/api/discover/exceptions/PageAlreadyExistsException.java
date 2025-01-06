package com.zyneonstudios.nexus.application.api.discover.exceptions;

public class PageAlreadyExistsException extends Exception {

    private final String id;
    private final String info;

    public PageAlreadyExistsException(String id, String info) {
        this.id = id;
        if(info.isEmpty()) {
            this.info = null;
        } else {
            this.info = info;
        }
    }

    @Override
    public String getMessage() {
        String message = "The page id '"+id+"' is already registered.";
        if(info!=null) {
            message = message.replace(".",": "+info);
        }
        return message;
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    public String getID() {
        return id;
    }

    public String getInfo() {
        return info;
    }
}