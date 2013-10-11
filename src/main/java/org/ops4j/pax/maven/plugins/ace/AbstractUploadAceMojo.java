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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.ops4j.pax.maven.plugins.ace.rest.AceRestClient;
import org.ops4j.pax.maven.plugins.ace.rest.Action;
import org.ops4j.pax.maven.plugins.ace.rest.model.AceArtifact;
import org.ops4j.pax.maven.plugins.ace.rest.utils.artifact.ArtifactHelper;

import java.io.File;
import java.util.*;

/**
 * Abstract upload ACE artifact mojo
 *
 * @author dpishchukhin
 */
public abstract class AbstractUploadAceMojo extends AbstractAceMojo {
    /**
     * Artifact attributes
     */
    @Parameter(property = "ace.create.attributes", required = true)
    protected Map<String, String> attributes;
    /**
     * Artifact tags
     */
    @Parameter(property = "ace.create.tags", required = false)
    protected Map<String, String> tags;

    /**
     * Overwrite old entry is exists.
     */
    @Parameter(property = "ace.create.overwrite", defaultValue = "false", required = false)
    protected boolean overwrite;

    @Override
    public void internalExecute() throws MojoExecutionException, MojoFailureException {
        try {
            List<File> files = getArtifactLocalFiles();

            AceRestClient aceRestClient = createClient();
            aceRestClient.open();
            try {
                List<String> addedIds = new ArrayList<String>();
                for (File file : files) {
                    AceArtifact artifact = new AceArtifact(attributes, tags);
                    Map<String, String> artifactAttributes = new ArtifactHelper().collectArtifactMetadataAttributes(file);
                    for (String key : artifactAttributes.keySet()) {
                        artifact.getAttributes().put(key, artifactAttributes.get(key));
                    }
                    if (overwrite) {
                        List<AceArtifact> oldArtifacts = aceRestClient.loadArtifacts(artifact.getIdFilter());
                        if (oldArtifacts != null) {
                            Set<String> deletedIds = new HashSet<String>();
                            for (AceArtifact oldArtifact : oldArtifacts) {
                                deletedIds.addAll(aceRestClient.deleteArtifacts(oldArtifact.getIdFilter(), new Action<AceArtifact>() {
                                    @Override
                                    public void execute(AceRestClient client, AceArtifact entry) throws Exception {
                                        client.purgeArtifact(entry.getAttributes().get(AceArtifact.URL_ATTR));
                                    }
                                }));
                            }
                            getLog().info(String.format("Old artifacts deleted: %s", deletedIds));
                        }
                    }

                    String obrUri = aceRestClient.uploadArtifact(file);
                    artifact.getAttributes().put(AceArtifact.URL_ATTR, obrUri);

                    addedIds.add(aceRestClient.createArtifact(artifact, file));
                }
                aceRestClient.persist();
                getLog().info(String.format("Artifacts uploaded: %s", addedIds));
            } finally {
                aceRestClient.close();
            }
        } catch (Exception e) {
            throw new MojoExecutionException(String.format("Unable to upload artifact(s) in ACE Server"), e);
        }
    }

    protected abstract List<File> getArtifactLocalFiles() throws Exception;
}
