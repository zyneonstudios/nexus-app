package com.zyneonstudios.nexus.application.api.discover.body.elements;

import com.google.gson.JsonObject;

import java.util.UUID;

public class DiscoverImage implements DiscoverElement {

    private final JsonObject json;
    private final UUID uuid = UUID.randomUUID();
    private String alt;
    private String src;

    public DiscoverImage() {
        this.json = new JsonObject();
        validateStructure();
    }

    public DiscoverImage(JsonObject json) {
        this.json = json;
        validateStructure();
    }

    private void validateStructure() {
        if(!json.has("type")) {
            json.remove("type");
        }
        json.addProperty("type",getType().toString());

        if(json.has("uuid")) {
            json.remove("uuid");
        }
        json.addProperty("uuid",uuid.toString());

        if(!json.has("alt")) {
            json.addProperty("alt","");
            alt = "";
        } else {
            alt = json.get("alt").getAsString();
        }

        if(!json.has("src")) {
            json.addProperty("src","");
            alt = "";
        } else {
            alt = json.get("src").getAsString();
        }
    }

    @Override
    public DiscoverElementType getType() {
        return DiscoverElementType.IMAGE;
    }

    @Override
    public String getHTML() {
        return "<img alt='"+alt.replace("<","").replace(">","")+"' src='"+src.replace("<","").replace(">","")+"'>";
    }

    @Override
    public String getJson() {
        return json.toString();
    }

    @Override
    public JsonObject getJsonObject() {
        return json;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    public String getAlt() {
        return alt;
    }

    public String getSrc() {
        return src;
    }

    public void setAlt(String alt) {
        this.json.remove("alt");
        this.json.addProperty("alt",alt);
        this.alt = alt;
    }

    public void setSrc(String src) {
        this.json.remove("src");
        this.json.addProperty("src",src);
        this.src = src;
    }
}