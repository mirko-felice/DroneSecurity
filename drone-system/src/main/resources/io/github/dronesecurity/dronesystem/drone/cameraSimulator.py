import time
import sys
import random as rand

# Simulating Data Stream
while True:
    print(str(rand.random()))

    sys.stdout.flush()
    time.sleep(0.3)
