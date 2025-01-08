package com.zyneonstudios.nexus.application.api.discover.body.elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zyneonstudios.nexus.application.api.discover.body.DiscoverBody;
import com.zyneonstudios.nexus.desktop.NexusDesktop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class DiscoverRow implements DiscoverElement {

    private final JsonObject json;
    private final UUID uuid = UUID.randomUUID();
    private ArrayList<DiscoverElement> elements = new ArrayList<>();

    public DiscoverRow() {
        this.json = new JsonObject();
        validateStructure();
    }

    public DiscoverRow(JsonObject json) {
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
                        NexusDesktop.getLogger().err("[DISCOVER|API] Couldn't resolve Element for Row: " + e.getMessage());
                    }
                }
            }
            elements = elementArray;
        }
    }

    @Override
    public DiscoverElementType getType() {
        return DiscoverElementType.ROW;
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
        return "<div class='flex'>"+elements+"</div>";
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

    public DiscoverElement[] getElements() {
        return elements.toArray(new DiscoverElement[0]);
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