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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.ops4j.pax.maven.plugins.ace.rest.AceRestClient;
import org.ops4j.pax.maven.plugins.ace.rest.AceRestClientConfig;

import java.net.URISyntaxException;
import java.net.URL;

/**
 * Abstract ACE Mojo
 *
 * @author dpishchukhin
 */
public abstract class AbstractAceMojo extends AbstractMojo {
    /**
     * ACE server url (e.g http://localhost:9090)
     */
    @Parameter(property = "ace.server.url", required = true)
    private URL serverUrl;

    /**
     * ACE server user name for HTTP Basic authentication
     */
    @Parameter(property = "ace.server.username", required = false)
    private String username;

    /**
     * ACE server user password for HTTP Basic authentication
     */
    @Parameter(property = "ace.server.password", required = false)
    private String password;

    /**
     * ACE server REST service path (Default: client/work)
     */
    @Parameter(property = "ace.server.client.path", defaultValue = "client/work", required = false)
    private String clientPath;

    /**
     * ACE server OBR service path (Default: obr)
     */
    @Parameter(property = "ace.server.obr.path", defaultValue = "obr", required = false)
    private String obrPath;

    /**
     * Skip execution
     */
    @Parameter(property = "ace.skip", defaultValue = "false", required = false)
    private boolean skip;

    /**
     * Do not set build to FAILURE.
     * Could be used during deployment of all artifacts in multi-module project to ignore unsupported artifacts
     */
    @Parameter(property = "ace.silent", defaultValue = "false", required = false)
    private boolean silent;

    protected AceRestClient createClient() throws URISyntaxException {
        return new AceRestClient(new AceRestClientConfig()
                .setServerUrl(serverUrl)
                .setUsername(username)
                .setPassword(password)
                .setClientPath(clientPath)
                .setObrPath(obrPath));
    }

    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("Skipping ACE actions");
            return;
        }
        try {
            internalExecute();
        } catch (Exception e) {
            if (silent) {
                getLog().warn(String.format("Unable to upload artifact in ACE Server"));
                getLog().debug(e);
            } else {
                throw new MojoExecutionException(String.format("Unable to upload artifact in ACE Server"), e);
            }
        }
    }

    protected abstract void internalExecute() throws MojoExecutionException, MojoFailureException;
}
