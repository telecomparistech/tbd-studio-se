// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.nosql.db.util.couchdb;

import java.util.ArrayList;
import java.util.List;

import org.talend.repository.model.nosql.NoSQLConnection;
import org.talend.repository.nosql.factory.NoSQLClassLoaderFactory;
import org.talend.repository.nosql.reflection.NoSQLReflection;

/**
 * created by cmeng on Dec 25, 2014 Detailled comment
 *
 */
@SuppressWarnings("nls")
public class LightCouchReflection {

    protected ClassLoader classLoader = null;

    protected static LightCouchReflection lightCouch = null;

    public static LightCouchReflection getInstance(NoSQLConnection connection) {
        if (lightCouch == null) {
            lightCouch = new LightCouchReflection(NoSQLClassLoaderFactory.getClassLoader(connection));
        }
        return lightCouch;
    }

    private LightCouchReflection(ClassLoader clsLoader) {
        classLoader = clsLoader;
    }

    public class CouchDbClient {

        Object couchDbClient;

        public CouchDbClient(String dbName, boolean createDbIfNotExist, String protocol, String host, int port, String username,
                String password) throws Exception {
            couchDbClient = NoSQLReflection.newInstance("org.lightcouch.CouchDbClient", new Object[] { dbName,
                    createDbIfNotExist, protocol, host, port, username, password }, LightCouchReflection.this.classLoader,
                    String.class, boolean.class, String.class, String.class, int.class, String.class, String.class);
        }

        public void shutdown() throws Exception {
            NoSQLReflection.invokeMethod(couchDbClient, "shutdown", new Object[0], new Class[0]);
        }

        public CouchDbContext context() throws Exception {
            Object objCouchDbContext = NoSQLReflection.invokeMethod(couchDbClient, "context", new Object[0], new Class[0]);
            if (objCouchDbContext != null) {
                return new CouchDbContext(objCouchDbContext);
            } else {
                return null;
            }
        }

        public Replication replication() throws Exception {
            Object objReplication = NoSQLReflection.invokeMethod(couchDbClient, "replication", new Object[0], new Class[0]);
            if (objReplication != null) {
                return new Replication(objReplication);
            } else {
                return null;
            }
        }
    }

    public class CouchDbContext {

        Object couchDbContext;

        public CouchDbContext(Object couchDbContextReflection) {
            this.couchDbContext = couchDbContextReflection;
        }

        @SuppressWarnings("unchecked")
        public List<String> getAllDbs() throws Exception {
            List<String> allDbs = null;

            Object objAllDbs = NoSQLReflection.invokeMethod(couchDbContext, "getAllDbs", new Object[0], new Class[0]);
            if (objAllDbs != null) {
                allDbs = (List<String>) objAllDbs;
            } else {
                allDbs = new ArrayList<String>();
            }

            return allDbs;
        }
    }

    public class Replication {

        Object replication;

        public Replication(Object replicationReflection) {
            replication = replicationReflection;
        }

        public Replication source(String source) throws Exception {
            Replication retReplication = null;

            Object objReplication = NoSQLReflection.invokeMethod(replication, "source", new Object[] { source }, String.class);
            if (objReplication != null) {
                if (replication == objReplication) {
                    retReplication = this;
                } else {
                    retReplication = new Replication(objReplication);
                }
            }
            return retReplication;
        }

        public Replication target(String target) throws Exception {
            Replication retReplication = null;

            Object objReplication = NoSQLReflection.invokeMethod(replication, "target", new Object[] { target }, String.class);
            if (objReplication != null) {
                if (replication == objReplication) {
                    retReplication = this;
                } else {
                    retReplication = new Replication(objReplication);
                }
            }
            return retReplication;
        }

        public Replication continuous(Boolean continuous) throws Exception {
            Replication retReplication = null;

            Object objReplication = NoSQLReflection.invokeMethod(replication, "continuous", new Object[] { continuous },
                    Boolean.class);
            if (objReplication != null) {
                if (replication == objReplication) {
                    retReplication = this;
                } else {
                    retReplication = new Replication(objReplication);
                }
            }
            return retReplication;
        }

        public Replication createTarget(Boolean createTarget) throws Exception {
            Replication retReplication = null;

            Object objReplication = NoSQLReflection.invokeMethod(replication, "createTarget", new Object[] { createTarget },
                    Boolean.class);
            if (objReplication != null) {
                if (replication == objReplication) {
                    retReplication = this;
                } else {
                    retReplication = new Replication(objReplication);
                }
            }
            return retReplication;
        }

        public Replication cancel(Boolean cancel) throws Exception {
            Replication retReplication = null;

            Object objReplication = NoSQLReflection.invokeMethod(replication, "cancel", new Object[] { cancel }, Boolean.class);
            if (objReplication != null) {
                if (replication == objReplication) {
                    retReplication = this;
                } else {
                    retReplication = new Replication(objReplication);
                }
            }
            return retReplication;
        }

        public ReplicationResult trigger() throws Exception {
            ReplicationResult retReplicationResult = null;

            Object objReplicationResult = NoSQLReflection.invokeMethod(replication, "trigger", new Object[0], new Class[0]);
            if (objReplicationResult != null) {
                retReplicationResult = new ReplicationResult(objReplicationResult);
            }
            return retReplicationResult;
        }
    }

    public class ReplicationResult {

        Object replicationResult;

        public ReplicationResult(Object replicationResultReflection) {
            replicationResult = replicationResultReflection;
        }

        public boolean isOk() throws Exception {
            boolean isOk = false;

            Object objIsOk = NoSQLReflection.invokeMethod(replicationResult, "isOk", new Object[0], new Class[0]);
            if (objIsOk != null) {
                isOk = (Boolean) objIsOk;
            }

            return isOk;
        }
    }
}
