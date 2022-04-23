import os
import time
import sys
import secrets
import random as rand

r = secrets.SystemRandom(os.urandom(8))
# Simulating Data Stream
while True:
    print(str(r.uniform(20, 70)))

    sys.stdout.flush()
    time.sleep(0.3)
