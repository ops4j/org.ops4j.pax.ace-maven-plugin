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

package org.ops4j.pax.maven.plugins.ace.rest.utils;

import org.ops4j.pax.maven.plugins.ace.rest.model.AceEntry;
import org.ops4j.pax.maven.plugins.ace.rest.model.AceTargetState;
import org.ops4j.pax.maven.plugins.ace.rest.model.AuditEvent;

import javax.json.*;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dpishchukhin
 */
public class AceResponsesParser {
    private static final String JSON_DEFINITION_ATTR = "definition";
    static final String JSON_ATTRIBUTES_ATTR = "attributes";
    static final String JSON_TAGS_ATTR = "tags";
    private static final String JSON_STATE_ATTR = "state";

    public static List<String> parseIdsArray(String json) {
        JsonArray array = Json.createReader(new StringReader(json)).readArray();
        return parseIdsArray(array);
    }

    private static List<String> parseIdsArray(JsonArray array) {
        List<String> result = new ArrayList<String>();
        for (JsonValue value : array) {
            result.add(((JsonString) value).getString());
        }
        return result;
    }

    public static <T extends AceEntry> T parseEntry(String json, Class<T> entryClass) throws Exception {
        JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
        T entry = entryClass.getDeclaredConstructor(String.class).newInstance(jsonObject.getString(JSON_DEFINITION_ATTR));
        entry.setAttributes(parseMap(jsonObject.getJsonObject(JSON_ATTRIBUTES_ATTR)));
        entry.setTags(parseMap(jsonObject.getJsonObject(JSON_TAGS_ATTR)));

        try {
            Method setStateMethod = entryClass.getDeclaredMethod("setState", AceTargetState.class);
            if (setStateMethod != null) {
                setStateMethod.invoke(entry, parseTargetState(jsonObject.getJsonObject(JSON_STATE_ATTR)));
            }
        } catch (NoSuchMethodException e) {
            // ignore
        }
        return entry;
    }

    private static AceTargetState parseTargetState(JsonObject jsonObject) {
        AceTargetState state = new AceTargetState();
        state.setRegistrationState(jsonObject.getString("registrationState"));
        state.setProvisioningState(jsonObject.getString("provisioningState"));
        state.setStoreState(jsonObject.getString("storeState"));
        state.setCurrentVersion(jsonObject.getString("currentVersion"));
        state.setRegistered(Boolean.parseBoolean(jsonObject.getString("isRegistered")));
        state.setNeedsApproval(Boolean.parseBoolean(jsonObject.getString("needsApproval")));
        state.setAutoApprove(Boolean.parseBoolean(jsonObject.getString("autoApprove")));
        state.setLastInstallSuccess(jsonObject.getBoolean("lastInstallSuccess"));
        state.setArtifactsFromShop(parseIdsArray(jsonObject.getJsonArray("artifactsFromShop")));
        state.setArtifactsFromDeployment(parseIdsArray(jsonObject.getJsonArray("artifactsFromDeployment")));
        return state;
    }

    private static Map<String, String> parseMap(JsonObject mapsObject) {
        Map<String, String> result = new HashMap<String, String>();
        for (String key : mapsObject.keySet()) {
            result.put(key, mapsObject.getString(key));
        }
        return result;
    }

    public static List<AuditEvent> parseAuditEvents(String json) {
        JsonArray array = Json.createReader(new StringReader(json)).readArray();
        List<AuditEvent> result = new ArrayList<AuditEvent>();
        for (JsonValue value : array) {
            result.add(parseAuditEvent((JsonObject) value));
        }
        return result;
    }

    private static AuditEvent parseAuditEvent(JsonObject value) {
        AuditEvent auditEvent = new AuditEvent(
                value.getJsonNumber("logId").toString(),
                value.getJsonNumber("id").toString(),
                value.getString("time"),
                value.getString("type")
        );
        JsonObject properties = value.getJsonObject("properties");
        if (properties != null) {
            for (String key : properties.keySet()) {
                auditEvent.getProperties().put(key, properties.getString(key));
            }
        }
        return auditEvent;
    }
}
