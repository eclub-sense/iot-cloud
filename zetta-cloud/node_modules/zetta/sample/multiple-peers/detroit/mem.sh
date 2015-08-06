#!/bin/bash

node detroit.js &
pid=$!
echo $pid
sleep 30;
mem_start=`ps -o rss $pid | tail -n -1`
sleep $1
mem_end=`ps -o rss $pid | tail -n -1`
kill $pid

echo $mem_start $mem_end
