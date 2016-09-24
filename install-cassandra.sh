#!/usr/bin/env bash

sudo service cassandra stop

echo "deb http://www.apache.org/dist/cassandra/debian 37x main" | sudo tee -a /etc/apt/sources.list.d/cassandra.sources.list
echo "deb-src http://www.apache.org/dist/cassandra/debian 37x main" | sudo tee -a /etc/apt/sources.list.d/cassandra.sources.list

gpg --keyserver pgp.mit.edu --recv-keys F758CE318D77295D
gpg --export --armor F758CE318D77295D | sudo apt-key add -

gpg --keyserver pgp.mit.edu --recv-keys 2B5C1B00
gpg --export --armor 2B5C1B00 | sudo apt-key add -

gpg --keyserver pgp.mit.edu --recv-keys 0353B12C
gpg --export --armor 0353B12C | sudo apt-key add -

sudo apt-get update
sudo apt-get install cassandra

sudo sed -i 's/#MAX_HEAP_SIZE="4G"/MAX_HEAP_SIZE="500M"/' /etc/cassandra/cassandra-env.sh
sudo sed -i 's/#HEAP_NEWSIZE="800M"/HEAP_NEWSIZE="100M"/' /etc/cassandra/cassandra-env.sh

sudo service cassandra restart

curl -sSL https://s3.amazonaws.com/circle-downloads/wait-for-cassandra.sh | sh
/usr/bin/nodetool enablethrift
/usr/bin/nodetool enablegossip
