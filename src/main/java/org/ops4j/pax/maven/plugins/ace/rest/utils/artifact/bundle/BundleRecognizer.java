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

package org.ops4j.pax.maven.plugins.ace.rest.utils.artifact.bundle;

import org.ops4j.pax.maven.plugins.ace.rest.utils.artifact.ArtifactRecognizer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static org.ops4j.pax.maven.plugins.ace.rest.model.AceArtifact.*;
import static org.osgi.framework.Constants.*;

/**
 * @author dpishchukhin
 */
public class BundleRecognizer implements ArtifactRecognizer {
    @Override
    public Map<String, String> collectArtifactMetadataAttributes(File file) throws IOException {
        Map<String, String> result = new HashMap<String, String>();

        JarFile jarFile = new JarFile(file);
        result.put(MIMETYPE_ATTR, MIMETYPE_OSGI_BUNDLE);
        result.put(PROCESSOR_PID_ATTR, "");
        Manifest manifest = jarFile.getManifest();
        Attributes manifestAttributes = manifest.getMainAttributes();
        result.put(BUNDLE_SYMBOLICNAME, manifestAttributes.getValue(BUNDLE_SYMBOLICNAME));
        result.put(BUNDLE_VERSION, manifestAttributes.getValue(BUNDLE_VERSION));

        // todo
        result.put(ARTIFACT_NAME_ATTR,
                String.format("%s-%s", result.get(BUNDLE_SYMBOLICNAME), result.get(BUNDLE_VERSION)));

        addAttribute(result, manifestAttributes, BUNDLE_CATEGORY);
        addAttribute(result, manifestAttributes, BUNDLE_COPYRIGHT);
        addAttribute(result, manifestAttributes, BUNDLE_DESCRIPTION);
        addAttribute(result, manifestAttributes, BUNDLE_NAME);
        addAttribute(result, manifestAttributes, BUNDLE_VENDOR);
        addAttribute(result, manifestAttributes, BUNDLE_DOCURL);
        addAttribute(result, manifestAttributes, BUNDLE_CONTACTADDRESS);

        // todo
        String artifactDescription = "";
        if (result.get(BUNDLE_DESCRIPTION) != null) {
            artifactDescription = result.get(BUNDLE_DESCRIPTION);
        }
        result.put(ARTIFACT_DESCRIPTION_ATTR, artifactDescription);

        return result;
    }

    @Override
    public boolean canHandle(File file) {
        try {
            JarFile jarFile = new JarFile(file);
            try {
                Attributes attributes = jarFile.getManifest().getMainAttributes();
                return attributes.getValue(BUNDLE_MANIFESTVERSION) != null
                        && attributes.getValue(BUNDLE_SYMBOLICNAME) != null;
            } finally {
                jarFile.close();
            }
        } catch (IOException e) {
            return false;
        }
    }

    private static void addAttribute(Map<String, String> attributes, Attributes manifestAttributes, String key) {
        String value = manifestAttributes.getValue(key);
        attributes.put(key, value == null ? "" : value);
    }
}
