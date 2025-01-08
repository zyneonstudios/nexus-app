package com.zyneonstudios.nexus.application.api.shared.body;

import com.google.gson.JsonObject;
import com.zyneonstudios.nexus.application.api.shared.body.elements.*;

import java.util.Map;
import java.util.function.Function;

public class ApplicationBody {

    private static final Map<String, Function<JsonObject, BodyElement>> ELEMENT_FACTORIES = Map.of(
            "ACTION_CARD", BodyActionCard::new,
            "BUTTON", BodyButton::new,
            "IMAGE", BodyImage::new,
            "ROW", BodyRow::new,
            "TEXT_CARD", BodyTextCard::new
    );

    public static BodyElement getElementFromJson(JsonObject json) {
        String type = json.get("type").getAsString();
        Function<JsonObject, BodyElement> factory = ELEMENT_FACTORIES.get(type);
        return (factory != null) ? factory.apply(json) : null;
    }
}