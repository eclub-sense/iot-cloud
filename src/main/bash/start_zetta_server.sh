#!/bin/bash
# Starts zetta-server for new user.

mkdir zetta-servers/"$1"
cp -r resources/generic-zetta-server/. zetta-servers/"$1"
sed -i -e 's/>>PORT<</'$2'/g' zetta-servers/"$1"/server.js
sed -i -e 's/>>SERVER_NAME<</'$1'/g' zetta-servers/"$1"/server.js
cd zetta-servers/"$1"
./node server.js
