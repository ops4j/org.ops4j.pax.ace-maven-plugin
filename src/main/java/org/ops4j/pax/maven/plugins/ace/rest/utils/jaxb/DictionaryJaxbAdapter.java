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

package org.ops4j.pax.maven.plugins.ace.rest.utils.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author dpishchukhin
 */
public abstract class DictionaryJaxbAdapter<T extends KeyValueList> extends XmlAdapter<T, Dictionary<String, String>> {
    @Override
    public Dictionary<String, String> unmarshal(T v) throws Exception {
        Dictionary<String, String> result = new Hashtable<String, String>();
        if (v != null) {
            for (KeyValue keyValue : v.getValues()) {
                result.put(keyValue.getKey(), keyValue.getValue());
            }
        }
        return result;
    }

    @Override
    public T marshal(Dictionary<String, String> v) throws Exception {
        T result = createInstance();
        if (v != null) {
            Enumeration<String> keys = v.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                result.add(new KeyValue(key, v.get(key)));
            }
        }
        return result;
    }

    protected abstract T createInstance();
}
