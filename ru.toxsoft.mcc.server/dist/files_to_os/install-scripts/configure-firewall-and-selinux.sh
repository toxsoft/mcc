##############################################################################################################
# Define interface firewall zones
#

# for example 
# sudo firewall-cmd --zone=internal --change-interface=ens785f0 --permanent
# sudo firewall-cmd --reload
# sudo firewall-cmd --get-active-zones

##############################################################################################################
#
# firewall. open ports
#

# source: http://galeracluster.com/documentation-webpages/firewallsettings.html
# 3306 For MySQL client connections and State Snapshot Transfer that use the mysqldump method.
# mvk 2019-03-16: пока неопределена необходимость порта для galera 
# sudo firewall-cmd --zone=internal --add-port=3306/tcp --permanent

# Galera: 4567 For Galera Cluster replication traffic, multicast replication uses both UDP transport and TCP on this port.
sudo firewall-cmd --zone=public --add-port=4567/tcp --permanent

# Galera: 4568 For Incremental State Transfer.
sudo firewall-cmd --zone=public --add-port=4568/tcp --permanent

# Galera: 4444 For all other State Snapshot Transfer.
sudo firewall-cmd --zone=public --add-port=4444/tcp --permanent

# Galera: 4567 For Galera Cluster replication traffic, multicast replication uses both UDP transport and TCP on this port.
sudo firewall-cmd --zone=public --add-port=4567/udp --permanent

# Wildfly: 8080 For Wildfly EJB invocations and JMS messaging.
sudo firewall-cmd --zone=public --add-port=8080/tcp --permanent

# Wildfly: 7600 For Wildfly Cluster replication traffic, multicast replication uses TCP on this port.
sudo firewall-cmd --zone=public --add-port=7600/tcp --permanent

sudo firewall-cmd --reload


##############################################################################################################
# Enabling galera an SELinux Policy (http://galeracluster.com/documentation-webpages/selinux.html).
#

# for create selinux policy (https://www.centos.org/forums/viewtopic.php?t=5012)
sudo yum install policycoreutils-python


# This creates a galera.te file in your working directory.
sudo fgrep "mysqld" /var/log/audit/audit.log | audit2allow -m MySQL_galera -o galera.te

# This creates a galera.mod file in your working directory.
sudo checkmodule -M -m galera.te -o MySQL_galera.mod

# This creates a galera.pp file in your working directory.
sudo semodule_package -m MySQL_galera.mod -o galera.pp

# Load the package into SELinux.
sudo semodule -i galera.pp

# Disable permissive mode for the database server.
# mvk 2019-03-16: неэффективно в текущей версии (CentOS 7.5) 
# sudo semanage permissive -d mysql_t


