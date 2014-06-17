package com.aga.mine.pumpkin;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

	class MessageWriter {
		byte[] data_;

		public MessageWriter() {
			this.data_ = new byte[0];
		}

		public byte[] appendBytes(byte[] b) {
			byte[] a = this.data_;
			byte[] result = new byte[a.length + b.length];
			System.arraycopy(a, 0, result, 0, a.length);
			System.arraycopy(b, 0, result, a.length, b.length);
			this.data_ = new byte[result.length];
			return result;
		}

		public void writeByte(byte value) {
			byte[] values = new byte[1];
			values[0] = value;

			this.data_ = this.appendBytes(values);
		}

		public void writeInt(int value) {
			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.putInt(value);
			bb.order(ByteOrder.nativeOrder());

			this.data_ = this.appendBytes(bb.array());
		}

		public void writeString(String value) throws UnsupportedEncodingException {
			byte[] utf8Value = value.getBytes("UTF-8");
			int length = utf8Value.length;

			this.writeInt(length);
			this.data_ = this.appendBytes(utf8Value);
		}

		public void writeBytes(byte[] bytes) {
			this.data_ = this.appendBytes(bytes);
		}
	}
	
