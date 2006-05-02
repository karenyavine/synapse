/*
* Copyright 2004,2005 The Apache Software Foundation.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.apache.synapse.mediators.base;

import org.apache.synapse.SynapseMessage;
import org.apache.synapse.mediators.AbstractListMediator;

/**
 * The Sequence mediator either refers to another Sequence mediator instance
 * or is a *Named* list/sequence of other (child) Mediators
 */
public class SequenceMediator extends AbstractListMediator {

    private String name = null;
    private String ref = null;

    /**
     * If this mediator refers to another named Sequence, execute that. Else
     * execute the list of mediators (children) contained within this.
     *
     * @param synMsg the synapse message
     * @return as per standard mediator result
     */
    public boolean mediate(SynapseMessage synMsg) {
        if (ref == null) {
            return super.mediate(synMsg);
        } else {
            return synMsg.getSynapseEnvironment().getConfiguration().
                getNamedMediator(ref).mediate(synMsg);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
