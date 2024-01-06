package org.eclipse.om2m.ipe.semantic;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.om2m.commons.constants.ResponseStatusCode;
import org.eclipse.om2m.commons.resource.ContentInstance;
import org.eclipse.om2m.commons.resource.RequestPrimitive;
import org.eclipse.om2m.commons.resource.ResponsePrimitive;
import org.eclipse.om2m.interworking.service.InterworkingService;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class sm_Router implements InterworkingService {
	final Base64.Decoder decoder = Base64.getDecoder();
	final Base64.Encoder encoder = Base64.getEncoder();

	@Override
	public ResponsePrimitive doExecute(RequestPrimitive request) {
		// TODO Auto-generated method stub
		ResponsePrimitive response = new ResponsePrimitive(request);
		System.out.println("sm_Router1: " + request);
		DocumentBuilder builder;
		InputSource src;
		Document doc = null;
		String ty = null;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			src = new InputSource();
			src.setCharacterStream(new StringReader(request.getContent().toString()));

			doc = builder.parse(src);
			ty = doc.getElementsByTagName("ty").item(0).getTextContent();
			response.setResponseStatusCode(ResponseStatusCode.OK);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ty.equals("16")) {
			System.out.println("sm_Router2: " + ty);
			String csi = doc.getElementsByTagName("csi").item(0).getTextContent();
			System.out.println("sm_Router2_1: " + csi);
			RequestSender.createSubscribe("SEMANTIC_SUB_MN", csi);
		} else if (ty.equals("2")) {
			System.out.println("sm_Router3: " + ty);
			String lbl = doc.getElementsByTagName("lbl").item(0).getTextContent();
			System.out.println("sm_Router3_1: " + lbl);
			if (lbl.contains("semantic") == true) {
				System.out.println("semantic");
				String ri = doc.getElementsByTagName("ri").item(0).getTextContent();
				RequestSender.createSubscribe("SEMANTIC_SUB_AE", ri);
			}
		} else if (ty.equals("3")) {
			System.out.println("sm_Router4: " + ty);
			String ol = doc.getElementsByTagName("ol").item(0).getTextContent();
			System.out.println("sm_Router4_1: " + ol);
			if (ol.contains("sm_DATA") == true) {
				System.out.println("sm_DATA");
				String ri = doc.getElementsByTagName("ri").item(0).getTextContent();
				RequestSender.createSubscribe("SEMANTIC_SUB_smDATA", ri);
			}
		} else if (ty.equals("4")) {
			System.out.println("sm_Router5: " + ty);
			
			String con = doc.getElementsByTagName("con").item(0).getTextContent();
			String cnf = doc.getElementsByTagName("cnf").item(0).getTextContent();
			try {
				System.out.println("sm_Router5_1: " + new String(decoder.decode(con), "UTF-8"));
				switch (cnf) {
				case "verify":			    
					sm_Func.handleCI_verify(new String(decoder.decode(con), "UTF-8"));
					break;
				case "update":			    
					sm_Func.handleCI_update(new String(decoder.decode(con), "UTF-8"));
					break;
                case "create":
                    sm_Func.handleCI_create(new String(decoder.decode(con), "UTF-8"));
                    break;
				default:
					response.setContent("This cnf is not supported");
					break;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("sm_Router6: " + ty);
		}
		return response;
	}

	@Override
	public String getAPOCPath() {
		// TODO Auto-generated method stub
		return sm_Constants.POA;
	}
}