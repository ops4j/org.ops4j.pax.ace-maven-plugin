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
import org.ops4j.pax.maven.plugins.ace.rest.AceRestClient;

/**
 * Delete all entries (Artifacts, Features, Distributions and Targets) from ACE server and OBR
 *
 * @author dpishchukhin
 */
@Mojo(name = "delete-all", requiresProject = false, aggregator = true)
public class DeleteAllMojo extends AbstractAceMojo {
    @Override
    public void internalExecute() throws MojoExecutionException, MojoFailureException {
        try {
            AceRestClient aceRestClient = createClient();
            aceRestClient.open();
            try {
                aceRestClient.deleteAll();
                aceRestClient.persist();
                getLog().info(String.format("Records were deleted from ACE Server"));
            } finally {
                aceRestClient.close();
            }
        } catch (Exception e) {
            throw new MojoExecutionException(String.format("Unable to delete all from ACE Server"), e);
        }
    }
}
