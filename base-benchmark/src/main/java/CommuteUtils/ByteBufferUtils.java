package CommuteUtils;

import java.nio.ByteBuffer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ByteBufferUtils {

	public static ByteBuffer allocateDirect(String str) {
		return _allocateByteBuffer(str.getBytes(UTF_8), true);
	}

	public static ByteBuffer allocate(String str) {
		return _allocateByteBuffer(str.getBytes(UTF_8), false);
	}

	public static ByteBuffer allocate(byte[] bytes) {
		return _allocateByteBuffer(bytes, false);
	}

	private static ByteBuffer _allocateByteBuffer(byte[] bytes, boolean isDirect) {
		final ByteBuffer buffer = isDirect ? //
				ByteBuffer.allocateDirect(bytes.length) : //
				ByteBuffer.allocate(bytes.length);

		buffer.put(bytes);
		buffer.flip();

		return buffer;
	}
}
