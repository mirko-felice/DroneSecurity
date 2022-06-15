import time

forward = [x * 1.5 for x in range(48, 12, -1)]
backward = [x * 1.5 for x in range(12, 48, 1)]
# Simulating Data Stream
index = 0
while True:
    for i in forward:
        print("{\"proximity\": " + str(i) +
              ",\n\"timestamp\": " + str(int(time.time() * 1000)) +
              ",\n\"index\": " + str(index) +
              "\n}", flush=True)
        index = index + 1
        time.sleep(0.033)
    for i in backward:
        print("{\"proximity\": " + str(i) +
              ",\n\"timestamp\": " + str(int(time.time() * 1000)) +
              ",\n\"index\": " + str(index) +
              "\n}", flush=True)
        index = index + 1
        time.sleep(0.033)
