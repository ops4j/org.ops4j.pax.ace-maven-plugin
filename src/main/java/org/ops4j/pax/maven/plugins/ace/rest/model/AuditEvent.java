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

import org.ops4j.pax.maven.plugins.ace.rest.utils.jaxb.PropertiesJaxbAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author dpishchukhin
 */
@XmlType(propOrder = {"logId", "id", "time", "type", "properties"})
public class AuditEvent {
    private String logId;
    private String id;
    private String time;
    private String type;
    private final Hashtable<String, String> properties = new Hashtable<String, String>();

    public AuditEvent() {
    }

    public AuditEvent(String logId, String id, String time, String type) {
        this.logId = logId;
        this.id = id;
        this.time = time;
        this.type = type;
    }

    @XmlElement
    public String getLogId() {
        return logId;
    }

    @XmlElement
    public String getId() {
        return id;
    }

    @XmlElement
    public String getTime() {
        return time;
    }

    @XmlElement
    public String getType() {
        return type;
    }

    @XmlElement(name = "properties")
    @XmlJavaTypeAdapter(PropertiesJaxbAdapter.class)
    public Dictionary<String, String> getProperties() {
        return properties;
    }
}
