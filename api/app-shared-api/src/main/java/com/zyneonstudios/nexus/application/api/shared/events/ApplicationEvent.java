package com.zyneonstudios.nexus.application.api.shared.events;

import com.zyneonstudios.nexus.utilities.events.Event;

public interface ApplicationEvent extends Event {

    EventType getType();
}