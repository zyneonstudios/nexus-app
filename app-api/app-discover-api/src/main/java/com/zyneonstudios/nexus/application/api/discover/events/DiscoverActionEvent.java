package com.zyneonstudios.nexus.application.api.discover.events;

import com.zyneonstudios.nexus.application.api.DiscoverAPI;
import com.zyneonstudios.nexus.application.api.discover.Discover;
import com.zyneonstudios.nexus.application.api.discover.body.elements.DiscoverElement;

import java.util.UUID;

public abstract class DiscoverActionEvent implements DiscoverEvent {

    private UUID uuid;

    public DiscoverActionEvent(UUID elementUUID) {
        this.uuid = elementUUID;
    }

    public DiscoverActionEvent(DiscoverElement element) {
        this.uuid = element.getUUID();
    }

    public DiscoverActionEvent() {
        uuid = UUID.randomUUID();
    }

    @Override
    public DiscoverEventType getType() {
        return DiscoverEventType.DISCOVER_ACTION_EVENT;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    public void bindToElement(DiscoverElement element) {
        bindToElementId(element.getUUID());
    }

    public void bindToElementId(UUID elementId) {
        this.uuid = elementId;
    }

    @Override
    public final boolean execute() {
        return onAction();
    }

    public abstract boolean onAction();

    public Discover getDiscover() {
        return DiscoverAPI.getDiscover();
    }
}