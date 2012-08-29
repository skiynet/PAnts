SET PAGESIZE 100 COLSEP ',' NUMWIDTH 8 LINESIZE 132 VERIFY OFF FEEDBACK OFF

SELECT
  to_char(begin_time,'hh24:mi:ss') "start time",
  to_char(end_time,'hh24:mi:ss')   "end time",
  VALUE 
from v$sysmetric_history where metric_name='Response Time Per Txn';

SELECT
  to_char(begin_time,'hh24:mi:ss') "start time",
  to_char(end_time,'hh24:mi:ss')   "end time",
  VALUE 
from v$sysmetric_history where metric_name='SQL Service Response Time';
