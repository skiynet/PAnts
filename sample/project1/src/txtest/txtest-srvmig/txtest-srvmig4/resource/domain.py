edit()
startEdit()

if getMBean('/Clusters/Cluster1') == None:
        edit()
        startEdit()
        cmo.createCluster('Cluster1')
cd('/Clusters/Cluster1')
cmo.setClusterMessagingMode('unicast')

save()
activate()
startEdit()

if getMBean('/Machines/wls1_nm') == None:
        cd('/')
        cmo.createMachine('wls1_nm')
cd('/Machines/wls1_nm/NodeManager/wls1_nm')
cmo.setNMType('Plain')
cmo.setListenAddress('el01bl3')
cmo.setListenPort(5556)
cmo.setDebugEnabled(true)

save()
activate()
startEdit()

if getMBean('/Machines/wls2_nm') == None:
        cd('/')
        cmo.createMachine('wls2_nm')
cd('/Machines/wls2_nm/NodeManager/wls2_nm')
cmo.setNMType('Plain')
cmo.setListenAddress('el01bl4')
cmo.setListenPort(5556)
cmo.setDebugEnabled(true)

save()
activate()
startEdit()


if getMBean('/Servers/ManagedServer3') == None:
    cd('/')
    cmo.createServer('ManagedServer3')
cd('/Servers/ManagedServer3')
cmo.setListenAddress('el01bl3')
cmo.setListenPort(9011)
cmo.setMachine(getMBean('/Machines/wls1_nm'))
cmo.setCluster(getMBean('/Clusters/Cluster1'))

save()
activate()
startEdit()

if getMBean('/Servers/ManagedServer4') == None:
    cd('/')
    cmo.createServer('ManagedServer4')
cd('/Servers/ManagedServer4')
cmo.setListenAddress('el01bl4')
cmo.setListenPort(9012)
cmo.setMachine(getMBean('/Machines/wls2_nm'))
cmo.setCluster(getMBean('/Clusters/Cluster1'))

save()
activate()
startEdit()


cd('/')
if getMBean('/JDBCSystemResources/genDS1') == None:
    cmo.createJDBCSystemResource('genDS1')

cd('/JDBCSystemResources/genDS1/JDBCResource/genDS1')
cmo.setName('genDS1')

cd('/JDBCSystemResources/genDS1/JDBCResource/genDS1/JDBCDataSourceParams/genDS1')
set('JNDINames',jarray.array([String('jdbc/OracleDS')], String))

cd('/JDBCSystemResources/genDS1/JDBCResource/genDS1/JDBCDriverParams/genDS1')
cmo.setUrl('jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=scan.exalogic.com)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=srvtx1b)(INSTANCE_NAME=orcl1)))')
cmo.setDriverName('oracle.jdbc.xa.client.OracleXADataSource')
cmo.setPassword('hr')

cd('/JDBCSystemResources/genDS1/JDBCResource/genDS1/JDBCConnectionPoolParams/genDS1')
cmo.setTestTableName('SQL SELECT 1 FROM DUAL\r\n\r\n')

cd('/JDBCSystemResources/genDS1/JDBCResource/genDS1/JDBCDriverParams/genDS1/Properties/genDS1')

if getMBean('/JDBCSystemResources/genDS1/JDBCResource/genDS1/JDBCDriverParams/genDS1/Properties/genDS1/Properties/user') == None:
    cmo.createProperty('user')
cd('/JDBCSystemResources/genDS1/JDBCResource/genDS1/JDBCDriverParams/genDS1/Properties/genDS1/Properties/user')
cmo.setValue('hr')

cd('/JDBCSystemResources/genDS1/JDBCResource/genDS1/JDBCDataSourceParams/genDS1')
cmo.setGlobalTransactionsProtocol('TwoPhaseCommit')

cd('/JDBCSystemResources/genDS1/JDBCResource/genDS1/JDBCConnectionPoolParams/genDS1')

