import wiringpi as wp
import time
import sys


# Waits for pin to go to a specified level or until timeout is reached.
def wait_pin_level(pin, level, timeout):
    done = False
    start = time.time_ns() / 1000

    micros = 0.0
    while not done:
        now = time.time_ns() / 1000
        micros = now - start
        if wp.digitalRead(pin) == level or micros > timeout:
            done = True
    return micros


# Waits a determined amount of microseconds.
def delay_micros(micros):
    start = time.time_ns()
    elapsed_time = 0
    while elapsed_time < ((micros - 1) * 1000):
        elapsed_time = time.time_ns() - start
    return elapsed_time


# wiringPi numbering
pin_echo = 2
pin_trigger = 0

# Setup
wp.wiringPiSetup()
wp.pinMode(pin_echo, wp.INPUT)
wp.pinMode(pin_trigger, wp.OUTPUT)
wp.digitalWrite(pin_trigger, wp.LOW)
time.sleep(0.5)

i = 0
while True:
    # Sensor activation
    wp.digitalWrite(pin_trigger, wp.HIGH)
    elapsed = delay_micros(10)
    wp.digitalWrite(pin_trigger, wp.LOW)

    # Sensor reading
    wait_pin_level(pin_echo, wp.HIGH, 5000)
    if wp.digitalRead(pin_echo) == wp.HIGH:
        # Waiting for the signal to return
        elapsed = wait_pin_level(pin_echo, wp.LOW, 60000)
        if wp.digitalRead(pin_echo) == wp.LOW:

            # Computing distance
            distance = 0.034 * elapsed / 2.0
            if elapsed < 38000:
                print("{\"proximity\": " + str(distance) +
                      ",\n\"timestamp\": " + str(int(time.time() * 1000)) +
                      ",\n\"index\": " + str(i) +
                      "\n}", flush=True)
                i = i + 1

    time.sleep(0.028)
