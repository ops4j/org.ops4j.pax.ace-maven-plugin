/*
 * Copyright (c) 2013 Dmytro Pishchukhin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ops4j.pax.maven.plugins.ace.rest.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dpishchukhin
 */
public class AuditLog {
    private AceTarget target;

    private final List<AuditEvent> events = new ArrayList<AuditEvent>();

    public AuditLog() {
    }

    public AuditLog(AceTarget target) {
        this.target = target;
    }

    @XmlElement(name = "target")
    public AceTarget getTarget() {
        return target;
    }

    @XmlElementWrapper(name = "events")
    @XmlElement(name = "event")
    public List<AuditEvent> getEvents() {
        return events;
    }

    public void setEvents(List<AuditEvent> events) {
        this.events.clear();
        this.events.addAll(events);
    }

}
