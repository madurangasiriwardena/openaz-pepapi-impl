package client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.openliberty.openaz.azapi.AzService;
import org.openliberty.openaz.azapi.pep.PepRequest;
import org.openliberty.openaz.azapi.pep.PepRequestFactory;
import org.openliberty.openaz.azapi.pep.PepResponse;
import org.openliberty.openaz.pep.PepRequestFactoryImpl;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.identity.entitlement.EntitlementServiceClient;

public class TestClient {
	
	public static void main(String args[]){
		String session;
		AzService entitlementService;
		try {
			session = EntitlementServiceClient.getSession();
			entitlementService = new EntitlementServiceClient(session, EntitlementServiceClient.backEndUrl);
			PepRequestFactory pepRequestFactory = new PepRequestFactoryImpl("ENTITLEMENT_SERVICE", entitlementService);
			
			PepRequest request = pepRequestFactory.newPepRequest("bob", "read", "https://localhost:9443/services/EntitlementService");
			
			PepResponse response = request.decide();
			System.out.println(response.allowed());
			EntitlementServiceClient.logout();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LoginAuthenticationExceptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LogoutAuthenticationExceptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
