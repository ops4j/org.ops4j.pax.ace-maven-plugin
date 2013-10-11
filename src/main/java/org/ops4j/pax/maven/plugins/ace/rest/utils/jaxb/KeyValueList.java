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

import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dpishchukhin
 */
public abstract class KeyValueList {
    private List<KeyValue> values = new ArrayList<KeyValue>();

    @XmlTransient
    public List<KeyValue> getValues() {
        return values;
    }

    public void setValues(List<KeyValue> values) {
        this.values = values;
    }

    public void add(KeyValue keyValue) {
        values.add(keyValue);
    }
}
