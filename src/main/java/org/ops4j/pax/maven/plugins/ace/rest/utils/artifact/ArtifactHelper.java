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

package org.ops4j.pax.maven.plugins.ace.rest.utils.artifact;

import org.ops4j.pax.maven.plugins.ace.rest.utils.artifact.bundle.BundleRecognizer;
import org.ops4j.pax.maven.plugins.ace.rest.utils.artifact.config.ConfigRecognizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dpishchukhin
 */
public class ArtifactHelper {
    private static List<ArtifactRecognizer> PROCESSORS = new ArrayList<ArtifactRecognizer>();

    static {
        PROCESSORS.add(new BundleRecognizer());
        PROCESSORS.add(new ConfigRecognizer());
    }

    public Map<String, String> collectArtifactMetadataAttributes(File file) throws IOException {
        Map<String, String> result = new HashMap<String, String>();

        ArtifactRecognizer processor = getArtifactProcessor(file);
        result.putAll(processor.collectArtifactMetadataAttributes(file));

        return result;
    }

    private ArtifactRecognizer getArtifactProcessor(File file) throws IllegalArgumentException {
        for (ArtifactRecognizer processor : PROCESSORS) {
            if (processor.canHandle(file)) {
                return processor;
            }
        }
        throw new IllegalArgumentException(String.format("Unsupported artifact type: %s", file.getAbsolutePath()));
    }
}
