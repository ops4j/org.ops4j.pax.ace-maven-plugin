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
import org.ops4j.pax.maven.plugins.ace.rest.model.AuditLogBook;
import org.ops4j.pax.maven.plugins.ace.rest.utils.jaxb.Serializer;

import javax.xml.bind.JAXBException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * List ACE Target audit event
 *
 * @author dpishchukhin
 */
@Mojo(name = "audit", requiresProject = false, aggregator = true)
public class AuditEventsTargetActionMojo extends AbstractTargetActionAceMojo {
    private static final String PRINT_TARGET_CONSOLE = "CONSOLE";
    private static final String PRINT_FORMAT_XML = "XML";
    private static final String PRINT_FORMAT_JSON = "JSON";

    /**
     * Start event ID
     */
    @Parameter(property = "ace.target.audit.start", defaultValue = "-1", required = false)
    private int start;
    /**
     * Maximal count of events
     */
    @Parameter(property = "ace.target.audit.max", defaultValue = "-1", required = false)
    private int max;

    /**
     * Available values: JSON, XML
     */
    @Parameter(property = "ace.target.audit.format", defaultValue = PRINT_FORMAT_JSON, required = false)
    private String printFormat;

    /**
     * Available values: CONSOLE or file name
     */
    @Parameter(property = "ace.target.audit.target", defaultValue = PRINT_TARGET_CONSOLE, required = false)
    private String printTarget;

    @Override
    protected void executeAction(AceRestClient client) throws Exception {
        AuditLogBook book = client.getAuditEventsForTargets(filter, start, max);
        printEvents(book);
    }

    @Override
    protected String getActionName() {
        return "auditEvents";
    }

    private void printEvents(AuditLogBook book) throws IOException, JAXBException {
        if (PRINT_TARGET_CONSOLE.equalsIgnoreCase(printTarget)) {
            StringWriter writer = new StringWriter();
            serializeEvents(book, writer);
            getLog().info(writer.getBuffer());
        } else {
            FileWriter writer = new FileWriter(printTarget);
            try {
                serializeEvents(book, writer);
            } finally {
                writer.close();
            }
            getLog().info("ACE Server Audit Logs are stored in: " + printTarget);
        }
    }

    private void serializeEvents(AuditLogBook book, Writer out) throws JAXBException, IOException {
        if (PRINT_FORMAT_XML.equalsIgnoreCase(printFormat)) {
            Serializer.serializeToXml(book, out);
        } else {
            Serializer.serializeToJson(book, out);
        }
    }

}
