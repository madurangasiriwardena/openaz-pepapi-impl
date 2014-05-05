package org.wso2.carbon.identity.entitlement;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openliberty.openaz.azapi.AzAttributeFinder;
import org.openliberty.openaz.azapi.AzEntity;
import org.openliberty.openaz.azapi.AzRequestContext;
import org.openliberty.openaz.azapi.AzResourceActionAssociation;
import org.openliberty.openaz.azapi.AzResponseContext;
import org.openliberty.openaz.azapi.AzResult;
import org.openliberty.openaz.azapi.AzService;
import org.openliberty.openaz.azapi.constants.AzCategoryId;
import org.openliberty.openaz.azapi.constants.AzCategoryIdAction;
import org.openliberty.openaz.azapi.constants.AzCategoryIdResource;
import org.openliberty.openaz.azapi.constants.AzCategoryIdSubjectAccess;
import org.openliberty.openaz.azapi.constants.AzDataTypeId;
import org.openliberty.openaz.azapi.constants.AzDecision;
import org.openliberty.openaz.azapi.pep.PepRequest;
import org.openliberty.openaz.pep.PepRequestImpl;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.identity.entitlement.objects.EntitlementAttribute;
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
        try {
            if(request != null){
                request = request.trim().replaceAll("&lt;", "<"); //TODO should be properly fixed
                request = request.trim().replaceAll("&gt;", ">");                 
            }
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
		//String requestStr1 = "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\"><Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\"><Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" IncludeInResult=\"false\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">read</AttributeValue></Attribute></Attributes><Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\"><Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">bob</AttributeValue></Attribute></Attributes><Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\"><Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" IncludeInResult=\"false\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">https://localhost:9443/services/EntitlementService</AttributeValue></Attribute></Attributes></Request>";
    	
    	String requestStr = createRequestString(arg0);
    	AzResult azResult = new EntitlementResultImpl();
    	
    	//AzResponseContext azResponseContext = AzRe
		try {
			String decision = getDecision(requestStr);
			//System.out.println(requestStr);
			//System.out.println(decision);
			
			OMElement documentElement = AXIOMUtil.stringToOM(decision);
			String decisionStr = documentElement.getFirstElement().getFirstElement().getText();
			
			if(decisionStr.equalsIgnoreCase("Permit")){
				((EntitlementResultImpl)azResult).setAzDecision(AzDecision.AZ_PERMIT);
			}else if(decisionStr.equalsIgnoreCase("Deny")){
				((EntitlementResultImpl)azResult).setAzDecision(AzDecision.AZ_DENY);
			}else if(decisionStr.equalsIgnoreCase("Indeterminate")){
				((EntitlementResultImpl)azResult).setAzDecision(AzDecision.AZ_INDETERMINATE);
			}else if(decisionStr.equalsIgnoreCase("NotApplicable")){
				((EntitlementResultImpl)azResult).setAzDecision(AzDecision.AZ_NOTAPPLICABLE);
			}
			
		    System.out.println();
			
		} catch (AxisFault e) {
			e.printStackTrace();
			((EntitlementResultImpl)azResult).setAzDecision(AzDecision.AZ_INDETERMINATE);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			((EntitlementResultImpl)azResult).setAzDecision(AzDecision.AZ_INDETERMINATE);
		}
		
		AzResponseContext azResponseContext = new EntitlementResponseContextImpl();
		((EntitlementResponseContextImpl)azResponseContext).addResult(azResult);
		
		return azResponseContext;
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
	
	private String createRequestString(AzRequestContext azRequestContext){
    	String request = "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">";

    	//AzRequestContext azRequestContext = ((PepRequestImpl)pepRequest).getEntitlementRequestContext();
    	
    	Set<AzEntity<? extends AzCategoryId>> setAction = azRequestContext.getAzEntitySet(AzCategoryIdAction.AZ_CATEGORY_ID_ACTION);
    	Iterator<AzEntity<? extends AzCategoryId>> iterAction = setAction.iterator();
    	while (iterAction.hasNext()) {
    		AzEntity<? extends AzCategoryId> azEntity = iterAction.next();
    		//System.out.println(azEntity.getAzCategoryId());
    		Set<?> azActionAttributeSet = azEntity.getAzAttributeSet();
    		Iterator<?> iterActionAttributes = azActionAttributeSet.iterator();
    		while (iterActionAttributes.hasNext()) {
    			EntitlementAttribute<? extends AzCategoryId, ? extends AzDataTypeId, ?> attribute = (EntitlementAttribute<? extends AzCategoryId, ? extends AzDataTypeId, ?>) iterActionAttributes.next();
    			//System.out.println(attribute.getAzAttributeValue());
    			//System.out.println(attribute.getAttributeId());
    			
    			request += "<Attributes Category=\"" + azEntity.getAzCategoryId() + "\">";
    			request += "<Attribute AttributeId=\"" + attribute.getAttributeId() + "\" IncludeInResult=\"false\">";
    			request += "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + attribute.getAzAttributeValue() + "</AttributeValue>";
    			request += "</Attribute>";
    			request += "</Attributes>";
    		}
    	} 
    	
    	Set<AzEntity<? extends AzCategoryId>> setSubject = azRequestContext.getAzEntitySet(AzCategoryIdSubjectAccess.AZ_CATEGORY_ID_SUBJECT_ACCESS);
    	Iterator<AzEntity<? extends AzCategoryId>> iterSubject = setSubject.iterator();
    	while (iterSubject.hasNext()) {
    		AzEntity<? extends AzCategoryId> azEntity = iterSubject.next();
    		//System.out.println(azEntity.getAzCategoryId());
    		Set<?> azAttributeSet = azEntity.getAzAttributeSet();
    		Iterator<?> iterSubjectAttributes = azAttributeSet.iterator();
    		while (iterSubjectAttributes.hasNext()) {
    			EntitlementAttribute<? extends AzCategoryId, ? extends AzDataTypeId, ?> attribute = (EntitlementAttribute<? extends AzCategoryId, ? extends AzDataTypeId, ?>) iterSubjectAttributes.next();
    			//System.out.println(attribute.getAzAttributeValue());
    			//System.out.println(attribute.getAttributeId());
    			
    			request += "<Attributes Category=\"" + azEntity.getAzCategoryId() + "\">";
    			request += "<Attribute AttributeId=\"" + attribute.getAttributeId() + "\" IncludeInResult=\"false\">";
    			request += "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + attribute.getAzAttributeValue() + "</AttributeValue>";
    			request += "</Attribute>";
    			request += "</Attributes>";
    		}
    	}
    	
    	Set<AzEntity<? extends AzCategoryId>> setResource = azRequestContext.getAzEntitySet(AzCategoryIdResource.AZ_CATEGORY_ID_RESOURCE);
    	Iterator<AzEntity<? extends AzCategoryId>> iterResource = setResource.iterator();
    	while (iterResource.hasNext()) {
    		AzEntity<? extends AzCategoryId> azEntity = iterResource.next();
    		//System.out.println(azEntity.getAzCategoryId());
    		Set<?> azResourceAttributeSet = azEntity.getAzAttributeSet();
    		Iterator<?> iterResourceAttributes = azResourceAttributeSet.iterator();
    		while (iterResourceAttributes.hasNext()) {
    			EntitlementAttribute<? extends AzCategoryId, ? extends AzDataTypeId, ?> attribute = (EntitlementAttribute<? extends AzCategoryId, ? extends AzDataTypeId, ?>) iterResourceAttributes.next();
    			//System.out.println(attribute.getAzAttributeValue());
    			//System.out.println(attribute.getAttributeId());
    			
    			request += "<Attributes Category=\"" + azEntity.getAzCategoryId() + "\">";
    			request += "<Attribute AttributeId=\"" + attribute.getAttributeId() + "\" IncludeInResult=\"false\">";
    			request += "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + attribute.getAzAttributeValue() + "</AttributeValue>";
    			request += "</Attribute>";
    			request += "</Attributes>";
    		}
    	}
    	
    	request += "</Request>";
    	
    	return request;
    }
}
