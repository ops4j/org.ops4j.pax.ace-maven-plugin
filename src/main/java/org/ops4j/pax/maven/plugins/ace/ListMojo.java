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
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.ops4j.pax.maven.plugins.ace.rest.AceRestClient;
import org.ops4j.pax.maven.plugins.ace.rest.RequestFilter;
import org.ops4j.pax.maven.plugins.ace.rest.model.AceStorage;
import org.ops4j.pax.maven.plugins.ace.rest.utils.jaxb.Serializer;

import javax.xml.bind.JAXBException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * List all data from ACE server
 *
 * @author dpishchukhin
 */
@Mojo(name = "list", requiresProject = false, aggregator = true)
public class ListMojo extends AbstractAceMojo {
    private static final String PRINT_TARGET_CONSOLE = "CONSOLE";
    private static final String PRINT_FORMAT_XML = "XML";
    private static final String PRINT_FORMAT_JSON = "JSON";

    /**
     * Include targets in result list
     */
    @Parameter(property = "ace.list.targets.include", defaultValue = "true", required = false)
    private boolean targetsInclude;
    /**
     * Targets filter
     */
    @Parameter(property = "ace.list.targets.filter", required = false)
    private String targetsFilter;

    /**
     * Include distributions in result list
     */
    @Parameter(property = "ace.list.distributions.include", defaultValue = "true", required = false)
    private boolean distributionsInclude;
    /**
     * Distributions filter
     */
    @Parameter(property = "ace.list.distributions.filter", required = false)
    private String distributionsFilter;

    /**
     * Include features in result list
     */
    @Parameter(property = "ace.list.features.include", defaultValue = "true", required = false)
    private boolean featuresInclude;
    /**
     * Features filter
     */
    @Parameter(property = "ace.list.features.filter", required = false)
    private String featuresFilter;

    /**
     * Include artifacts in result list
     */
    @Parameter(property = "ace.list.artifacts.include", defaultValue = "true", required = false)
    private boolean artifactsInclude;
    /**
     * Artifacts filter
     */
    @Parameter(property = "ace.list.artifacts.filter", required = false)
    private String artifactsFilter;

    /**
     * Available values: JSON, XML
     */
    @Parameter(property = "ace.list.print.format", defaultValue = PRINT_FORMAT_JSON, required = false)
    private String printFormat;

    /**
     * Available values: CONSOLE or file name
     */
    @Parameter(property = "ace.list.print.target", defaultValue = PRINT_TARGET_CONSOLE, required = false)
    private String printTarget;

    @Override
    public void internalExecute() throws MojoExecutionException, MojoFailureException {
        try {
            AceRestClient aceRestClient = createClient();
            aceRestClient.open();
            try {
                AceStorage storage = aceRestClient.loadStorage(
                        new RequestFilter()
                                .includeTargets(targetsInclude)
                                .filterTargets(targetsFilter)
                                .includeDistributions(distributionsInclude)
                                .filterDistributions(distributionsFilter)
                                .includeFeatures(featuresInclude)
                                .filterFeatures(featuresFilter)
                                .includeArtifacts(artifactsInclude)
                                .filterArtifacts(artifactsFilter)
                );
                printStorageDetails(storage);
            } finally {
                aceRestClient.close();
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Unable to get information for ACE Server", e);
        }
    }

    private void printStorageDetails(AceStorage storage) throws IOException, JAXBException {
        if (storage == null) {
            getLog().info("ACE Server Distribution Storage is empty");
        } else {
            if (PRINT_TARGET_CONSOLE.equalsIgnoreCase(printTarget)) {
                StringWriter writer = new StringWriter();
                serializeStorage(storage, writer);
                getLog().info(writer.getBuffer());
            } else {
                FileWriter writer = new FileWriter(printTarget);
                try {
                    serializeStorage(storage, writer);
                } finally {
                    writer.close();
                }
                getLog().info("ACE Server Distribution Storage is stored in: " + printTarget);
            }
        }
    }

    private void serializeStorage(AceStorage storage, Writer out) throws JAXBException, IOException {
        if (PRINT_FORMAT_XML.equalsIgnoreCase(printFormat)) {
            Serializer.serializeToXml(storage, out);
        } else {
            Serializer.serializeToJson(storage, out);
        }
    }
}
