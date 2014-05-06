package client;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import java.util.Iterator;

import org.apache.axis2.AxisFault;
import org.openliberty.openaz.azapi.AzAttribute;
import org.openliberty.openaz.azapi.AzEntity;
import org.openliberty.openaz.azapi.AzService;
import org.openliberty.openaz.azapi.constants.AzCategoryIdObligation;
import org.openliberty.openaz.azapi.pep.Obligation;
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
			Map<String, Obligation> obligations = response.getObligations();
			for (String entry : obligations.keySet())
			{
				AzEntity<AzCategoryIdObligation> temp = obligations.get(entry).getAzEntity();
//				for (String entry1 : temp.keySet()){
//					System.out.println(temp.get(entry1));
//				}
				Set<AzAttribute<?>> mixedset = temp.getAzAttributeMixedSet();
				
				Iterator<AzAttribute<?>> itr = mixedset.iterator();
				while(itr.hasNext()){
					System.out.println(itr.next().getAzAttributeValue());
				}
				
			    
			}
			
			System.out.println();
			
			
			System.out.println();
			
			PepRequest request2 = pepRequestFactory.newPepRequest("alice", "read", "https://localhost:9443/services/EntitlementService");
			
			PepResponse response2 = request2.decide();
			System.out.println(response2.allowed());
			
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
