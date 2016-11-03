package burp;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import netspi.payloadEncoder.base64;

public class BurpExtender implements IBurpExtender, ActionListener, ISessionHandlingAction, IHttpListener
{
	private IBurpExtenderCallbacks callbacks;
	
  	private base64 encoder; 
   // private base64Tab base64Tab; 
    
    public final String TAB_NAME = "base64"; 
    //
    // implement IBurpExtender
    //
    
    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
    	callbacks.registerHttpListener(this);
    	//Set up our extension operations 
    	//this.base64Tab = new base64Tab();
    	this.encoder = new base64(callbacks);
    	//this.encoder.base64Tab = this.base64Tab;
        // obtain an extension helpers object
    	encoder.helpers = callbacks.getHelpers();

        // set our extension name
    	encoder.callbacks.setExtensionName("Base64 Payload Encoder");
        
        //Our main and error output 
    	encoder.stdout = new PrintWriter(encoder.callbacks.getStdout(), true); 
    	encoder.errout = new PrintWriter(encoder.callbacks.getStderr(), true); 
        
		//register session handling functionality
    	encoder.callbacks.registerSessionHandlingAction(this);    	
    }
    
    @Override
    public void performAction(IHttpRequestResponse currentRequest, IHttpRequestResponse[] macroItems) {
    	
    	//currentRequest.setRequest(encoder.encodeRequest(currentRequest));
		 byte[] encoded = encoder.encodeRequest(currentRequest);
		 if ( encoded != null) {
			 currentRequest.setRequest(encoded);
		 }
    }
    
    @Override
    public String getActionName() { return "payloadEncoder"; }    

	 @Override 
	 public void actionPerformed(ActionEvent e) { 
		 //String cmd = e.getActionCommand(); 
		 //signer.ParseAction(cmd); 	   
	 } 
	 
	 public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse currentRequest)
	 {
		 if (toolFlag != 4) {
			 
			 System.out.println(messageIsRequest ? "HTTP request to " : "HTTP response from ");  
			 if (messageIsRequest) {

			 } else {    	 
				 currentRequest.setResponse(encoder.decodeResponse(currentRequest));
			 }
			 
		 }	 
	 }

	 public void processProxyMessage(boolean messageIsRequest, IInterceptedProxyMessage message)
	 {
		 System.out.println(message);
	 }
	
}