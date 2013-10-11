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

package org.ops4j.pax.maven.plugins.ace.rest.model;

import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;

/**
 * @author dpishchukhin
 */
public class AceLink extends AceEntry {
    public static final String LEFT_ENDPOINT_ATTR = "leftEndpoint";
    public static final String RIGHT_ENDPOINT_ATTR = "rightEndpoint";

    private static final String[] MANDATORY_ATTRIBUTES = {
            LEFT_ENDPOINT_ATTR,
            RIGHT_ENDPOINT_ATTR
    };

    public AceLink() {
    }

    public AceLink(String id) {
        super(id);
    }

    @Override
    public String getIdAttributeKey() {
        return null;
    }

    @Override
    protected String[] getMandatoryAttributes() {
        return MANDATORY_ATTRIBUTES;
    }

    public Filter getLeftEndpointFilter() throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(getAttributes().get(LEFT_ENDPOINT_ATTR));
    }

    public Filter getRightEndpointFilter() throws InvalidSyntaxException {
        return FrameworkUtil.createFilter(getAttributes().get(RIGHT_ENDPOINT_ATTR));
    }

    public String getLeftEndpoint() {
        return getAttributes().get(LEFT_ENDPOINT_ATTR);
    }

    public String getRightEndpoint() {
        return getAttributes().get(RIGHT_ENDPOINT_ATTR);
    }

    public void setLeftEndpoint(String filter) {
        getAttributes().put(LEFT_ENDPOINT_ATTR, filter);
    }

    public void setRightEndpoint(String filter) {
        getAttributes().put(RIGHT_ENDPOINT_ATTR, filter);
    }
}
