/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.server.xdm.appusage.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.management.RuntimeMBeanException;
import javax.naming.NamingException;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;
import org.mobicents.xcap.client.XcapClient;
import org.mobicents.xcap.client.XcapResponse;
import org.mobicents.xcap.client.auth.Credentials;
import org.mobicents.xcap.client.impl.XcapClientImpl;
import org.mobicents.xcap.client.uri.DocumentSelectorBuilder;
import org.mobicents.xcap.client.uri.UriBuilder;
import org.openxdm.xcap.common.error.ConstraintFailureConflictException;
import org.openxdm.xcap.common.error.InternalServerErrorException;
import org.openxdm.xcap.common.error.NotUTF8ConflictException;
import org.openxdm.xcap.common.error.NotWellFormedConflictException;
import org.openxdm.xcap.common.xml.TextWriter;
import org.openxdm.xcap.common.xml.XMLValidator;
import org.openxdm.xcap.server.slee.appusage.rlsservices.RLSServicesAppUsage;

public class BadResourceListContentTest extends AbstractT {
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(BadResourceListContentTest.class);
	}
	
	private String user = "sip:joe@example.com";
	private String documentName = "index";
	
	@Test
	public void test() throws IOException, JAXBException, InterruptedException, TransformerException, NotWellFormedConflictException, NotUTF8ConflictException, InternalServerErrorException, URISyntaxException, InstanceNotFoundException, MBeanException, ReflectionException, MalformedObjectNameException, NullPointerException, NamingException {
		
		initRmiAdaptor();
		
		try {
			createUser(user,"password");
		}
		catch (RuntimeMBeanException e) {
			if (!(e.getCause() instanceof IllegalStateException)) {
				e.printStackTrace();
			}
		}
		
		XcapClient client = new XcapClientImpl();
		
		Credentials credentials = client.getCredentialsFactory().getHttpDigestCredentials(user, "password");
		
		// create exception for return codes
		ConstraintFailureConflictException exception = new ConstraintFailureConflictException("junit test");
		String expectedContent =
			"<?xml version='1.0' encoding='UTF-8'?><xcap-error xmlns='urn:ietf:params:xml:ns:xcap-error'><constraint-failure phrase='Bad URI in resource-list element >> http://badref.example.com' /></xcap-error>";

		// create uri		
		String documentSelector = DocumentSelectorBuilder.getUserDocumentSelectorBuilder(RLSServicesAppUsage.ID,user,documentName).toPercentEncodedString();
		UriBuilder uriBuilder = new UriBuilder()
			.setSchemeAndAuthority("http://localhost:8080")
			.setXcapRoot("/mobicents/")
			.setDocumentSelector(documentSelector);
		URI documentURI = uriBuilder.toURI();
				
		// read document xml
		InputStream is = this.getClass().getResourceAsStream("example_bad_resource-list_content.xml");
        String content = TextWriter.toString(XMLValidator.getWellFormedDocument(XMLValidator.getUTF8Reader(is)));
		is.close();
		
		// send put request and get response
		XcapResponse response = client.put(documentURI,RLSServicesAppUsage.MIMETYPE,content,null,credentials);
		
		// check put response
		System.out.println("Response got:\n"+response);
		assertTrue("Put response must exists",response != null);
		assertTrue("Put response content must be the expected and the response code should be "+exception.getResponseStatus(),response.getCode() == exception.getResponseStatus() && response.getEntity().getContentAsString().equals(expectedContent));
		
		client.shutdown();
		
		removeUser(user);
				
	}
		
}
