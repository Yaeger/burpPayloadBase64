package netspi.payloadEncoder;

import java.io.PrintWriter;
import java.util.List;

import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;
import netspi.payloadEncoder.ui.base64Tab;



public class base64 {
    public IBurpExtenderCallbacks callbacks; 
	public base64Tab base64Tab;
    public IExtensionHelpers helpers;
    public PrintWriter stdout, errout;
	
	public base64(IBurpExtenderCallbacks callbacks) {
		// TODO Auto-generated constructor stub
	     this.callbacks = callbacks; 
	     this.helpers = callbacks.getHelpers(); 
	}

	public byte[] decodeResponse(IHttpRequestResponse currentRequest) {
		// TODO Auto-generated method stub
		System.out.println("--RESPONSE--");
		String postBody = "";
		
		String postBodyInner = "";
		String postBodyInnerPre = "";
		String postBodyInnerPost = "";
		
		String postBodyInnerInner = "";	
		String postBodyInnerInnerPre = "";	
		String postBodyInnerInnerPost = "";	
		
		List headers = this.helpers.analyzeResponse(currentRequest.getResponse()).getHeaders();
		postBody = (new String(currentRequest.getResponse())).substring(this.helpers.analyzeResponse(currentRequest.getResponse()).getBodyOffset());

//		System.out.println("Original Response:\n" + headers + postBody + "\n");
			
		if (postBody.matches(".*Payload.*") == true) {
			
			System.out.println("PAYLOAD MATCH");
			
			if (postBody.matches(".*Data.*") == true) {
				postBodyInnerPre = postBody.split("Data\":\"")[0] + "Data\":\"";
				postBodyInner = postBody.split("Data\":\"")[1];
			
				postBodyInner = postBodyInner.split("\"")[0];
				postBodyInnerPost = "\"";
				for (int i = 1; i < postBodyInner.split("\"").length; i++){
					postBodyInnerPost = postBodyInnerPost + postBodyInner.split("\"")[i];
				}
				postBodyInner = new String(helpers.base64Decode(postBodyInner));			
			} else if (postBody.matches(".*Message.*") == true) {
				postBodyInnerPre = postBody.split("Message\":\"")[0] + "Message\":\"";
				postBodyInner = postBody.split("Message\":\"")[1];
			
				postBodyInner = postBodyInner.split("\"")[0];
				postBodyInnerPost = "\"";
				for (int i = 1; i < postBodyInner.split("\"").length; i++){
					postBodyInnerPost = postBodyInnerPost + postBodyInner.split("\"")[i];
				}
				postBodyInner = new String(helpers.base64Decode(postBodyInner));
			} else {
				System.out.println("NO ENCODED FIELDS\n\n");
				return currentRequest.getResponse();
			}
 						
			if (postBodyInner.matches(".*Body.*") == true) {
				postBodyInnerInnerPre = postBodyInner.split("Body\":\"")[0] + "Body\":\"";
				postBodyInnerInner = postBodyInner.split("Body\":\"")[1];	
				postBodyInnerInnerPost = "\"" + postBodyInnerInner.split("(.*?)\"")[1];
				postBodyInnerInner = postBodyInnerInner.split("\"")[0];
				postBodyInnerInnerPost = "\"";
				for (int i = 1; i < postBodyInnerInner.split("\"").length; i++){
					postBodyInnerInnerPost = postBodyInnerInnerPost + postBodyInnerInner.split("\"")[i];
				}
				
				postBodyInnerInner = new String(helpers.base64Decode(postBodyInnerInner));
			}  else {
				postBody = postBodyInnerPre + postBodyInner + postBodyInnerPost;
				System.out.println("REWRITING RESPONSE 1\n\n");
				return helpers.buildHttpMessage(headers, postBody.getBytes());
			}			
			postBody = postBodyInnerPre + postBodyInnerInnerPre + postBodyInnerInner + postBodyInnerInnerPost + postBodyInnerPost;
			System.out.println("REWRITING RESPONSE 2\n\n");
			return helpers.buildHttpMessage(headers, postBody.getBytes());	
		} else {
			System.out.println("NO MATCH\n\n");
			return currentRequest.getResponse();
		}		
	}

