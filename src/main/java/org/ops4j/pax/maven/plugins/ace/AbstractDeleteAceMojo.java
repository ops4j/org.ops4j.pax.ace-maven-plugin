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

import java.util.List;

/**
 * Abstract delete ACE entry mojo
 *
 * @author dpishchukhin
 */
public abstract class AbstractDeleteAceMojo extends AbstractAceMojo {
    /**
     * Filter for entries to remove
     */
    @Parameter(property = "ace.delete.filter", required = true)
    protected String filter;

    // @Parameter(property = "ace.delete.links", defaultValue = "true", required = false)
    protected boolean deleteLinks = true; // always delete links

    @Override
    public void internalExecute() throws MojoExecutionException, MojoFailureException {
        String entryName = getEntryName();
        try {
            AceRestClient aceRestClient = createClient();
            aceRestClient.open();
            try {
                List<String> deletedIds = deleteEntries(aceRestClient);
                aceRestClient.persist();
                getLog().info(String.format("Deleted %s: %s", entryName, deletedIds));
            } finally {
                aceRestClient.close();
            }
        } catch (Exception e) {
            throw new MojoExecutionException(String.format("Unable to delete %s from ACE Server", entryName), e);
        }
    }

    protected abstract String getEntryName();

    protected abstract List<String> deleteEntries(AceRestClient client) throws Exception;
}
