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
import org.ops4j.pax.maven.plugins.ace.rest.AceRestClient;

/**
 * Execute 'approve' action for ACE Targets
 *
 * @author dpishchukhin
 */
@Mojo(name = "approve", requiresProject = false, aggregator = true)
public class ApproveTargetActionMojo extends AbstractTargetActionAceMojo {
    @Override
    protected void executeAction(AceRestClient client) throws Exception {
        client.approveTargets(filter);
    }

    @Override
    protected String getActionName() {
        return "approve";
    }
}
