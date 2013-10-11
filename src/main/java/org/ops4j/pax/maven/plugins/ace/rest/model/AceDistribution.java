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
import java.util.Map;

/**
 * @author dpishchukhin
 */
public class AceDistribution extends AceEntry {
    private static final String NAME_ATTR = "name";

    private static final String[] MANDATORY_ATTRIBUTES = {
            NAME_ATTR
    };

    private List<String> features = new ArrayList<String>();
    private List<String> targets = new ArrayList<String>();

    public AceDistribution() {
    }

    public AceDistribution(String id) {
        super(id);
    }

    public AceDistribution(Map<String, String> attributes, Map<String, String> tags) {
        super(attributes, tags);
    }

    @Override
    public String getIdAttributeKey() {
        return NAME_ATTR;
    }

    @Override
    protected String[] getMandatoryAttributes() {
        return MANDATORY_ATTRIBUTES;
    }

    @XmlElementWrapper(name = "features")
    @XmlElement(name = "feature")
    public List<String> getFeatures() {
        return this.features;
    }

    public void addFeature(String featureId) {
        this.features.add(featureId);
    }

    @XmlElementWrapper(name = "targets")
    @XmlElement(name = "target")
    public List<String> getTargets() {
        return this.targets;
    }

    public void addTarget(String targetId) {
        this.targets.add(targetId);
    }
}
