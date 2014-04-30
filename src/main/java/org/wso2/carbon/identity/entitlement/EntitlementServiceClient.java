package org.wso2.carbon.identity.entitlement;

import java.rmi.RemoteException;
import java.util.Set;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openliberty.openaz.azapi.AzAttributeFinder;
import org.openliberty.openaz.azapi.AzRequestContext;
import org.openliberty.openaz.azapi.AzResourceActionAssociation;
import org.openliberty.openaz.azapi.AzResponseContext;
import org.openliberty.openaz.azapi.AzService;
import org.openliberty.openaz.azapi.constants.AzCategoryId;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.identity.entitlement.objects.EntitlementRequestContext;
import org.wso2.carbon.identity.entitlement.stub.EntitlementServiceStub;
import org.wso2.carbon.identity.entitlement.EntitlementServiceClient;

import org.wso2.carbon.identity.entitlement.LoginAdminServiceClient;

public class EntitlementServiceClient implements AzService {
	
	private EntitlementServiceStub stub;
    private static final Log log = LogFactory.getLog(EntitlementServiceClient.class);
    public static String backEndUrl = "https://localhost:9443/services/";
    static LoginAdminServiceClient login;
	
	public EntitlementServiceClient(String cookie, String backendServerURL) throws AxisFault {
        String serviceURL = backendServerURL + "EntitlementService";
        stub = new EntitlementServiceStub(serviceURL);
        ServiceClient client = stub._getServiceClient();
        Options option = client.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);
    }
	
	public String getDecision(String request) throws AxisFault {
		System.out.println(request);
        try {
            if(request != null){
                request = request.trim().replaceAll("&lt;", "<"); //TODO should be properly fixed
                request = request.trim().replaceAll("&gt;", ">");                 
            }
            System.out.println(request);
            return stub.getDecision(request);
        } catch (Exception e) {
        	e.printStackTrace();
            handleException("Error occurred while policy evaluation", e);
        }
        return null;
    }
	
	private void handleException(String msg, Exception e) throws AxisFault {
        log.error(msg, e);
        throw new AxisFault(msg);
    }
	
	public static String getSession() throws RemoteException, LoginAuthenticationExceptionException{
		System.setProperty(
				"javax.net.ssl.trustStore",
				"/home/maduranga/WSO2/IS/21-04-2014/wso2is-5.0.0/repository/resources/security/wso2carbon.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
		System.setProperty("javax.net.ssl.t" + "rustStoreType", "JKS");
		
		login = new LoginAdminServiceClient(EntitlementServiceClient.backEndUrl);
		String session = login.authenticate("admin", "admin");
		
		return session;
	}
	
	public static void logout() throws RemoteException, LogoutAuthenticationExceptionException{
		login.logOut();
	}

	@Override
	public AzRequestContext createAzRequestContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AzResponseContext decide(AzRequestContext arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<AzResourceActionAssociation> query(String arg0,
			AzRequestContext arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AzResponseContext queryVerbose(String arg0, AzRequestContext arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Enum<T> & AzCategoryId> void registerAzAttributeFinder(
			AzAttributeFinder<T> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public AzRequestContext createEntitlementRequestContext() {
		AzRequestContext reqCtx = new EntitlementRequestContext();
		return reqCtx;
	}
}
