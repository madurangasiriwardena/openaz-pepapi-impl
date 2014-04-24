package org.wso2.carbon.identity.entitlement;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.entitlement.stub.EntitlementServiceStub;
import org.wso2.carbon.identity.entitlement.EntitlementServiceClient;

public class EntitlementServiceClient {
	
	private EntitlementServiceStub stub;
    private static final Log log = LogFactory.getLog(EntitlementServiceClient.class);
	
	public EntitlementServiceClient(String cookie, String backendServerURL,
            ConfigurationContext configCtx) throws AxisFault {
        String serviceURL = backendServerURL + "EntitlementService";
        stub = new EntitlementServiceStub(configCtx, serviceURL);
        ServiceClient client = stub._getServiceClient();
        Options option = client.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);
    }
	
	public String getDecision(String request) throws AxisFault {
        try {
            if(request != null){
                request = request.trim().replaceAll("&lt;", "<"); //TODO should be properly fixed
                request = request.trim().replaceAll("&gt;", ">");                 
            }
            return stub.getDecision(request);
        } catch (Exception e) {
            handleException("Error occurred while policy evaluation", e);
        }
        return null;
    }
	
	private void handleException(String msg, Exception e) throws AxisFault {
        log.error(msg, e);
        throw new AxisFault(msg);
    }
}
