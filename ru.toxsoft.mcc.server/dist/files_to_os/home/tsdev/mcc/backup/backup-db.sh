# mysqldump --user=root --password=1 tm > tm.sql.

# mysqldump --user=root --password=1 tm2 | pv | gzip >  tm2-$(date +%Y%m%d).sql.gz


mysqldump --user=root --password=1 mcc > mcc-$(date +%Y-%m-%d).sql



