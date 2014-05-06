/**
 * Copyright 2009 Oracle, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 * Authors:
 * 		Rich Levinson (Oracle)
 * Contributor:
 * 		Rich Levinson (Oracle)
 */
package org.wso2.openaz.pep.entitlement;
//import org.openliberty.openaz.azapi.AzAction;
//import org.openliberty.openaz.azapi.AzResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openliberty.openaz.azapi.*;
import org.openliberty.openaz.azapi.constants.*;

public class EntitlementResourceActionAssociation 
	implements AzResourceActionAssociation {
	int localId = 0;
	AzEntity<AzCategoryIdResource> azResource = null;
	AzEntity<AzCategoryIdAction> azAction = null;
	EntitlementResourceActionAssociationId entitlementResourceActionAssociationId = null;
	Log log = LogFactory.getLog(this.getClass()); 
	
	public EntitlementResourceActionAssociation(
			AzEntity<AzCategoryIdResource> azResource, 
			AzEntity<AzCategoryIdAction> azAction, int localId){
		this.azResource = azResource;
		this.azAction = azAction;
		this.localId = localId;
		if (log.isTraceEnabled()) log.trace(
			"\n********************************************" + 
	  		"*********************************************" +
	  		"\n   Constructor created new AzResourceActionAssociation with: " +
			"\n\t resource id:    " + azResource.getId() +
			"\n\t action id:      " + azAction.getId() +
			"\n\t azRaa localId:  " + localId +
			"\n********************************************" + 
	  		"*********************************************\n");
		this.entitlementResourceActionAssociationId = 
			new EntitlementResourceActionAssociationId(azResource, azAction);
	}
	public AzEntity<AzCategoryIdAction> getAzAction(){
		return null;
	}
	public AzEntity<AzCategoryIdResource> getAzResource(){
		return null;
	}
	public AzResourceActionAssociationId getAzResourceActionAssociationId(){
		return null;
	}
	public boolean equals(AzResourceActionAssociation azResourceActionAssociation){
			return false;
	}
	public AzEntity<AzCategoryIdAction> getEntitlementAction(){
		return azAction;
	}
	public AzEntity<AzCategoryIdResource> getEntitlementResource(){
		return azResource;
	}
	public AzResourceActionAssociationId getEntitlementResourceActionAssociationId(){
		return entitlementResourceActionAssociationId;
	}
	public boolean equals(EntitlementResourceActionAssociation entitlementResourceActionAssociation){
		if (entitlementResourceActionAssociation.getEntitlementResource().equals(this.azResource) &&
				entitlementResourceActionAssociation.getEntitlementAction().equals(this.azAction))
			return true;
		else
			return false;
	}
	public int getCorrelationId(){
		return localId;
	}
}