	public byte[] encodeRequest(IHttpRequestResponse currentRequest) {
		// TODO Auto-generated method stub
		System.out.println("--REQUEST--");
		String field = "";
		String postBody = "";
		
		String postBodyInner = "";
		String postBodyInnerPre = "";
		String postBodyInnerPost = "";
		
		String postBodyInnerInner = "";	
		String postBodyInnerInnerPre = "";	
		String postBodyInnerInnerPost = "";	
		
		field = this.helpers.analyzeRequest(currentRequest).getMethod().toUpperCase();
		
		if (field == "POST") {
			System.out.println("MSG: METHOD POST");			
			postBody = (new String(currentRequest.getRequest())).substring(this.helpers.analyzeRequest(currentRequest.getRequest()).getBodyOffset());
			System.out.println("postBody: " + postBody + "\n");
			
			if (postBody.matches(".*Payload.*") == true) {
				System.out.println("PAYLOAD MATCH");
				if (postBody.matches(".*Body.*") == true || postBody.matches(".*Message.*") == true) {
					
					if (postBody.matches(".*Body.*") == true) {
						System.out.println("BODY MATCH\n\n");					
						postBodyInnerInnerPre = postBody.split("Body\":\"")[0] + "Body\":\"";
						postBodyInnerInner = postBody.split("Body\":\"")[1];
						postBodyInnerInner = postBodyInnerInner.split("\"}\"}}")[0];
						postBodyInnerInnerPost = "\"}\"}}";
						postBodyInnerInner = new String(helpers.base64Encode(postBodyInnerInner));
						postBody = postBodyInnerInnerPre + postBodyInnerInner + postBodyInnerInnerPost;
						System.out.println("postBody1: " + postBody + "\n");
					} else {
						System.out.println("MESSAGE MATCH");					
						postBodyInnerInnerPre = postBody.split("Message\":\"")[0] + "Message\":\"";
						postBodyInnerInner = postBody.split("Message\":\"")[1];	
						postBodyInnerInner = postBodyInnerInner.split("\"}}")[0] + "\"}}";
						postBodyInnerInnerPost = "\"}}";
						if (postBodyInnerInner.matches(".*:.*") == true) {	
							System.out.println("MESSAGE MATCH2\n\n");	
							postBodyInnerInner = new String(helpers.base64Encode(postBodyInnerInner));
							postBody = postBodyInnerInnerPre + postBodyInnerInner + postBodyInnerInnerPost;
							System.out.println("postBody1: " + postBody + "\n");
							List headers = this.helpers.analyzeRequest(currentRequest).getHeaders();
							return helpers.buildHttpMessage(headers, postBody.getBytes());
						} else { 
							System.out.println("postBodyInnerInner: " + postBodyInnerInner);	
							System.out.println("NO MATCH\n\n");	
							return null; 
							}
					}
					

					//##############################
					
					
					if (postBody.matches(".*Data.*") == true) {
						System.out.println("DATA MATCH\n\n");
						postBodyInnerPre = postBody.split("Data\":\"")[0] + "Data\":\"";
					
						postBodyInner = postBody.split("Data\":\"")[1];				
						postBodyInner = postBodyInner.split("\"}}")[0];
						postBodyInnerPost = "\"}}";
				
						postBodyInner = new String(helpers.base64Encode(postBodyInner));
						postBody = postBodyInnerPre + postBodyInner + postBodyInnerPost;
						System.out.println("postBody2: " + postBody + "\n");						
					}					
					List headers = this.helpers.analyzeRequest(currentRequest).getHeaders();
					return helpers.buildHttpMessage(headers, postBody.getBytes());
				}
			} 
		}
		System.out.println("NO MATCH\n\n");
		return null;
	}

}
