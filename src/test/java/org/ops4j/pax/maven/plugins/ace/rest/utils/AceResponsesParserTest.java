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

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.ops4j.pax.maven.plugins.ace.rest.utils.AceResponsesParser.parseIdsArray;

/**
 * @author dpishchukhin
 */
public class AceResponsesParserTest {
    @Test
    public void testParseIdArray() throws Exception {
        List<String> list1 = parseIdsArray("[]");
        assertThat(list1, notNullValue());
        assertThat(list1.size(), is(equalTo(0)));

        List<String> list2 = parseIdsArray("[\"test\"]");
        assertThat(list2, notNullValue());
        assertThat(list2.size(), is(equalTo(1)));
        assertThat(list2.get(0), is(equalTo("test")));

        List<String> list3 = parseIdsArray("[\"test\",\"test2\"]");
        assertThat(list3, notNullValue());
        assertThat(list3.size(), is(equalTo(2)));
        assertThat(list3.get(0), is(equalTo("test")));
        assertThat(list3.get(1), is(equalTo("test2")));
    }
}
