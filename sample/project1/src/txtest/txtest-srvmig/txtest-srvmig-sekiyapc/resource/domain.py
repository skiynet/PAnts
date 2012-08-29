edit()
startEdit()

if getMBean('/Clusters/Cluster1') == None:
        edit()
        startEdit()
        cmo.createCluster('Cluster1')
cd('/Clusters/Cluster1')
cmo.setClusterMessagingMode('unicast')

activate()
startEdit()

if getMBean('/Machines/wls1_nm') == None:
        cd('/')
        cmo.createMachine('wls1_nm')
cd('/Machines/wls1_nm/NodeManager/wls1_nm')
cmo.setNMType('Plain')
cmo.setListenAddress('192.168.0.101')
cmo.setListenPort(5556)
cmo.setDebugEnabled(true)

activate()
startEdit()

if getMBean('/Machines/wls2_nm') == None:
        cd('/')
        cmo.createMachine('wls2_nm')
cd('/Machines/wls2_nm/NodeManager/wls2_nm')
cmo.setNMType('Plain')
cmo.setListenAddress('192.168.0.101')
cmo.setListenPort(5557)
cmo.setDebugEnabled(true)

activate()
startEdit()

if getMBean('/Servers/ManagedServer3') == None:
    cd('/')
    cmo.createServer('ManagedServer3')
cd('/Servers/ManagedServer3')
cmo.setListenAddress('192.168.0.101')
cmo.setListenPort(7002)
cmo.setMachine(getMBean('/Machines/wls1_nm'))
cmo.setCluster(getMBean('/Clusters/Cluster1'))

activate()
startEdit()

if getMBean('/Servers/ManagedServer4') == None:
    cd('/')
    cmo.createServer('ManagedServer4')
cd('/Servers/ManagedServer4')
cmo.setListenAddress('192.168.0.101')
cmo.setListenPort(7003)
cmo.setMachine(getMBean('/Machines/wls2_nm'))
cmo.setCluster(getMBean('/Clusters/Cluster1'))

activate()
startEdit()

cd('/Servers/ManagedServer3/WebServer/ManagedServer3/WebServerLog/ManagedServer3')
cmo.setLoggingEnabled(false)

activate()


#====================================================


startEdit()

cd('/Clusters/Cluster1')
cmo.setMigrationBasis('consensus')
set('CandidateMachinesForMigratableServers',jarray.array([ObjectName('com.bea:Name=wls1_nm,Type=Machine'), ObjectName('com.bea:Name=wls2_nm,Type=Machine')], ObjectName))

activate()

startEdit()

cd('/Servers/ManagedServer3')
cmo.setAutoMigrationEnabled(true)
set('CandidateMachines',jarray.array([], ObjectName))

cd('/MigratableTargets/ManagedServer3 (移行可能)')
set('ConstrainedCandidateServers',jarray.array([], ObjectName))

cd('/Servers/ManagedServer3/JTAMigratableTarget/ManagedServer3')
set('ConstrainedCandidateServers',jarray.array([], ObjectName))
cmo.setMigrationPolicy('manual')

activate()

startEdit()

cd('/Servers/ManagedServer3')
set('CandidateMachines',jarray.array([ObjectName('com.bea:Name=wls1_nm,Type=Machine'), ObjectName('com.bea:Name=wls2_nm,Type=Machine')], ObjectName))

cd('/MigratableTargets/ManagedServer3 (移行可能)')
set('ConstrainedCandidateServers',jarray.array([], ObjectName))

cd('/Servers/ManagedServer3/JTAMigratableTarget/ManagedServer3')
set('ConstrainedCandidateServers',jarray.array([], ObjectName))
cmo.setMigrationPolicy('manual')

activate()

startEdit()

cd('/Servers/ManagedServer3')
set('CandidateMachines',jarray.array([ObjectName('com.bea:Name=wls1_nm,Type=Machine'), ObjectName('com.bea:Name=wls2_nm,Type=Machine')], ObjectName))

cd('/MigratableTargets/ManagedServer3 (移行可能)')
set('ConstrainedCandidateServers',jarray.array([], ObjectName))

cd('/Servers/ManagedServer3/JTAMigratableTarget/ManagedServer3')
set('ConstrainedCandidateServers',jarray.array([], ObjectName))
cmo.setMigrationPolicy('failure-recovery')

activate()

startEdit()

cd('/Servers/ManagedServer3')
set('CandidateMachines',jarray.array([ObjectName('com.bea:Name=wls1_nm,Type=Machine'), ObjectName('com.bea:Name=wls2_nm,Type=Machine')], ObjectName))

cd('/MigratableTargets/ManagedServer3 (移行可能)')
set('ConstrainedCandidateServers',jarray.array([], ObjectName))

cd('/Servers/ManagedServer3/JTAMigratableTarget/ManagedServer3')
set('ConstrainedCandidateServers',jarray.array([ObjectName('com.bea:Name=ManagedServer3,Type=Server'), ObjectName('com.bea:Name=ManagedServer4,Type=Server')], ObjectName))
cmo.setMigrationPolicy('failure-recovery')

activate()

startEdit()

cd('/Servers/ManagedServer4')
cmo.setAutoMigrationEnabled(true)
set('CandidateMachines',jarray.array([ObjectName('com.bea:Name=wls1_nm,Type=Machine'), ObjectName('com.bea:Name=wls2_nm,Type=Machine')], ObjectName))

cd('/MigratableTargets/ManagedServer4 (移行可能)')
set('ConstrainedCandidateServers',jarray.array([], ObjectName))

cd('/Servers/ManagedServer4/JTAMigratableTarget/ManagedServer4')
set('ConstrainedCandidateServers',jarray.array([ObjectName('com.bea:Name=ManagedServer3,Type=Server'), ObjectName('com.bea:Name=ManagedServer4,Type=Server')], ObjectName))
cmo.setMigrationPolicy('failure-recovery')

activate()

startEdit()

