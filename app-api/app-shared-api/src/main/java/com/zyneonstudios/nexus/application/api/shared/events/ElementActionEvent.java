package com.zyneonstudios.nexus.application.api.shared.events;

import com.zyneonstudios.nexus.application.api.shared.body.elements.BodyElement;

import java.util.UUID;

public abstract class ElementActionEvent implements ApplicationEvent {

    private UUID uuid;

    public ElementActionEvent(UUID elementUUID) {
        this.uuid = elementUUID;
    }

    public ElementActionEvent(BodyElement element) {
        this.uuid = element.getUUID();
    }

    public ElementActionEvent() {
        uuid = UUID.randomUUID();
    }

    @Override
    public EventType getType() {
        return EventType.ELEMENT_ACTION_EVENT;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    public void bindToElement(BodyElement element) {
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
}