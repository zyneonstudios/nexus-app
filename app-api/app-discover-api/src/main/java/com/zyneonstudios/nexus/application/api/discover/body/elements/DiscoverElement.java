package com.zyneonstudios.nexus.application.api.discover.body.elements;

import com.google.gson.JsonObject;

import java.util.UUID;

public interface DiscoverElement {

    DiscoverElementType getType();
    String getHTML();
    String getJson();
    JsonObject getJsonObject();
    UUID getUUID();

}
