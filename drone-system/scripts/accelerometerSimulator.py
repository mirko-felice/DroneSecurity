import time
import sys

#Simulating Data Stream
while True:
    print("{\"accelerometer\": {"
    "\n        \"x\": " + str(1.2) +
    ",\n        \"y\": " + str(0) +
    ",\n        \"z\": " + str(1) +
    "\n    }"
    "\n}")
    sys.stdout.flush()
    time.sleep(1)
