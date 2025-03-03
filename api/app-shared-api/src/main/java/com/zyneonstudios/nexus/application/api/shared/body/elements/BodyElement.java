package com.zyneonstudios.nexus.application.api.shared.body.elements;

import com.google.gson.JsonObject;

import java.util.UUID;

public interface BodyElement {

    BodyElementType getType();
    String getHTML();
    String getJson();
    JsonObject getJsonObject();
    UUID getUUID();

}
