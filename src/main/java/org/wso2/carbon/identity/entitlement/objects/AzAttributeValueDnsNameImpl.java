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
package org.wso2.carbon.identity.entitlement.objects;

//import org.openliberty.openaz.azapi.AzAttributeValueString;
//import org.openliberty.openaz.azapi.AzAttributeValueTypeString;
//import org.openliberty.openaz.azapi.constants.AzDataType;
import org.openliberty.openaz.azapi.*;
import org.openliberty.openaz.azapi.constants.*;

public class AzAttributeValueDnsNameImpl 
	extends AzAttributeValueImpl<AzDataTypeIdDnsName,String> 
	implements AzAttributeValueDnsName {
	String value;
	public AzAttributeValueDnsNameImpl(String s){
		super(AzDataTypeIdDnsName.AZ_DATATYPE_ID_DNSNAME, null);
		this.value = s;
		System.out.println(
			"TestAzAttributeValueRfc822Name: String Created = " + value);
	}
	public AzDataTypeIdDnsName getType(){
		return AzDataTypeIdDnsName.AZ_DATATYPE_ID_DNSNAME;
	}
	public void setValue(String s){
		this.value = s;
	}
	public String getValue() {
		return value;
	}
	public String toXacmlString() {
		return value;
	}
}
