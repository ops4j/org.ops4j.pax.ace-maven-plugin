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
import org.ops4j.pax.maven.plugins.ace.rest.model.AceDistribution;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Create ACE Distribution
 *
 * @author dpishchukhin
 */
@Mojo(name = "create-distribution", requiresProject = false, aggregator = true)
public class CreateDistributionMojo extends AbstractCreateAceMojo {
    /**
     * Link created Distribution with features by filter. If overwrite property id TRUE - links to features have been replaced
     */
    @Parameter(property = "ace.create-distribution.features.link-filter", required = false)
    protected String featuresLinkFilter;

    @Override
    protected String getEntryName() {
        return "Distribution";
    }

    @Override
    protected String createEntry(AceRestClient client) throws Exception {
        AceDistribution distribution = new AceDistribution(attributes, tags);
        if (overwrite) {
            List<AceDistribution> oldDistributions = client.loadDistributions(distribution.getIdFilter());
            if (oldDistributions != null) {
                Set<String> deletedIds = new HashSet<String>();
                for (AceDistribution oldDistribution : oldDistributions) {
                    deletedIds.addAll(client.deleteDistributions(oldDistribution.getIdFilter(), new Action<AceDistribution>() {
                        @Override
                        public void execute(AceRestClient client, AceDistribution entry) throws Exception {
                            if (featuresLinkFilter != null) {
                                client.deleteFeature2DistributionLinks(client.loadFeatures(null),
                                        Collections.singletonList(entry));
                            }
                        }
                    }));
                }
                getLog().info(String.format("Old distributions deleted: %s", deletedIds));
            }
        }
        String distributionId = client.createDistribution(distribution);
        client.createFeature2DistributionLinks(featuresLinkFilter, distribution.getIdFilter());
        return distributionId;
    }
}
