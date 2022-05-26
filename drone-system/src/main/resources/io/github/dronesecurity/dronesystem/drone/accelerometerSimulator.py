import time
import sys

# Simulating Data Stream
while True:
    print("{\"accelerometer\": {"
          "\n        \"x\": " + str(0) +
          ",\n        \"y\": " + str(1) +
          ",\n        \"z\": " + str(0) +
          "\n    }"
          "\n}")
    sys.stdout.flush()
    time.sleep(0.3)
