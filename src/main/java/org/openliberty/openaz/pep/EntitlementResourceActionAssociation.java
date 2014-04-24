package org.openliberty.openaz.pep;

import org.openliberty.openaz.azapi.AzEntity;
import org.openliberty.openaz.azapi.AzResourceActionAssociation;
import org.openliberty.openaz.azapi.AzResourceActionAssociationId;
import org.openliberty.openaz.azapi.constants.AzCategoryIdAction;
import org.openliberty.openaz.azapi.constants.AzCategoryIdResource;

public class EntitlementResourceActionAssociation implements
		AzResourceActionAssociation {

	@Override
	public boolean equals(AzResourceActionAssociation arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AzEntity<AzCategoryIdAction> getAzAction() {
		return null;
	}

	@Override
	public AzEntity<AzCategoryIdResource> getAzResource() {
		return null;
	}

	@Override
	public AzResourceActionAssociationId getAzResourceActionAssociationId() {
		return null;
	}

	@Override
	public int getCorrelationId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public EntitlementObject getEntitlementAction() {

	}

	public EntitlementObject getEntitlementResource() {

	}

}
