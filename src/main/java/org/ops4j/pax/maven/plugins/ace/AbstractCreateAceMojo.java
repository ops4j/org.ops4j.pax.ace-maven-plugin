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

import java.util.Map;

/**
 * Abstract Create ACE entry mojo
 *
 * @author dpishchukhin
 */
public abstract class AbstractCreateAceMojo extends AbstractAceMojo {
    /**
     * ACE entry attributes
     */
    @Parameter(property = "ace.create.attributes", required = true)
    protected Map<String, String> attributes;
    /**
     * ACE entry tags
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
        String entryName = getEntryName();

        try {
            AceRestClient aceRestClient = createClient();
            aceRestClient.open();
            try {
                String id = createEntry(aceRestClient);
                aceRestClient.persist();
                getLog().info(String.format("%s created: %s", entryName, id));
            } finally {
                aceRestClient.close();
            }
        } catch (Exception e) {
            throw new MojoExecutionException(String.format("Unable to create %s in ACE Server", entryName), e);
        }
    }

    protected abstract String getEntryName();

    protected abstract String createEntry(AceRestClient client) throws Exception;
}
