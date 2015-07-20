i=0
while [ "$i" -lt 5 ]; do
	bash add_sensor.sh "$i" "THERMOMETER"
	echo registering uuid "$i"
	let i++
done
while [ "$i" -lt 10 ]; do
	bash add_sensor.sh "$i" "LED"
	echo registering uuid "$i"
	let i++
done
