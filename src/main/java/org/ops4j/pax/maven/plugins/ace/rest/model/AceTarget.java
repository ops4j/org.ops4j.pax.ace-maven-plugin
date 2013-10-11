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
public class AceTarget extends AceEntry {
    private static final String ID_ATTR = "id";

    private static final String[] MANDATORY_ATTRIBUTES = {
            ID_ATTR
    };

    private List<String> distributions = new ArrayList<String>();
    private AceTargetState state;

    public AceTarget() {
    }

    public AceTarget(String id) {
        super(id);
    }

    public AceTarget(Map<String, String> attributes, Map<String, String> tags) {
        super(attributes, tags);
    }

    @Override
    public String getIdAttributeKey() {
        return ID_ATTR;
    }

    @Override
    protected String[] getMandatoryAttributes() {
        return MANDATORY_ATTRIBUTES;
    }

    @XmlElementWrapper(name = "distributions")
    @XmlElement(name = "distribution")
    public List<String> getDistributions() {
        return this.distributions;
    }

    public void addDistribution(String distributionId) {
        this.distributions.add(distributionId);
    }

    @XmlElement(name = "state")
    public AceTargetState getState() {
        return state;
    }

    public void setState(AceTargetState state) {
        this.state = state;
    }
}
