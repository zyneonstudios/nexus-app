package com.zyneonstudios.nexus.application.api.modules.events;

import com.zyneonstudios.nexus.utilities.events.Event;

public interface ModuleEvent extends Event {

    ModuleEventType getType();
}
