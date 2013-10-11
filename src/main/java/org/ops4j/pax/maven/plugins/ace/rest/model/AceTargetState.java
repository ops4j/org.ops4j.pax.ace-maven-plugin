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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * @author dpishchukhin
 */
public class AceTargetState {
    private String registrationState;
    private String provisioningState;
    private String storeState;
    private String currentVersion;
    private boolean registered;
    private boolean needsApproval;
    private boolean autoApprove;
    private boolean lastInstallSuccess;
    private List<String> artifactsFromShop;
    private List<String> artifactsFromDeployment;

    public String getRegistrationState() {
        return registrationState;
    }

    public String getProvisioningState() {
        return provisioningState;
    }

    public String getStoreState() {
        return storeState;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public boolean isRegistered() {
        return registered;
    }

    public boolean isNeedsApproval() {
        return needsApproval;
    }

    public boolean isAutoApprove() {
        return autoApprove;
    }

    public boolean isLastInstallSuccess() {
        return lastInstallSuccess;
    }

    @XmlElementWrapper(name = "artifactsFromShop")
    @XmlElement(name = "id")
    public List<String> getArtifactsFromShop() {
        return artifactsFromShop;
    }

    @XmlElementWrapper(name = "artifactsFromDeployment")
    @XmlElement(name = "url")
    public List<String> getArtifactsFromDeployment() {
        return artifactsFromDeployment;
    }

    public void setRegistrationState(String registrationState) {
        this.registrationState = registrationState;
    }

    public void setProvisioningState(String provisioningState) {
        this.provisioningState = provisioningState;
    }

    public void setStoreState(String storeState) {
        this.storeState = storeState;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public void setNeedsApproval(boolean needsApproval) {
        this.needsApproval = needsApproval;
    }

    public void setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public void setLastInstallSuccess(boolean lastInstallSuccess) {
        this.lastInstallSuccess = lastInstallSuccess;
    }

    public void setArtifactsFromShop(List<String> artifactsFromShop) {
        this.artifactsFromShop = artifactsFromShop;
    }

    public void setArtifactsFromDeployment(List<String> artifactsFromDeployment) {
        this.artifactsFromDeployment = artifactsFromDeployment;
    }
}
