import time
import sys
import random as rand

# Simulating Data Stream
while True:
    print("{\"accelerometer\": {"
          "\n        \"x\": " + str(1) +
          ",\n        \"y\": " + str(1) +
          ",\n        \"z\": " + str(1) +
          "\n    }"
          "\n}")
    sys.stdout.flush()
    time.sleep(0.3)
