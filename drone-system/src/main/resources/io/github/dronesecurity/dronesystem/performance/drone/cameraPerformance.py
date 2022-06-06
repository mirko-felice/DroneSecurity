import io
import socket
import struct
import time
import picamera

# Creates a Server socket to localhost:10000 and awaits the connection
server_socket = socket.socket()
server_socket.bind(('0.0.0.0', 10000))
server_socket.listen(0)

# Make a file-like object out of the connection
connection = server_socket.accept()[0].makefile('wb')
try:
    camera = picamera.PiCamera()
    camera.resolution = (640, 480)
    camera.framerate = 30

    stream = io.BytesIO()
    i = 0
    for foo in camera.capture_continuous(stream, 'jpeg', use_video_port=True):
        # Write the length of the capture to the stream and flush to
        # ensure it actually gets sent
        connection.write(struct.pack('<L', stream.tell()))
        connection.flush()

        print("{\"timestamp\": " + str(int(time.time() * 1000)) +
              ",\n\"index\": " + str(i) +
              "\n}", flush=True)

        i = i + 1
        if i >= 100:
            i = 0
        # Rewind the stream and send the image data over the wire
        stream.seek(0)
        connection.write(stream.read())

        # Reset the stream for the next capture
        stream.seek(0)
        stream.truncate()
    # Write a length of zero to the stream to signal we're done
    connection.write(struct.pack('<L', 0))
finally:
    connection.close()
    server_socket.close()