cd('/JDBCSystemResources/genDS1')
set('Targets',jarray.array([ObjectName('com.bea:Name=ManagedServer3,Type=Server')], ObjectName))

save()
activate()

startEdit()

cd('/')
if getMBean('/JDBCSystemResources/genDS2') == None:
    cmo.createJDBCSystemResource('genDS2')
    
cd('/JDBCSystemResources/genDS2/JDBCResource/genDS2')
cmo.setName('genDS2')

cd('/JDBCSystemResources/genDS2/JDBCResource/genDS2/JDBCDataSourceParams/genDS2')
set('JNDINames',jarray.array([String('jdbc/OracleDS2')], String))

cd('/JDBCSystemResources/genDS2/JDBCResource/genDS2/JDBCDriverParams/genDS2')
cmo.setUrl('jdbc:oracle:thin:@el01bl1:1521:orcl')
cmo.setDriverName('oracle.jdbc.xa.client.OracleXADataSource')
cmo.setPassword('hr')

cd('/JDBCSystemResources/genDS2/JDBCResource/genDS2/JDBCConnectionPoolParams/genDS2')
cmo.setTestTableName('SQL SELECT 1 FROM DUAL\r\n\r\n')

cd('/JDBCSystemResources/genDS2/JDBCResource/genDS2/JDBCDriverParams/genDS2/Properties/genDS2')
if getMBean('/JDBCSystemResources/genDS2/JDBCResource/genDS2/JDBCDriverParams/genDS2/Properties/genDS2/Properties/user') == None:
    cmo.createProperty('user')

cd('/JDBCSystemResources/genDS2/JDBCResource/genDS2/JDBCDriverParams/genDS2/Properties/genDS2/Properties/user')
cmo.setValue('hr')

cd('/JDBCSystemResources/genDS2/JDBCResource/genDS2/JDBCDataSourceParams/genDS2')
cmo.setGlobalTransactionsProtocol('TwoPhaseCommit')

cd('/JDBCSystemResources/genDS2')
set('Targets',jarray.array([ObjectName('com.bea:Name=ManagedServer3,Type=Server')], ObjectName))

save()
activate()
startEdit()

cd('/Servers/ManagedServer3/WebServer/ManagedServer3/WebServerLog/ManagedServer3')
cmo.setLoggingEnabled(false)

save()
activate()



#------------------------------------------------




startEdit()

cd('/Clusters/Cluster1')
cmo.setMigrationBasis('consensus')
set('CandidateMachinesForMigratableServers',jarray.array([ObjectName('com.bea:Name=wls1_nm,Type=Machine'), ObjectName('com.bea:Name=wls2_nm,Type=Machine')], ObjectName))

cd('/Servers/ManagedServer3/JTAMigratableTarget/ManagedServer3')
set('ConstrainedCandidateServers',jarray.array([ObjectName('com.bea:Name=ManagedServer3,Type=Server'), ObjectName('com.bea:Name=ManagedServer4,Type=Server')], ObjectName))
cmo.setMigrationPolicy('failure-recovery')

cd('/Servers/ManagedServer4/JTAMigratableTarget/ManagedServer4')
set('ConstrainedCandidateServers',jarray.array([ObjectName('com.bea:Name=ManagedServer3,Type=Server'), ObjectName('com.bea:Name=ManagedServer4,Type=Server')], ObjectName))
cmo.setMigrationPolicy('failure-recovery')

cd('/Servers/ManagedServer3/DefaultFileStore/ManagedServer3')
cmo.setDirectory('/export/common/general/txtest/antserver/txtest-srvmig4/tlog')

cd('/Servers/ManagedServer3/TransactionLogJDBCStore/ManagedServer3')
cmo.setEnabled(false)

cd('/Servers/ManagedServer4/DefaultFileStore/ManagedServer4')
cmo.setDirectory('/export/common/general/txtest/antserver/txtest-srvmig4/tlog')

cd('/Servers/ManagedServer4/TransactionLogJDBCStore/ManagedServer4')
cmo.setEnabled(false)
save()
activate()

