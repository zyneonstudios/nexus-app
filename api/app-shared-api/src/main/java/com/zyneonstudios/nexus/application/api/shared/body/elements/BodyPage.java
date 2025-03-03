package com.zyneonstudios.nexus.application.api.shared.body.elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zyneonstudios.nexus.application.api.shared.body.ApplicationBody;
import com.zyneonstudios.nexus.desktop.NexusDesktop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class BodyPage implements BodyElement {

    private final JsonObject json;
    private final UUID uuid = UUID.randomUUID();
    private String title;
    private String id;
    private boolean active;
    private ArrayList<BodyElement> elements = new ArrayList<>();

    public BodyPage() {
        this.json = new JsonObject();
        validateStructure();
    }

    public BodyPage(JsonObject elements) {
        this.json = elements;
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

        if(!json.has("id")) {
            json.addProperty("id","");
            id = uuid.toString();
        } else {
            id = json.get("id").getAsString();
        }

        if(!json.has("title")) {
            json.addProperty("title","");
            title = "";
        } else {
            title = json.get("title").getAsString();
        }

        if(!json.has("active")) {
            json.addProperty("active",false);
            active = false;
        } else {
            active = json.get("active").getAsBoolean();
        }

        if(!json.has("elements")) {
            json.add("elements",new JsonArray());
            elements = new ArrayList<>();
        } else {
            ArrayList<BodyElement> elementArray = new ArrayList<>();
            if(!json.getAsJsonArray("elements").isEmpty()) {
                for (JsonElement element : json.getAsJsonArray("elements")) {
                    try {
                        elementArray.add(ApplicationBody.getElementFromJson(element.getAsJsonObject()));
                    } catch (Exception e) {
                        NexusDesktop.getLogger().err("[DISCOVER|API] Couldn't resolve Element for Page: " + e.getMessage());
                    }
                }
            }
            elements = elementArray;
        }
    }

    @Override
    public BodyElementType getType() {
        return BodyElementType.PAGE;
    }

    @Override
    public String getHTML() {
        String elements = "";
        if(!this.elements.isEmpty()) {
            elements = "%";
            StringBuilder e = new StringBuilder();
            for(BodyElement element:this.elements) {
                e.append(element.getHTML());
            }
            elements = elements.replace("%",e.toString());
        }
        String active = "";
        if(this.active) {
            active = " active";
        }
        return "<div id='discover-"+id+"' class='discover-tab"+active+"'><div class='discover-tab-content'>"+elements+"</div></div>";
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

    public String getTitle() {
        return title;
    }

    public String getID() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public BodyElement[] getElements() {
        return elements.toArray(new BodyElement[0]);
    }

    public void setId(String id) {
        this.json.remove("id");
        this.json.addProperty("id",id);
        this.id = id;
    }

    public void setTitle(String title) {
        this.json.remove("title");
        this.json.addProperty("title",title);
        this.title = title;
    }

    public void setActive(boolean active) {
        this.json.remove("active");
        this.json.addProperty("active",active);
        this.active = active;
    }

    public void addElement(BodyElement element) {
        elements.add(element);
        updateElements(elements);
    }

    public void removeElement(BodyElement element) {
        elements.remove(element);
        updateElements(elements);
    }

    private void updateElements(Collection<BodyElement> elements) {
        JsonArray elementArray = new JsonArray();
        for(BodyElement element:elements) {
            elementArray.add(element.getJsonObject());
        }
        this.json.remove("elements");
        this.json.add("elements",elementArray);
    }
}