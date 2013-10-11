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
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.osgi.framework.Constants.BUNDLE_SYMBOLICNAME;
import static org.osgi.framework.Constants.BUNDLE_VERSION;

/**
 * @author dpishchukhin
 */
public class AceArtifact extends AceEntry {
    public static final String URL_ATTR = "url";
    public static final String PROCESSOR_PID_ATTR = "processorPid";
    public static final String MIMETYPE_ATTR = "mimetype";

    public static final String ARTIFACT_NAME_ATTR = "artifactName";
    public static final String ARTIFACT_DESCRIPTION_ATTR = "artifactDescription";

    public static final String FILENAME_ATTR = "filename";

    public static final String MIMETYPE_OSGI_BUNDLE = "application/vnd.osgi.bundle";
    public static final String MIMETYPE_OSGI_CONFIG = "application/xml:osgi-autoconf";

    private static final String[] BUNDLE_MANDATORY_ATTRIBUTES = {
            ARTIFACT_NAME_ATTR,
            MIMETYPE_ATTR,
            URL_ATTR,
            BUNDLE_SYMBOLICNAME,
            BUNDLE_VERSION
    };

    private static final String[] CONFIG_MANDATORY_ATTRIBUTES = {
            ARTIFACT_NAME_ATTR,
            MIMETYPE_ATTR,
            URL_ATTR,
            FILENAME_ATTR,
            PROCESSOR_PID_ATTR
    };

    private List<String> features = new ArrayList<String>();

    public AceArtifact() {
    }

    public AceArtifact(String id) {
        super(id);
    }

    public AceArtifact(Map<String, String> attributes, Map<String, String> tags) {
        super(attributes, tags);
    }

    @XmlElementWrapper(name = "features")
    @XmlElement(name = "feature")
    public List<String> getFeatures() {
        return features;
    }

    public void addFeature(String featureId) {
        this.features.add(featureId);
    }

    @Override
    public String getIdAttributeKey() {
        if (isConfig()) {
            return FILENAME_ATTR;
        } else {
            return null;
        }
    }

    @Override
    public String getIdFilter() {
        if (isBundle()) {
            return String.format("(&(%s=%s)(%s=%s))",
                    BUNDLE_SYMBOLICNAME, getAttributes().get(BUNDLE_SYMBOLICNAME),
                    BUNDLE_VERSION, getAttributes().get(BUNDLE_VERSION)
            );
        } else if (isConfig()) {
            return super.getIdFilter();
        }
        return null; // todo
    }

    @Override
    protected String[] getMandatoryAttributes() {
        if (isBundle()) {
            return BUNDLE_MANDATORY_ATTRIBUTES;
        } else if (isConfig()) {
            return CONFIG_MANDATORY_ATTRIBUTES;
        } else {
            return null; // todo
        }
    }

    @XmlTransient
    public boolean isBundle() {
        return getAttributes().get(MIMETYPE_ATTR).equals(MIMETYPE_OSGI_BUNDLE);
    }

    @XmlTransient
    public boolean isConfig() {
        return getAttributes().get(MIMETYPE_ATTR).equals(MIMETYPE_OSGI_CONFIG);
    }
}
