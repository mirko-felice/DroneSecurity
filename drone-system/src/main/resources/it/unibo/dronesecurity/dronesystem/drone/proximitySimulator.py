import time

forward = [x * 1.5 for x in range(48, 12, -1)]
backward = [x * 1.5 for x in range(12, 48, 1)]
# Simulating Data Stream
while True:
    for i in forward:
        print(i, flush=True)
        time.sleep(0.3)
    for i in backward:
        print(i, flush=True)
        time.sleep(0.3)
