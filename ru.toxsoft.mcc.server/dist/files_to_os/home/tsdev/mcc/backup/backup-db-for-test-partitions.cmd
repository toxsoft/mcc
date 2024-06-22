rem mysqldump --user=root --password=1 tm > tm.sql.
rem mysqldump --user=root --password=1 tm2 | pv | gzip >  tm2-$(date +%Y%m%d).sql.gz

SET DB=mcc

mysqldump --user=root --password=1 ^
%DB% > mcc_2023-10-08_for_partition_tests.sql



