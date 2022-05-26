import os
import sys

if sys.platform == "linux":
	print(os.uname().nodename)
	sys.stdout.flush()
else:
	print("not linux")
	sys.stdout.flush()
