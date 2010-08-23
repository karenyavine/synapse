/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.builtin.ValidateMediator;
import org.apache.synapse.mediators.MediatorProperty;

import java.util.List;

/**
 * Serializer for {@link ValidateMediator} instances.
 * 
 * @see ValidateMediatorSerializer
 */
public class ValidateMediatorSerializer extends AbstractListMediatorSerializer
        implements MediatorSerializer {

    public OMElement serializeSpecificMediator(OMElement parent, Mediator m) {

        if (!(m instanceof ValidateMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        ValidateMediator mediator = (ValidateMediator) m;
        OMElement validate = fac.createOMElement("validate", synNS);
        saveTracingState(validate, mediator);

        if (mediator.getSource() != null) {
            SynapseXPathSerializer.serializeXPath(mediator.getSource(), validate, "source");
        }

        for (String key : mediator.getSchemaKeys()) {
            OMElement schema = fac.createOMElement("schema", synNS, validate);
            schema.addAttribute(fac.createOMAttribute("key", nullNS, key));
        }

        List<MediatorProperty> features = mediator.getFeatures();
        if (!features.isEmpty()) {
            for (MediatorProperty mp : features) {
                OMElement feature = fac.createOMElement("feature", synNS, validate);
                if (mp.getName() != null) {
                    feature.addAttribute(fac.createOMAttribute("name", nullNS, mp.getName()));
                } else {
                    handleException("The Feature name is missing");
                }
                if (mp.getValue() != null) {
                    feature.addAttribute(fac.createOMAttribute("value", nullNS, mp.getValue()));
                } else {
                    handleException("The Feature value is missing");
                }
            }
        }
        OMElement onFail = fac.createOMElement("on-fail", synNS, validate);
        serializeChildren(onFail, mediator.getList());

        if (parent != null) {
            parent.addChild(validate);
        }
        return validate;
    }

    public String getMediatorClassName() {
        return ValidateMediator.class.getName();
    }

}
