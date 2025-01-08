package com.zyneonstudios.nexus.application.api.shared.body.elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zyneonstudios.nexus.desktop.NexusDesktop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class BodyTextCard implements BodyElement {

    private final JsonObject json;
    private final UUID uuid = UUID.randomUUID();
    private String title;
    private String text;
    private ArrayList<BodyButton> buttons;

    public BodyTextCard() {
        this.json = new JsonObject();
        validateStructure();
    }

    public BodyTextCard(JsonObject json) {
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

        if(!json.has("title")) {
            json.addProperty("title","");
            title = "";
        } else {
            title = json.get("title").getAsString();
        }

        if(!json.has("text")) {
            json.addProperty("text","");
            text = "";
        } else {
            text = json.get("text").getAsString();
        }

        if(!json.has("buttons")) {
            json.add("buttons",new JsonArray());
            buttons = new ArrayList<>();
        } else {
            ArrayList<BodyButton> buttonArray = new ArrayList<>();
            if(!json.getAsJsonArray("buttons").isEmpty()) {
                for (JsonElement button : json.getAsJsonArray("buttons")) {
                    try {
                        buttonArray.add(new BodyButton(button.getAsJsonObject()));
                    } catch (Exception e) {
                        NexusDesktop.getLogger().err("[DISCOVER|API] Couldn't resolve Button for TextCard: " + e.getMessage());
                    }
                }
            }
            buttons = buttonArray;
        }
    }

    @Override
    public BodyElementType getType() {
        return BodyElementType.TEXT_CARD;
    }

    @Override
    public String getHTML() {
        String buttons = "";
        if(!this.buttons.isEmpty()) {
            buttons = "<div class='buttons'>%</div>";
            StringBuilder b = new StringBuilder();
            for(BodyButton button:this.buttons) {
                b.append(button.getHTML());
            }
            buttons = buttons.replace("%",b.toString());
        }
        return "<div class='card text-card'><h4>"+title.replace("\"","''").replace("<","‹").replace(">","›")+"</h4><div class='line'></div><div class='flex'><p>"+text.replace("\"","''").replace("<","‹").replace(">","›")+"</p>"+buttons+"</div></div>";
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

    public BodyButton[] getButtons() {
        return buttons.toArray(new BodyButton[0]);
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setButtons(ArrayList<BodyButton> buttons) {
        this.buttons = buttons;
        updateButtons(buttons);
    }

    public void addButton(BodyButton button) {
        buttons.add(button);
        updateButtons(buttons);
    }

    public void removeButton(BodyButton button) {
        buttons.remove(button);
        updateButtons(buttons);
    }

    private void updateButtons(Collection<BodyButton> buttons) {
        JsonArray buttonArray = new JsonArray();
        for(BodyButton button:buttons) {
            buttonArray.add(button.getJsonObject());
        }
        this.json.remove("buttons");
        this.json.add("buttons",buttonArray);
    }

    public void setTitle(String title) {
        this.json.remove("title");
        this.json.addProperty("title",title);
        this.title = title;
    }

    public void setText(String text) {
        this.json.remove("text");
        this.json.addProperty("text",text);
        this.text = text;
    }
}