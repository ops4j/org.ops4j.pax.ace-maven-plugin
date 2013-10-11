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

/**
 * Abstract target action mojo
 *
 * @author dpishchukhin
 */
public abstract class AbstractTargetActionAceMojo extends AbstractAceMojo {
    /**
     * Targets filter for action execution
     * e.g. (id=*)
     */
    @Parameter(property = "ace.target.filter", required = true)
    protected String filter;

    @Override
    public void internalExecute() throws MojoExecutionException, MojoFailureException {
        String actionName = getActionName();
        try {
            AceRestClient aceRestClient = createClient();
            aceRestClient.open();
            try {
                executeAction(aceRestClient);
                aceRestClient.persist();
                getLog().info(String.format("Action '%s' executed", actionName));
            } finally {
                aceRestClient.close();
            }
        } catch (Exception e) {
            throw new MojoExecutionException(String.format("Unable to execute action '%s' on ACE Server", actionName), e);
        }
    }

    protected abstract void executeAction(AceRestClient client) throws Exception;

    protected abstract String getActionName();
}
