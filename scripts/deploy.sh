#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/sweater-1.0-SNAPSHOT.jar \
    sv@192.168.100.4:/home/sv/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa sv@192.168.100.4 << EOF
pgrep java | xargs kill -9
nohup java -jar sweater-1.0-SNAPSHOT.jar > log.txt &
EOF

echo 'Bye'