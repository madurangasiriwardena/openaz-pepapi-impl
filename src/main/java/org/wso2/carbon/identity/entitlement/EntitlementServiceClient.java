package org.wso2.carbon.identity.entitlement;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.identity.entitlement.stub.EntitlementServiceStub;
import org.wso2.carbon.identity.entitlement.EntitlementServiceClient;

import org.wso2.carbon.identity.entitlement.LoginAdminServiceClient;

public class EntitlementServiceClient {
	
	private EntitlementServiceStub stub;
    private static final Log log = LogFactory.getLog(EntitlementServiceClient.class);
	
	public EntitlementServiceClient(String cookie, String backendServerURL) throws AxisFault {
        String serviceURL = backendServerURL + "EntitlementService";
        stub = new EntitlementServiceStub(serviceURL);
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
	
	public static void main(String args[]) throws RemoteException, LoginAuthenticationExceptionException{
		System.setProperty(
				"javax.net.ssl.trustStore",
				"/home/maduranga/WSO2/IS/21-04-2014/wso2is-5.0.0/repository/resources/security/wso2carbon.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
		System.setProperty("javax.net.ssl.t" + "rustStoreType", "JKS");
		String backEndUrl = "https://localhost:9443";
		
		LoginAdminServiceClient login = new LoginAdminServiceClient(backEndUrl);
		String session = login.authenticate("admin", "admin");
		
		String request = "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\"><Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\"><Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" IncludeInResult=\"false\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">read</AttributeValue></Attribute></Attributes><Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\"><Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">bob</AttributeValue></Attribute></Attributes><Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\"><Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" IncludeInResult=\"false\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">https://localhost:9443/services/EntitlementService</AttributeValue></Attribute></Attributes></Request>";
		
		EntitlementServiceClient entitlementServiceClient = new EntitlementServiceClient(session, backEndUrl);
		String decision = entitlementServiceClient.getDecision(request);
		System.out.println(decision);
	}
}
