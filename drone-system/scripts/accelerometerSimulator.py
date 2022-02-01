import time
import sys
import random as rand

#Simulating Data Stream
while True:
    print("{\"accelerometer\": {"
    "\n        \"x\": " + str(rand.random()) +
    ",\n        \"y\": " + str(rand.random()) +
    ",\n        \"z\": " + str(rand.random()) +
    "\n    }"
    "\n}")
    sys.stdout.flush()
    time.sleep(0.3)
