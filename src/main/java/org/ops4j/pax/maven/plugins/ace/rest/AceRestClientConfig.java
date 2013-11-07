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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;

/**
 * @author dpishchukhin
 */
public class AceRestClientConfig {
    private URI serverUri;
    private String username;
    private String password;
    private String obrPath;
    private String clientPath;
    private KeyStore keyStore;
    private String privateKeyPassword;
    private KeyStore trustStore;
    private URL proxy;

    public URI getServerUri() {
        return serverUri;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getObrPath() {
        return obrPath;
    }

    public String getClientPath() {
        return clientPath;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public String getPrivateKeyPassword() {
        return privateKeyPassword;
    }

    public KeyStore getTrustStore() {
        return trustStore;
    }

    public URL getProxy() {
        return proxy;
    }

    public AceRestClientConfig setServerUri(URI serverUri) {
        this.serverUri = serverUri;
        return this;
    }

    public AceRestClientConfig setServerUrl(URL serverUrl) throws URISyntaxException {
        this.serverUri = serverUrl.toURI();
        return this;
    }

    public AceRestClientConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public AceRestClientConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public AceRestClientConfig setObrPath(String obrPath) {
        this.obrPath = obrPath;
        return this;
    }

    public AceRestClientConfig setClientPath(String clientPath) {
        this.clientPath = clientPath;
        return this;
    }

    public AceRestClientConfig setProxy(URL proxy) {
        this.proxy = proxy;
        return this;
    }

    public AceRestClientConfig setKeyStore(File keyStore, String password, String privateKeyPassword) {
        if (keyStore != null) {
            this.keyStore = loadKeyStore(keyStore, password);
        }
        this.privateKeyPassword = privateKeyPassword;
        return this;
    }

    public AceRestClientConfig setTrustStore(File trustStore, String password) {
        if (trustStore != null) {
            this.trustStore = loadKeyStore(trustStore, password);
        }
        return this;
    }

    private KeyStore loadKeyStore(File keyStoreFile, String password) {
        try {
            KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
            java.io.FileInputStream fis = null;
            try {
                fis = new java.io.FileInputStream(keyStoreFile);
                store.load(fis, password.toCharArray());
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
            return store;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to load KeyStore", e);
        }
    }
}
