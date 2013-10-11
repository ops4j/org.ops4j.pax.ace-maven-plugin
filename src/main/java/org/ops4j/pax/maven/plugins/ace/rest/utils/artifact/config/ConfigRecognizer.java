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

package org.ops4j.pax.maven.plugins.ace.rest.utils.artifact.config;

import org.ops4j.pax.maven.plugins.ace.rest.model.AceArtifact;
import org.ops4j.pax.maven.plugins.ace.rest.utils.artifact.ArtifactRecognizer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.ops4j.pax.maven.plugins.ace.rest.model.AceArtifact.ARTIFACT_NAME_ATTR;

/**
 * @author dpishchukhin
 */
public class ConfigRecognizer implements ArtifactRecognizer {
    private static final String XML_FILE_EXTENSION = "xml";
    private static final String CONFIG_PROCESSOR_PID = "org.osgi.deployment.rp.autoconf";

    private static final String NAMESPACE_1_0 = "http://www.osgi.org/xmlns/metatype/v1.0.0";
    private static final String NAMESPACE_1_1 = "http://www.osgi.org/xmlns/metatype/v1.1.0";
    private static final String NAMESPACE_1_2 = "http://www.osgi.org/xmlns/metatype/v1.2.0";

    private SAXParserFactory saxParserFactory;

    public ConfigRecognizer() {
        saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(false);
        saxParserFactory.setValidating(false);
    }

    @Override
    public Map<String, String> collectArtifactMetadataAttributes(File file) throws IOException {
        Map<String, String> result = new HashMap<String, String>();

        result.put(AceArtifact.MIMETYPE_ATTR, AceArtifact.MIMETYPE_OSGI_CONFIG);
        result.put(AceArtifact.PROCESSOR_PID_ATTR, CONFIG_PROCESSOR_PID);

        result.put(ARTIFACT_NAME_ATTR, file.getName());
        result.put(AceArtifact.FILENAME_ATTR, file.getName());

        return result;
    }

    @Override
    public boolean canHandle(File file) {
        return XML_FILE_EXTENSION.equalsIgnoreCase(getFileExtension(file.getName()))
                && isConfigArtifact(file);
    }

    public boolean isConfigArtifact(File file) {
        MetaDataNamespaceCollector handler = new MetaDataNamespaceCollector();
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            SAXParser parser = saxParserFactory.newSAXParser();
            parser.parse(input, handler);
        } catch (Exception e) {
            String namespace = handler.getMetaDataNamespace();
            if (namespace != null
                    && (namespace.equals(NAMESPACE_1_0)
                    || namespace.equals(NAMESPACE_1_1)
                    || namespace.equals(NAMESPACE_1_2))) {
                return true;
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    private String getFileExtension(String name) {
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex != -1) {
            return name.substring(dotIndex + 1);
        }
        return "";
    }

    static class MetaDataNamespaceCollector extends DefaultHandler {
        private String m_metaDataNameSpace = "";

        public String getMetaDataNamespace() {
            return m_metaDataNameSpace;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            if (qName.equals("MetaData") || qName.endsWith(":MetaData")) {
                String nsAttributeQName = "xmlns";
                if (qName.endsWith(":MetaData")) {
                    nsAttributeQName = "xmlns" + ":" + qName.split(":")[0];
                }
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getQName(i).equals(nsAttributeQName)) {
                        m_metaDataNameSpace = attributes.getValue(i);
                    }
                }
            }
            throw new SAXException("Done");
        }
    }
}
