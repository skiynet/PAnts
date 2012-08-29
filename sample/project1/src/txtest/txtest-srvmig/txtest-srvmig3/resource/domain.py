edit()
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


if getMBean('/Servers/ManagedServer3') == None:
    cd('/')
    cmo.createServer('ManagedServer3')
cd('/Servers/ManagedServer3')
cmo.setListenAddress('el01bl5')
cmo.setListenPort(9011)
cmo.setMachine(getMBean('/Machines/wls1_nm'))

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
cmo.setDriverName('oraRcle.jdbc.xa.client.OracleXADataSource')
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
cmo.setMaxCapacity(20)
cmo.setMinCapacity(10)
cmo.setInitialCapacity(10)

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




