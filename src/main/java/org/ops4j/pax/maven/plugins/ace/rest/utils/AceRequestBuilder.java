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

import javax.json.*;
import java.io.StringWriter;
import java.util.Dictionary;
import java.util.Enumeration;

/**
 * @author dpishchukhin
 */
public class AceRequestBuilder {
    public static <T extends AceEntry> String buildJson(T entry) {
        StringWriter out = new StringWriter();
        JsonWriter writer = Json.createWriter(out);
        JsonObject object = Json.createObjectBuilder()
                .add(AceResponsesParser.JSON_ATTRIBUTES_ATTR, buildJsonFromDictionary(entry.getAttributes()))
                .add(AceResponsesParser.JSON_TAGS_ATTR, buildJsonFromDictionary(entry.getTags()))
                .build();
        writer.writeObject(object);
        return out.toString();
    }

    private static JsonValue buildJsonFromDictionary(Dictionary<String, String> dic) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        if (dic != null) {
            Enumeration<String> keys = dic.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                objectBuilder.add(key, dic.get(key));
            }
        }
        return objectBuilder.build();
    }
}
