package org.openliberty.openaz.pep;

import java.util.Set;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openliberty.openaz.azapi.pep.PepRequest;
import org.openliberty.openaz.azapi.constants.PepRequestOperation;

import org.openliberty.openaz.azapi.pep.PepResponse;
import org.openliberty.openaz.azapi.pep.DecisionHandler;
import org.openliberty.openaz.azapi.pep.PreDecisionHandler;
import org.openliberty.openaz.azapi.pep.PostDecisionHandler;

import org.openliberty.openaz.azapi.AzResourceActionAssociation;
import org.openliberty.openaz.azapi.AzResponseContext;
import org.openliberty.openaz.azapi.AzService;

import org.wso2.carbon.identity.entitlement.EntitlementServiceClient;
//import org.openliberty.openaz.pdp.AzServiceFactory;
//import org.openliberty.openaz.pdp.provider.AzServiceFactory;
import org.wso2.carbon.identity.entitlement.objects.EntitlementServiceFactory;
/**
 * Provides a default implementation of the decide() method 
 * and has null implementations of preDecide() and postDecide().
 * @author Josh Bregman, Rich Levinson, Prateek Mishra
 *
 */
public class DefaultDecisionHandler 
	implements DecisionHandler, 
				PreDecisionHandler,
                PostDecisionHandler {

    private Log log = LogFactory.getLog(this.getClass());
    
    /** 
    * Implements the decide() method by
    * using the underlying AzService.decide(requestContext) method to
    * execute the request, and handles the AzResponseContext that is 
    * returned by wrapping it in a PepResponse. 
    * <p>
    * @param request a pepRequest object populated with the attributes
    * to be included with the request.
    * @return a pepResponse object containing one or more XACML Results
    */
    public PepResponse decide(PepRequest request) {
    	String requestStr = "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\"><Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\"><Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" IncludeInResult=\"false\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">read</AttributeValue></Attribute></Attributes><Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\"><Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">bob</AttributeValue></Attribute></Attributes><Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\"><Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" IncludeInResult=\"false\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">https://localhost:9443/services/EntitlementService</AttributeValue></Attribute></Attributes></Request>";
    	System.out.println(requestStr);
    	String response;
    	try {
    		response = ((EntitlementServiceClient)EntitlementServiceFactory.getEntitlementService()).getDecision(requestStr);
    		System.out.println(response);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	AzService azHandle = EntitlementServiceFactory.getEntitlementService();
    	AzResponseContext azRspCtx = azHandle.decide(request.getAzRequestContext());
    	return request.getPepRequestFactory().getResponseFactory().
        		createPepResponse(azRspCtx,
        						  request,
        						  request.getOperation());
    	
//    	// Get handle to AzService
//        AzService azHandle = EntitlementServiceFactory.getAzService();        
//        AzResponseContext azRspCtx = null;
//        if (log.isTraceEnabled()) log.trace(
//        	"\n   Calling decide w PepRequest.getOperation() = " + 
//    		request.getOperation() +
//    		"\n\tusing AzService =   " + azHandle.getClass().getName() + "\n");
//           
//        // If this is decide or bulk-decide call then do the 
//        // ordinary call to underlying azapi AzService.decide():
//        // Note: for bulk requests, the multiple resource action
//        // associations were already built into the AzRequestContext
//        // so there is only one AzRequestContext and one decide() call.
//        // However, multiple results are returned within the 
//        // AzResponseContext.
//        if ((request.getOperation()==PepRequestOperation.DECIDE) ||
//        	(request.getOperation()==PepRequestOperation.BULK_DECIDE)) {
//            azRspCtx = azHandle.decide(request.getAzRequestContext());
//            
//        // If query verbose, this is ordinary use 
//        // AzService.queryVerbose() call which returns ordinary
//        // responses
//        } else if (request.getOperation()==PepRequestOperation.QUERY_VERBOSE) {
//            azRspCtx = azHandle.queryVerbose(
//		            			request.getScope(),
//		            			request.getAzRequestContext());
//        
//        // If "simple" query then use AzService.query() which
//        // returns Set of AzResourceActionAssociation's
//        } else {
//            Set<AzResourceActionAssociation> actionResourceAssociations =
//                azHandle.query(
//                                request.getScope(),
//                                request.getAzRequestContext(),
//                                request.isQueryForAllowedResults());
//            
//            return request.getPepRequestFactory().getResponseFactory().
//            	createPepResponse(actionResourceAssociations,
//            					  request,
//            					  request.isQueryForAllowedResults());            
//        }
//        
//        //Return the PepResponse for the OPERATION.DECIDE or 
//        // OPERATION.QUERY_VERBOSE    
//        return request.getPepRequestFactory().getResponseFactory().
//        		createPepResponse(azRspCtx,
//        						  request,
//        						  request.getOperation());
    }

    /**
     * Null implementation.
     */
    public void preDecide(PepRequest request) {
    }

    /**
     * Null implementation.
     */
    public void postDecide(PepRequest request, PepResponse response) {
    }
}
