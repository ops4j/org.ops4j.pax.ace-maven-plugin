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
import org.ops4j.pax.maven.plugins.ace.rest.model.AceTarget;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Create Target in ACE server
 *
 * @author dpishchukhin
 */
@Mojo(name = "create-target", requiresProject = false, aggregator = true)
public class CreateTargetMojo extends AbstractCreateAceMojo {
    /**
     * Link created Target with distributions by filter. If overwrite property id TRUE - links to distributions have been replaced
     */
    @Parameter(property = "ace.create-target.distributions.link-filter", required = false)
    protected String distributionsLinkFilter;

    @Override
    protected String getEntryName() {
        return "Target";
    }

    @Override
    protected String createEntry(AceRestClient client) throws Exception {
        AceTarget target = new AceTarget(attributes, tags);
        if (overwrite) {
            List<AceTarget> oldTargets = client.loadTargets(target.getIdFilter());
            if (oldTargets != null) {
                Set<String> deletedIds = new HashSet<String>();
                for (AceTarget oldTarget : oldTargets) {
                    deletedIds.addAll(client.deleteTargets(oldTarget.getIdFilter(), new Action<AceTarget>() {
                        @Override
                        public void execute(AceRestClient client, AceTarget entry) throws Exception {
                            if (distributionsLinkFilter != null) {
                                client.deleteDistribution2TargetLinks(client.loadDistributions(null),
                                        Collections.singletonList(entry));
                            }
                        }
                    }));
                }
                getLog().info(String.format("Old targets deleted: %s", deletedIds));
            }
        }
        String targetId = client.createTarget(target);
        client.createDistribution2TargetLinks(distributionsLinkFilter, target.getIdFilter());
        return targetId;
    }
}
