import smbus2
import time
import sys

# Constant definitions
RANGE_MASK = 0b00011000
SCALE_FACTOR = [16384.0, 8192.0, 4096.0, 2048.0]
SENSOR_ADDRESS = 0x68

# Accelerometer data addresses
ACCEL_ADR = {
    "x": 0x3b,
    "y": 0x3d,
    "z": 0x3f
}

# Gyroscope data addresses
GYRE_ADR = {
    "x": 0x43,
    "y": 0x45,
    "z": 0x47
}

# Temperature data address
TEMP_ADR = 0x41
# Power management address
POWER_MGMT_1 = 0x6b


# Read a byte from the sensor
def read_byte(adr):
    return bus.read_byte_data(SENSOR_ADDRESS, adr)


# Read 2 bytes from the sensor
def read_word(adr):
    high = read_byte(adr)
    low = read_byte(adr + 1)
    val = (high << 8) + low
    return val


# Convert 2 bytes read from the sensor to a signed int using
# two complement algorithm
def read_word_2_comp(adr):
    val = read_word(adr)
    if val >= 0x8000:
        return -((0xffff - val) + 1)
    else:
        return val


# Read temperature value
def read_temp():
    val = read_word_2_comp(TEMP_ADR)
    temp = (val / 340.0) + 36.53
    return temp


# Read accelerometer configured range
def read_accel_range():
    val = read_byte(0x1c)
    afs_sel = val & RANGE_MASK
    return afs_sel >> 3


# Read accelerometer value and scales it dependently on its configuration
def read_scaled_val(adr):
    accel_range = read_accel_range()
    scale = SCALE_FACTOR[accel_range]
    return read_word_2_comp(adr) / scale


with smbus2.SMBus(1) as bus:
    # Read sensor data indefinitely
    i = 0
    while True:
        bus.write_byte_data(SENSOR_ADDRESS, POWER_MGMT_1, 0)

        accelX = read_scaled_val(ACCEL_ADR["x"])
        accelY = read_scaled_val(ACCEL_ADR["y"])
        accelZ = read_scaled_val(ACCEL_ADR["z"])

        print("{\"accelerometer\": {"
              "\n        \"x\": " + str(accelX) +
              ",\n        \"y\": " + str(accelY) +
              ",\n        \"z\": " + str(accelZ) +
              "\n    },"
              "\n\"timestamp\": " + str(int(time.time() * 1000)) +
              ",\n\"index\": " + str(i) +
              "\n}", flush=True)

        i = i + 1

        time.sleep(0.033)
