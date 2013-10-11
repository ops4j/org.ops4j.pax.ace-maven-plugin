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

package org.ops4j.pax.maven.plugins.ace;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.ops4j.pax.maven.plugins.ace.rest.AceRestClient;
import org.ops4j.pax.maven.plugins.ace.rest.Action;
import org.ops4j.pax.maven.plugins.ace.rest.model.AceFeature;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Create ACE Feature
 *
 * @author dpishchukhin
 */
@Mojo(name = "create-feature", requiresProject = false, aggregator = true)
public class CreateFeatureMojo extends AbstractCreateAceMojo {
    /**
     * Link created Feature with artifacts by filter. If overwrite property id TRUE - links to artifacts have been replaced
     */
    @Parameter(property = "ace.create-feature.artifacts.link-filter", required = false)
    protected String artifactsLinkFilter;

    @Override
    protected String getEntryName() {
        return "Feature";
    }

    @Override
    protected String createEntry(AceRestClient client) throws Exception {
        AceFeature feature = new AceFeature(attributes, tags);
        if (overwrite) {
            List<AceFeature> oldFeatures = client.loadFeatures(feature.getIdFilter());
            if (oldFeatures != null) {
                Set<String> deletedIds = new HashSet<String>();
                for (AceFeature oldFeature : oldFeatures) {
                    deletedIds.addAll(client.deleteFeatures(oldFeature.getIdFilter(), new Action<AceFeature>() {
                        @Override
                        public void execute(AceRestClient client, AceFeature entry) throws Exception {
                            if (artifactsLinkFilter != null) {
                                client.deleteArtifactDFeatureLinks(client.loadArtifacts(null),
                                        Collections.singletonList(entry));
                            }
                        }
                    }));
                }
                getLog().info(String.format("Old features deleted: %s", deletedIds));
            }
        }
        String featureId = client.createFeature(feature);
        client.createArtifactDFeatureLinks(artifactsLinkFilter, feature.getIdFilter());
        return featureId;
    }
}
