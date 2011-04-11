package org.mobicents.xdm.server.appusage.pidfmanipulation;

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
import org.mobicents.xcap.client.XcapConstant;
import org.mobicents.xcap.client.XcapResponse;
import org.mobicents.xcap.client.header.Header;
import org.mobicents.xcap.client.header.HeaderFactory;
import org.mobicents.xcap.client.impl.XcapClientImpl;
import org.mobicents.xcap.client.uri.DocumentSelectorBuilder;
import org.mobicents.xcap.client.uri.UriBuilder;
import org.openxdm.xcap.common.error.InternalServerErrorException;
import org.openxdm.xcap.common.error.NotUTF8ConflictException;
import org.openxdm.xcap.common.error.NotWellFormedConflictException;
import org.openxdm.xcap.common.xml.TextWriter;
import org.openxdm.xcap.common.xml.XMLValidator;

public class PutDocumentTest extends AbstractT {
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(PutDocumentTest.class);
	}
	
	private String user = "sip:someone@example.com";
	private String anotherUser = "sip:carol@example.com";
	private String documentName = "index";
	
	private Header[] getAssertedUserIdHeaders(HeaderFactory headerFactory, String assertedUserId) {
		Header[] headers = null;
		if (assertedUserId != null) {
			headers = new Header[1];
			headers[0] = headerFactory
					.getBasicHeader(
							XcapConstant.HEADER_X_3GPP_Asserted_Identity,
							assertedUserId);
		}
		return headers;
	}
	
	@Test
	public void test() throws IOException, JAXBException, InterruptedException, TransformerException, NotWellFormedConflictException, NotUTF8ConflictException, InternalServerErrorException, InstanceNotFoundException, MBeanException, ReflectionException, URISyntaxException, MalformedObjectNameException, NullPointerException, NamingException {
		
		initRmiAdaptor();

		try {
			createUser(user,user);
			createUser(anotherUser,anotherUser);
		}
		catch (RuntimeMBeanException e) {
			if (!(e.getCause() instanceof IllegalStateException)) {
				e.printStackTrace();
			}
		}
		
		XcapClient client = new XcapClientImpl();
				
		// create uri to put rls-services doc		
		String documentSelector = DocumentSelectorBuilder.getUserDocumentSelectorBuilder(PIDFManipulationAppUsage.ID,user,documentName).toPercentEncodedString();
		UriBuilder uriBuilder = new UriBuilder()
			.setSchemeAndAuthority("http://localhost:8080")
			.setXcapRoot("/mobicents")
			.setDocumentSelector(documentSelector);
		URI documentURI = uriBuilder.toURI();
		
		// read documents xml
		InputStream is = this.getClass().getResourceAsStream("good-document.xml");
        String content = TextWriter.toString(XMLValidator.getWellFormedDocument(XMLValidator.getUTF8Reader(is)));
		is.close();
		
		// send put request and get response
		XcapResponse putResponse = client.put(documentURI,PIDFManipulationAppUsage.MIMETYPE,content,getAssertedUserIdHeaders(client.getHeaderFactory(), user),null);
		System.out.println("Response got:\n"+putResponse);
		assertTrue("Put response must exists",putResponse != null);
		assertTrue("Put response code should be 200 or 201",putResponse.getCode() == 201 || putResponse.getCode() == 200);
		
		// ensure other user can't get doc
		XcapResponse getResponse = client.get(documentURI,getAssertedUserIdHeaders(client.getHeaderFactory(), anotherUser),null);
		assertTrue("Get response must exists",getResponse != null);
		assertTrue("Get response code should be 403",getResponse.getCode() == 403); 
		System.out.println("Response got:\n"+getResponse);
		
		// ensure another user can't put
		putResponse = client.put(documentURI,PIDFManipulationAppUsage.MIMETYPE,content,getAssertedUserIdHeaders(client.getHeaderFactory(), anotherUser),null);
		System.out.println("Response got:\n"+putResponse);
		assertTrue("Put response must exists",putResponse != null);
		assertTrue("Put response code should be 403",putResponse.getCode() == 403);
		
		// ensure another user can't delete
		XcapResponse deleteResponse = client.delete(documentURI,getAssertedUserIdHeaders(client.getHeaderFactory(), anotherUser),null);
		assertTrue("Unauthorized delete response must exists",deleteResponse != null);
		assertTrue("Unauthorized delete response code should be 403",deleteResponse.getCode() == 403); 
		System.out.println("Response got:\n"+deleteResponse);
		
		// delete doc
		deleteResponse = client.delete(documentURI,getAssertedUserIdHeaders(client.getHeaderFactory(), user),null);
		assertTrue("Authorized delete response must exists",deleteResponse != null);
		assertTrue("Authorized delete response code should be 200",deleteResponse.getCode() == 200); 
		System.out.println("Response got:\n"+deleteResponse);		
		
		client.shutdown();
		
		removeUser(user);
		removeUser(anotherUser);
		
	}
		
}
