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

import java.util.List;

/**
 * Create link(s) between ACE entries
 *
 * @author dpishchukhin
 */
@Mojo(name = "link", requiresProject = false, aggregator = true)
public class LinkMojo extends AbstractAceMojo {
    private static final String ARTIFACT_2_FEATURE_TYPE = "A2F";
    private static final String FEATURE_2_DISTRIBUTION_TYPE = "F2D";
    private static final String DISTRIBUTION_2_TARGET_TYPE = "D2T";

    /**
     * Link type <br/>
     * A2F - between artifacts and features<br/>
     * F2D - between features and distributions<br/>
     * D2T - between distributions and targets<br/>
     */
    @Parameter(property = "ace.link.type", required = true)
    protected String type;
    /**
     * Left endpoint filter
     */
    @Parameter(property = "ace.link.left-endpoint.filter", required = true)
    protected String leftFilter;
    /**
     * Right endpoint filter
     */
    @Parameter(property = "ace.link.right-endpoint.filter", required = true)
    protected String rightFilter;

    @Override
    public void internalExecute() throws MojoExecutionException, MojoFailureException {
        if (!ARTIFACT_2_FEATURE_TYPE.equalsIgnoreCase(type)
                && !FEATURE_2_DISTRIBUTION_TYPE.equalsIgnoreCase(type)
                && !DISTRIBUTION_2_TARGET_TYPE.equalsIgnoreCase(type)) {
            throw new MojoExecutionException(String.format("Unknown link type.\nSupported types: " +
                    ARTIFACT_2_FEATURE_TYPE + " - artifacts and features,\n" +
                    FEATURE_2_DISTRIBUTION_TYPE + " - features and distributions,\n" +
                    DISTRIBUTION_2_TARGET_TYPE + " - distributions and targets"));
        }

        try {
            AceRestClient aceRestClient = createClient();
            aceRestClient.open();
            try {
                List<String> createIds = null;
                if (ARTIFACT_2_FEATURE_TYPE.equalsIgnoreCase(type)) {
                    createIds = aceRestClient.createArtifactDFeatureLinks(leftFilter, rightFilter);
                } else if (FEATURE_2_DISTRIBUTION_TYPE.equalsIgnoreCase(type)) {
                    createIds = aceRestClient.createFeature2DistributionLinks(leftFilter, rightFilter);
                } else if (DISTRIBUTION_2_TARGET_TYPE.equalsIgnoreCase(type)) {
                    createIds = aceRestClient.createDistribution2TargetLinks(leftFilter, rightFilter);
                }
                aceRestClient.persist();
                getLog().info(String.format("Links are created: %s", createIds));
            } finally {
                aceRestClient.close();
            }
        } catch (Exception e) {
            throw new MojoExecutionException(String.format("Unable to create links in ACE Server"), e);
        }
    }
}
