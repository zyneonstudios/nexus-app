package com.zyneonstudios.nexus.application.api.discover.body.elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zyneonstudios.nexus.application.api.discover.body.DiscoverBody;
import com.zyneonstudios.nexus.desktop.NexusDesktop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class DiscoverPage implements DiscoverElement {

    private final JsonObject json;
    private final UUID uuid = UUID.randomUUID();
    private String title;
    private String id;
    private boolean active;
    private ArrayList<DiscoverElement> elements = new ArrayList<>();

    public DiscoverPage() {
        this.json = new JsonObject();
        validateStructure();
    }

    public DiscoverPage(JsonObject elements) {
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
            ArrayList<DiscoverElement> elementArray = new ArrayList<>();
            if(!json.getAsJsonArray("elements").isEmpty()) {
                for (JsonElement element : json.getAsJsonArray("elements")) {
                    try {
                        elementArray.add(DiscoverBody.getElementFromJson(element.getAsJsonObject()));
                    } catch (Exception e) {
                        NexusDesktop.getLogger().err("[DISCOVER|API] Couldn't resolve Element for Page: " + e.getMessage());
                    }
                }
            }
            elements = elementArray;
        }
    }

    @Override
    public DiscoverElementType getType() {
        return DiscoverElementType.PAGE;
    }

    @Override
    public String getHTML() {
        String elements = "";
        if(!this.elements.isEmpty()) {
            elements = "%";
            StringBuilder e = new StringBuilder();
            for(DiscoverElement element:this.elements) {
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

    public Collection<DiscoverElement> getElements() {
        return elements;
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

    public void addElement(DiscoverElement element) {
        elements.add(element);
        updateElements(elements);
    }

    public void removeElement(DiscoverElement element) {
        elements.remove(element);
        updateElements(elements);
    }

    private void updateElements(Collection<DiscoverElement> elements) {
        JsonArray elementArray = new JsonArray();
        for(DiscoverElement element:elements) {
            elementArray.add(element.getJsonObject());
        }
        this.json.remove("elements");
        this.json.add("elements",elementArray);
    }
}