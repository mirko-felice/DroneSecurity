import time
import sys

# Simulating Data Stream

i = 0
while True:
    print("{\"accelerometer\": {"
          "\n        \"x\": " + str(0) +
          ",\n        \"y\": " + str(1) +
          ",\n        \"z\": " + str(0) +
          "\n    }"
          ",\n\"timestamp\": " + str(int(time.time() * 1000)) +
          ",\n\"index\": " + str(i) +
          "\n}", flush=True)

    i = i + 1

    time.sleep(0.033)
