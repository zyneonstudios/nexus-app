package com.zyneonstudios.nexus.application.api.discover.body;

import com.google.gson.JsonObject;
import com.zyneonstudios.nexus.application.api.discover.body.elements.*;

import java.util.Map;
import java.util.function.Function;

public class DiscoverBody {

    private static final Map<String, Function<JsonObject, DiscoverElement>> ELEMENT_FACTORIES = Map.of(
            "ACTION_CARD", DiscoverActionCard::new,
            "BUTTON", DiscoverButton::new,
            "IMAGE", DiscoverImage::new,
            "ROW", DiscoverRow::new,
            "TEXT_CARD", DiscoverTextCard::new
    );

    public static DiscoverElement getElementFromJson(JsonObject json) {
        String type = json.get("type").getAsString();
        Function<JsonObject, DiscoverElement> factory = ELEMENT_FACTORIES.get(type);
        return (factory != null) ? factory.apply(json) : null;
    }
}