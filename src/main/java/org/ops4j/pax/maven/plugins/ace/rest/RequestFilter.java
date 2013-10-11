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

package org.ops4j.pax.maven.plugins.ace.rest;

import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;

/**
 * @author dpishchukhin
 */
public class RequestFilter {
    private boolean targetsInclude;
    private Filter targetsFilter;
    private boolean distributionsInclude;
    private Filter distributionsFilter;
    private boolean featuresInclude;
    private Filter featuresFilter;
    private boolean artifactsInclude;
    private Filter artifactsFilter;

    public RequestFilter includeTargets(boolean targetsInclude) {
        this.targetsInclude = targetsInclude;
        return this;
    }

    public RequestFilter filterTargets(String targetsFilter) throws InvalidSyntaxException {
        if (targetsFilter != null) {
            this.targetsFilter = FrameworkUtil.createFilter(targetsFilter);
        }
        return this;
    }

    public RequestFilter includeDistributions(boolean distributionsInclude) {
        this.distributionsInclude = distributionsInclude;
        return this;
    }

    public RequestFilter filterDistributions(String distributionsFilter) throws InvalidSyntaxException {
        if (distributionsFilter != null) {
            this.distributionsFilter = FrameworkUtil.createFilter(distributionsFilter);
        }
        return this;
    }

    public RequestFilter includeFeatures(boolean featuresInclude) {
        this.featuresInclude = featuresInclude;
        return this;
    }

    public RequestFilter filterFeatures(String featuresFilter) throws InvalidSyntaxException {
        if (featuresFilter != null) {
            this.featuresFilter = FrameworkUtil.createFilter(featuresFilter);
        }
        return this;
    }

    public RequestFilter includeArtifacts(boolean artifactsInclude) {
        this.artifactsInclude = artifactsInclude;
        return this;
    }

    public RequestFilter filterArtifacts(String artifactsFilter) throws InvalidSyntaxException {
        if (artifactsFilter != null) {
            this.artifactsFilter = FrameworkUtil.createFilter(artifactsFilter);
        }
        return this;
    }

    public boolean isTargetsInclude() {
        return targetsInclude;
    }

    public Filter getTargetsFilter() {
        return targetsFilter;
    }

    public boolean isDistributionsInclude() {
        return distributionsInclude;
    }

    public Filter getDistributionsFilter() {
        return distributionsFilter;
    }

    public boolean isFeaturesInclude() {
        return featuresInclude;
    }

    public Filter getFeaturesFilter() {
        return featuresFilter;
    }

    public boolean isArtifactsInclude() {
        return artifactsInclude;
    }

    public Filter getArtifactsFilter() {
        return artifactsFilter;
    }
}
