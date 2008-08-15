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

package org.apache.synapse.transport.testkit;

import org.apache.axis2.description.AxisService;
import org.apache.synapse.transport.testkit.listener.ContentTypeMode;

public abstract class TestEnvironment {
    /**
     * Carry out initialization before server startup. This method is called
     * immediately before the test server is started and can be used by subclasses
     * to set up the test environment.
     * 
     * @throws Exception
     */
    public void setUp() throws Exception {
    }
    
    public void tearDown() throws Exception {
    }
    
    /**
     * Set up the expected content type on the given service. This method should only be
     * implemented for transports that support {@link ContentTypeMode#SERVICE}.
     * The default implementation throws an {@link UnsupportedOperationException}.
     * 
     * @param service
     * @param contentType the content type
     * @throws Exception
     */
    public void setupContentType(AxisService service, String contentType) throws Exception {
        throw new UnsupportedOperationException();
    }
}