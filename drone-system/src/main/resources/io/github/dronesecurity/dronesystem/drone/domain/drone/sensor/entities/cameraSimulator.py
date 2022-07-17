import io
import socket
import struct

# Creates a Server socket to localhost:10000 and awaits the connection
server_socket = socket.socket()
server_socket.bind(('0.0.0.0', 10000))
server_socket.listen(0)

# Make a file-like object out of the connection
connection = server_socket.accept()[0].makefile('wb')
try:
    length = 4
    stream = io.BytesIO(b"abcd")
    while True:
        # Write the length of the capture to the stream and flush to
        # ensure it actually gets sent
        connection.write(struct.pack('<L', length))
        connection.flush()
        # Rewind the stream and send the image data over the wire
        connection.write(stream.read())
        time.sleep(0.1)

finally:
    connection.close()
    server_socket.close()
