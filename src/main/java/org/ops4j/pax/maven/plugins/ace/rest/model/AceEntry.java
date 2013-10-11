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

import org.ops4j.pax.maven.plugins.ace.rest.utils.jaxb.AttributesJaxbAdapter;
import org.ops4j.pax.maven.plugins.ace.rest.utils.jaxb.TagsJaxbAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author dpishchukhin
 */
@XmlType(propOrder = {"id", "attributes", "tags"})
public abstract class AceEntry {
    private String id;
    private final Hashtable<String, String> attributes = new Hashtable<String, String>();
    private final Hashtable<String, String> tags = new Hashtable<String, String>();

    public AceEntry() {
    }

    public AceEntry(String id) {
        this.id = id;
    }

    protected AceEntry(Map<String, String> attributes, Map<String, String> tags) {
        setAttributes(attributes);
        setTags(tags);
    }

    @XmlElement
    public String getId() {
        return id;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes.clear();
        if (attributes != null) {
            this.attributes.putAll(attributes);
        }
    }

    @XmlElement(name = "attributes")
    @XmlJavaTypeAdapter(AttributesJaxbAdapter.class)
    public Dictionary<String, String> getAttributes() {
        return attributes;
    }

    public void setTags(Map<String, String> tags) {
        this.tags.clear();
        if (tags != null) {
            this.tags.putAll(tags);
        }
    }

    @XmlElement(name = "tags")
    @XmlJavaTypeAdapter(TagsJaxbAdapter.class)
    public Dictionary<String, String> getTags() {
        return tags;
    }

    @XmlTransient
    protected abstract String getIdAttributeKey();

    @XmlTransient
    public String getIdFilter() {
        return String.format("(&(%s=%s))", getIdAttributeKey(), getAttributes().get(getIdAttributeKey()));
    }

    public void validate() throws Exception {
        String[] mandatoryAttributes = getMandatoryAttributes();
        if (mandatoryAttributes != null) {
            for (String attribute : mandatoryAttributes) {
                if (getAttributes().get(attribute) == null) {
                    throw new Exception(String.format("Attribute '%s' is missed", attribute));
                }
            }
        }
    }

    @XmlTransient
    protected abstract String[] getMandatoryAttributes();
}
