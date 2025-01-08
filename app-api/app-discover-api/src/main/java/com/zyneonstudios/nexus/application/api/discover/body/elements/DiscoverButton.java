package com.zyneonstudios.nexus.application.api.discover.body.elements;

import com.google.gson.JsonObject;
import com.zyneonstudios.nexus.application.api.DiscoverAPI;
import com.zyneonstudios.nexus.application.api.discover.events.DiscoverActionEvent;

import java.util.ArrayList;
import java.util.UUID;

public class DiscoverButton implements DiscoverElement {

    private final JsonObject json;
    private final UUID uuid = UUID.randomUUID();
    private final ArrayList<DiscoverActionEvent> actionEvents = new ArrayList<>();
    private String text;
    private String icon;

    public DiscoverButton() {
        this.json = new JsonObject();
        validateStructure();
    }

    public DiscoverButton(JsonObject json) {
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

        if(!json.has("text")) {
            json.addProperty("text","");
            text = "";
        } else {
            text = json.get("text").getAsString();
        }

        if(!json.has("icon")) {
            json.addProperty("icon","");
            icon = "";
        } else {
            icon = json.get("icon").getAsString();
        }
    }

    @Override
    public DiscoverElementType getType() {
        return DiscoverElementType.BUTTON;
    }

    @Override
    public String getHTML() {
        String text = this.text.replace("\"","''").replace("<","‹").replace(">","›");
        if(icon!=null&&!icon.isEmpty()) {
            text += "<i class='"+icon.replace("<","").replace(">","")+"'></i>";
        }
        return "<h3 onclick='connector(`event.discover.action."+uuid+"`);'><i class='"+icon+"'></i> "+text+"</h3>";
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

    public String getText() {
        return text;
    }

    public String getIcon() {
        return icon;
    }

    public void setText(String text) {
        this.json.remove("text");
        this.json.addProperty("text",text);
        this.text = text;
    }

    public void setIcon(String icon) {
        this.json.remove("icon");
        this.json.addProperty("icon",icon);
        this.icon = icon;
    }

    public DiscoverActionEvent[] getActionEvents() {
        return actionEvents.toArray(new DiscoverActionEvent[0]);
    }

    public void addActionEvent(DiscoverActionEvent event) {
        event.bindToElementId(uuid);
        actionEvents.add(event);
        DiscoverAPI.registerEvent(event);
    }

    public void click() {
        actionEvents.forEach(DiscoverActionEvent::execute);
    }
}