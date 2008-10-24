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
/**
 * To change this template use File | Settings | File Templates.
 */
package org.apache.synapse.util.datasource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.util.datasource.factory.DataSourceFactory;
import org.apache.synapse.util.MBeanRepository;
import org.apache.synapse.util.MBeanRegistrar;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Keeps all DataSources in the memory
 */
public class InMemoryDataSourceRegistry implements DataSourceRegistry, MBeanRepository {

    private final static Log log = LogFactory.getLog(InMemoryDataSourceRegistry.class);

    private static final InMemoryDataSourceRegistry ourInstance = new InMemoryDataSourceRegistry();
    private final static Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
    private final static Map<String, DBPoolView> dataSourcesMBeans = new HashMap<String, DBPoolView>();
    private final static String MBEAN_CATEGORY_DATABASE_CONNECTION_POOL = "DatabaseConnectionPool";

    public static InMemoryDataSourceRegistry getInstance() {
        return ourInstance;
    }

    private InMemoryDataSourceRegistry() {
    }

    /**
     * Keep DataSource in the Local store
     *
     * @see org.apache.synapse.util.datasource.DataSourceRegistry#register(DataSourceInformation)
     */
    public void register(DataSourceInformation information) {

        if (information == null) {
            handleException("DataSourceInformation cannot be found.");
        }

        DataSource dataSource = DataSourceFactory.createDataSource(information);

        if (dataSource == null) {

            if (log.isDebugEnabled()) {
                log.debug("DataSource cannot be created or" +
                        " found for DataSource Information " + information);
            }
            return;
        }

        String name = information.getName();

        if (log.isDebugEnabled()) {
            log.debug("Registering a DatSource with name : " + name + " in Local Pool");
        }

        addMBean(name, new DBPoolView(name));
        dataSources.put(name, dataSource);
    }

    /**
     * Get a DataSource from Local store
     *
     * @see org.apache.synapse.util.datasource.DataSourceRegistry#lookUp(String)
     */
    public DataSource lookUp(String name) {

        assertNull(name, "DataSorce name cannot be found.");
        return dataSources.get(name);
    }

    public void init(Properties properties) {
        // nothing
    }

    public boolean isInitialized() {
        return true;
    }

    public void clear() {
        if (!dataSources.isEmpty()) {
            log.info("Clearing all in-memory datasources ");
            dataSources.clear();
        }
        if (!dataSourcesMBeans.isEmpty()) {
            log.info("UnRegistering DBPool MBeans");
            for (DBPoolView dbPoolView : dataSourcesMBeans.values()) {
                if (dbPoolView != null) {
                    MBeanRegistrar.getInstance().unRegisterMBean(
                            MBEAN_CATEGORY_DATABASE_CONNECTION_POOL, dbPoolView.getName());
                }
            }
            dataSourcesMBeans.clear();
        }
    }

    private static void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }

    public void addMBean(String name, Object mBean) {
        assertNull(name, "DataSorce MBean name cannot be found.");
        assertNull(mBean, "DataSorce MBean  cannot be found.");
        assertFalse(mBean instanceof DBPoolView, "Given MBean instance is not matched " +
                "with the expected MBean - 'DBPoolView'.");
        dataSourcesMBeans.put(name, (DBPoolView) mBean);
        MBeanRegistrar mBeanRegistrar = MBeanRegistrar.getInstance();
        mBeanRegistrar.registerMBean(mBean, MBEAN_CATEGORY_DATABASE_CONNECTION_POOL, name);
    }

    public Object getMBean(String name) {
        assertNull(name, "DataSorce MBean name cannot be found.");
        return dataSourcesMBeans.get(name);
    }

    private void assertNull(String name, String msg) {
        if (name == null || "".equals(name)) {
            handleException(msg);
        }
    }

    private void assertNull(Object object, String msg) {
        if (object == null) {
            handleException(msg);
        }
    }

    private void assertFalse(boolean condition, String msg) {
        if (!condition) {
            handleException(msg);
        }
    }
}
