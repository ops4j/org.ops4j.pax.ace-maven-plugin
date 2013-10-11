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
import org.ops4j.pax.maven.plugins.ace.rest.model.AceArtifact;

import java.util.Collections;
import java.util.List;

/**
 * Delete artifacts from ACE server
 *
 * @author dpishchukhin
 */
@Mojo(name = "delete-artifacts", requiresProject = false, aggregator = true)
public class DeleteArtifactsMojo extends AbstractDeleteAceMojo {
    /**
     * Delete artifacts from OBR as well
     */
    @Parameter(property = "ace.delete.artifacts.purge", defaultValue = "false", required = false)
    private boolean purge;

    @Override
    protected String getEntryName() {
        return "Artifacts";
    }

    @Override
    protected List<String> deleteEntries(AceRestClient client) throws Exception {
        return client.deleteArtifacts(filter, new Action<AceArtifact>() {
            @Override
            public void execute(AceRestClient client, AceArtifact entry) throws Exception {
                if (deleteLinks) {
                    client.deleteArtifactDFeatureLinks(Collections.singletonList(entry),
                            client.loadFeatures(null));
                }
                if (purge) {
                    client.purgeArtifact(entry.getAttributes().get(AceArtifact.URL_ATTR));
                }
            }
        });
    }
}
