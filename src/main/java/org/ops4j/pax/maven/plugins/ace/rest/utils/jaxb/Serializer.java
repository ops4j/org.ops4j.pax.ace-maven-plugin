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

import org.codehaus.jettison.util.StringIndenter;
import org.glassfish.jersey.jettison.JettisonJaxbContext;
import org.glassfish.jersey.jettison.JettisonMarshaller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author dpishchukhin
 */
public class Serializer {
    public static void serializeToXml(Object object, Writer out) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, out);
    }

    public static void serializeToJson(Object object, Writer out) throws JAXBException, IOException {
        StringWriter writer = new StringWriter();
        JettisonJaxbContext context = new JettisonJaxbContext(object.getClass());
        JettisonMarshaller marshaller = context.createJsonMarshaller();
        marshaller.setProperty(JettisonMarshaller.FORMATTED, true);
        marshaller.marshallToJSON(object, writer);

        out.write(new StringIndenter(writer.toString()).result());
    }

}
