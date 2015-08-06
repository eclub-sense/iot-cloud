#!/bin/bash

node server.js &
server_pid=$!

sleep 30
kill -USR2 $server_pid
start_mem=`ps -o rss -p $server_pid | sed -n '1!p'`

voltron test load -t $1 -i $2 -a 0 -s $3 http://localhost:3000 > /dev/null 2> /dev/null
sleep 30

kill -USR2 $server_pid
end_mem=`ps -o rss -p $server_pid | sed -n '1!p'`

sleep 5

echo $start_mem $end_mem $(expr $end_mem - $start_mem)
kill $server_pid
