package com.zyneonstudios.nexus.application.api.discover.events;

import com.zyneonstudios.nexus.utilities.events.Event;

public interface DiscoverEvent extends Event {

    DiscoverEventType getType();
}