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

//import org.openliberty.openaz.azapi.constants.AzDataType;
import org.openliberty.openaz.azapi.*;
import org.openliberty.openaz.azapi.constants.*;

public class EntitlementAttributeValueDayTimeDuration 
	implements AzAttributeValueDayTimeDuration{
	AzDataDayTimeDuration azDTD = null;
	public EntitlementAttributeValueDayTimeDuration(
			AzDataDayTimeDuration azDTD){
		this.azDTD = azDTD;
	}
	public AzDataTypeIdDayTimeDuration getType(){
		return AzDataTypeIdDayTimeDuration.AZ_DATATYPE_ID_DAYTIMEDURATION;
	}
	public void setValue(AzDataDayTimeDuration azDTD){
		this.azDTD = azDTD;
	}
	public AzDataDayTimeDuration getValue() {
		return azDTD;
	}
	public String toXacmlString(){
		String intString = null;
		return intString;
	}
	//@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}
}
