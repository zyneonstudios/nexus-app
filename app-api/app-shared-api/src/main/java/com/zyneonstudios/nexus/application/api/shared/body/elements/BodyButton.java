package com.zyneonstudios.nexus.application.api.shared.body.elements;

import com.google.gson.JsonObject;
import com.zyneonstudios.nexus.application.api.SharedAPI;
import com.zyneonstudios.nexus.application.api.shared.events.ElementActionEvent;

import java.util.ArrayList;
import java.util.UUID;

public class BodyButton implements BodyElement {

    private final JsonObject json;
    private final UUID uuid = UUID.randomUUID();
    private final ArrayList<ElementActionEvent> actionEvents = new ArrayList<>();
    private String text;
    private String icon;

    public BodyButton() {
        this.json = new JsonObject();
        validateStructure();
    }

    public BodyButton(JsonObject json) {
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
    public BodyElementType getType() {
        return BodyElementType.BUTTON;
    }

    @Override
    public String getHTML() {
        String text = this.text.replace("\"","''").replace("<","‹").replace(">","›");
        if(icon!=null&&!icon.isEmpty()) {
            text += "<i class='"+icon.replace("<","").replace(">","")+"'></i>";
        }
        return "<h3 onclick='connector(`event.shared.action."+uuid+"`);'><i class='"+icon+"'></i> "+text+"</h3>";
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

    public ElementActionEvent[] getActionEvents() {
        return actionEvents.toArray(new ElementActionEvent[0]);
    }

    public void addActionEvent(ElementActionEvent event) {
        event.bindToElementId(uuid);
        actionEvents.add(event);
        SharedAPI.registerEvent(event);
    }

    public void click() {
        actionEvents.forEach(ElementActionEvent::execute);
    }
}