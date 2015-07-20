i=0
while [ "$i" -lt 10 ]; do
	bash add_sensor.sh "$i"
	echo registering uuid "$i"
	let i++
done
