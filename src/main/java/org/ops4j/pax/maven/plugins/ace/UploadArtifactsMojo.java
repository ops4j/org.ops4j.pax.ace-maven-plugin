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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Upload maven artifact
 *
 * @author dpishchukhin
 */
@Mojo(name = "upload-artifacts", requiresProject = false, aggregator = true)
public class UploadArtifactsMojo extends AbstractUploadAceMojo {
    /**
     * Artifacts
     */
    @Parameter(property = "artifacts", required = true)
    private UploadArtifact[] artifacts;


    @Component
    private ArtifactFactory artifactFactory;
    @Component
    private org.apache.maven.artifact.resolver.ArtifactResolver resolver;
    @Parameter(defaultValue = "${localRepository}", readonly = true)
    private org.apache.maven.artifact.repository.ArtifactRepository localRepository;
    @Parameter(defaultValue = "${project.remoteArtifactRepositories}", readonly = true)
    private java.util.List remoteRepositories;

    @Override
    protected List<File> getArtifactLocalFiles() throws ArtifactNotFoundException, ArtifactResolutionException {
        List<File> result = new ArrayList<File>();
        if (artifacts != null) {
            for (UploadArtifact uploadArtifact : artifacts) {
                Artifact artifact = artifactFactory.createArtifactWithClassifier(
                        uploadArtifact.getGroupId(),
                        uploadArtifact.getArtifactId(),
                        uploadArtifact.getVersion(),
                        uploadArtifact.getType(),
                        uploadArtifact.getClassifier());
                resolver.resolve(artifact, remoteRepositories, localRepository);
                result.add(artifact.getFile());
            }
        }
        return result;
    }
}
