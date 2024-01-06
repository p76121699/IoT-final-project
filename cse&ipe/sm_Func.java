package org.eclipse.om2m.ipe.semantic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.om2m.commons.constants.MimeMediaType;
import org.eclipse.om2m.commons.obix.Int;
import org.eclipse.om2m.commons.obix.Obj;
import org.eclipse.om2m.commons.obix.io.ObixEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.om2m.commons.resource.ContentInstance;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.AE;
import org.eclipse.om2m.commons.resource.Container;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;

public class sm_Func {

	private static Log LOGGER = LogFactory.getLog(sm_Func.class);
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	public static void createResources(String appId, String poa) {
		// TODO Auto-generated method stub
		// create the application resource

		AE ae = new AE();
		ae.setRequestReachability(true);
		ae.getPointOfAccess().add(poa);
		ae.setAppID(appId);
		ae.setName(appId);

		ResponsePrimitive response = RequestSender.createAE(ae);
		// Create Application sub-resources only if application not yet created
		if (response.getResponseStatusCode().equals(ResponseStatusCode.CREATED)) {
			Container cnt = new Container();
			cnt.getLabels().add("semantic");
			cnt.setMaxNrOfInstances(BigInteger.valueOf(0));
			cnt = new Container();
			cnt.setMaxNrOfInstances(BigInteger.valueOf(10));
			// Create STATE container sub-resource
			cnt.setName(sm_Constants.DATA);
			LOGGER.info(RequestSender.createContainer(response.getLocation(), cnt));
		}
	}
	public static Document XMLParser(String args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(args));
            Document document = builder.parse(inputSource);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static String extract_int(NodeList objElements, int num) {
		if (objElements.getLength() > 0) {
	        NodeList strElements = ((Element) objElements.item(0)).getElementsByTagName("int");

	        if (strElements.getLength() > 0 ) {
	            return strElements.item(num).getAttributes().getNamedItem("val").getTextContent();       
	        }
	    }
		return null;
	}
	
	public static String extract(NodeList objElements, int num) {
		if (objElements.getLength() > 0) {
	        NodeList strElements = ((Element) objElements.item(0)).getElementsByTagName("str");

	        if (strElements.getLength() > 0 ) {
	            return strElements.item(num).getAttributes().getNamedItem("val").getTextContent();       
	        }
	    }
		return null;
	}
	
	public static StringBuilder httpres(String url) {
	    try {
	        URL Url = new URL(url);
	        HttpURLConnection connection = (HttpURLConnection) Url.openConnection();
	        connection.setRequestMethod("GET");

	        connection.setRequestProperty("X-M2M-Origin", "admin:admin");
	        connection.setRequestProperty("Content-Type", "application/xml");
	        int responseCode = connection.getResponseCode();
	        System.out.println("Response Code: " + responseCode);

	        BufferedReader reader;
	        if (responseCode == 200) {
	            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        } else {
	            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
	        }

	        String line;
	        StringBuffer response = new StringBuffer();

	        while ((line = reader.readLine()) != null) {
	            response.append(line);
	        }
	        reader.close();
	        
	        System.out.println("Response Content: " + response.toString());

	        connection.disconnect();
	        return new StringBuilder(response.toString());
	    } catch (IOException e) {
	        e.printStackTrace();
	        return new StringBuilder(); 
	    }
	}
	
	public static void handleCI_verify(String con) {
		String url = "";
		String url2 = "";
		String password = "";
		Document doc = XMLParser(con);
	    NodeList objElements = doc.getElementsByTagName("obj");
	    url = extract(objElements, 0);
	    url2 = extract(objElements, 1);
	    password = extract(objElements, 2);
	    System.out.println("url1: " + url);
	    StringBuilder result = httpres(url);
	    
	    System.out.println("urlResponse: " + result.toString());
        Document condoc = XMLParser(result.toString().replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\""));
        NodeList objElements_2 = condoc.getElementsByTagName("obj");
        String check_password = extract(objElements_2, 0);
        System.out.println("check_Password: " + check_password);
        System.out.println("Password: " + password);
        if(check_password.equals(password)) {
        	StringBuilder result2 = httpres(url2);
        	String declaration = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        	System.out.println("result2: " + result2.toString().replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace(declaration, ""));
        	Document condoc2 = XMLParser(result2.toString().replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace(declaration, ""));
            NodeList objElements_3 = condoc2.getElementsByTagName("obj");
            String balance = extract_int(objElements_3, 0);
            System.out.println("balance: " + balance);
            try {
	            URL url_nodered = new URL("http://127.0.0.1:1880/get_balance");
	        	HttpURLConnection connection = (HttpURLConnection) url_nodered.openConnection();
	
	            connection.setRequestMethod("POST");
	            connection.setDoOutput(true);
	
	            String postData = "{\"balance\":" + balance + "}";
	            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
	            
	            connection.setRequestProperty("Content-Type", "application/json");
	            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
	
	            try (OutputStream os = connection.getOutputStream()) {
	                os.write(postDataBytes);
	            }
	
	            int responseCode = connection.getResponseCode();
	            System.out.println("Response Code: " + responseCode);
	
	            connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	public static void handleCI_create(String con) {
		String url = "";
		int balance = 0;
		con = con.replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"");
		String declaration = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        con.replace(declaration, "");
		Document doc = XMLParser(con);
	    System.out.println("con: " + con);
	    NodeList objElements = doc.getElementsByTagName("obj");
	    if (objElements.getLength() > 0) {
	        NodeList strElements = ((Element) objElements.item(0)).getElementsByTagName("str");
	        NodeList intElements = ((Element) objElements.item(0)).getElementsByTagName("int");

	        if (strElements.getLength() > 0 && intElements.getLength() > 0) {
	            url = strElements.item(0).getAttributes().getNamedItem("val").getTextContent();
	            balance = Integer.parseInt(intElements.item(0).getAttributes().getNamedItem("val").getTextContent());

	            System.out.println("sm_Router5_1_url: " + url);
	            System.out.println("sm_Router5_1_balance: " + balance);
	        }
	    }

	    ContentInstance cin = new ContentInstance();
	    Obj obj = new Obj();
		obj.add(new Int("balance", balance));
		cin.setContent(ObixEncoder.toString(obj));
		cin.setContentInfo("application/xml");
		cin.setContentInfo(MimeMediaType.OBIX + ":" + MimeMediaType.ENCOD_PLAIN);
	    RequestSender.createContentInstance(url, cin);
	}

	public static void handleCI_update(String con) {
		UpdateRequest updateRequest = UpdateFactory.create(con);
		UpdateAction.execute(updateRequest, sm_Constants.model);
		sm_Constants.model.write(System.out, "TURTLE");
	}
}