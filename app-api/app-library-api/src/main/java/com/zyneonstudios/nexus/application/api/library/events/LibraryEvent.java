package com.zyneonstudios.nexus.application.api.library.events;

import com.zyneonstudios.nexus.utilities.events.Event;

public interface LibraryEvent extends Event {

    LibraryEventType getType();
}