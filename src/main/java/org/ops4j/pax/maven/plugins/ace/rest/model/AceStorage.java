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

import org.osgi.framework.Filter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dpishchukhin
 */
@XmlRootElement(name = "storage")
@XmlType(propOrder = {"artifacts", "features", "distributions", "targets"})
public class AceStorage {
    private List<AceTarget> targets = new ArrayList<AceTarget>();

    private List<AceDistribution> distributions = new ArrayList<AceDistribution>();

    private List<AceFeature> features = new ArrayList<AceFeature>();

    private List<AceArtifact> artifacts = new ArrayList<AceArtifact>();

    @XmlElementWrapper(name = "targets")
    @XmlElement(name = "target")
    public List<AceTarget> getTargets() {
        return targets;
    }

    @XmlElementWrapper(name = "distributions")
    @XmlElement(name = "distribution")
    public List<AceDistribution> getDistributions() {
        return distributions;
    }

    @XmlElementWrapper(name = "features")
    @XmlElement(name = "feature")
    public List<AceFeature> getFeatures() {
        return features;
    }

    @XmlElementWrapper(name = "artifacts")
    @XmlElement(name = "artifact")
    public List<AceArtifact> getArtifacts() {
        return artifacts;
    }

    public void addTargets(List<AceTarget> targets) {
        this.targets.addAll(targets);
    }

    public void addDistributions(List<AceDistribution> distributions) {
        this.distributions.addAll(distributions);
    }

    public void addFeatures(List<AceFeature> features) {
        this.features.addAll(features);
    }

    public void addArtifacts(List<AceArtifact> artifacts) {
        this.artifacts.addAll(artifacts);
    }

    public List<AceFeature> getFeatures(Filter filter) {
        return getEntryByAttribute(features, filter);
    }

    public List<AceDistribution> getDistributions(Filter filter) {
        return getEntryByAttribute(distributions, filter);
    }

    public List<AceTarget> getTargets(Filter filter) {
        return getEntryByAttribute(targets, filter);
    }

    public List<AceArtifact> getArtifacts(Filter filter) {
        return getEntryByAttribute(artifacts, filter);
    }

    private <T extends AceEntry> List<T> getEntryByAttribute(List<T> entries, Filter filter) {
        List<T> result = new ArrayList<T>();
        for (T entry : entries) {
            if (filter.match(entry.getAttributes())) {
                result.add(entry);
            }
        }
        return result;
    }
}
