# mysqldump --user=root --password=1 tm > tm.sql.

# mysqldump --user=root --password=1 tm2 | pv | gzip >  tm2-$(date +%Y%m%d).sql.gz


mysqldump --user=root --password=1 sitrol_mcc > sitrol_mcc-$(date +%Y-%m-%d).sql



