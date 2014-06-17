package com.aga.mine.pumpkin;

import java.nio.ByteBuffer;

public class MessageReader {
		ByteBuffer buffer_;

		public MessageReader(byte[] data) {
			this.buffer_ = ByteBuffer.wrap(data);
		}

		public byte readByte() {
			return this.buffer_.get();
		}

		public int readInt() {
			return this.buffer_.getInt();
		}

		public String readString() {
			int stringLength = this.readInt();
			byte[] read = new byte[stringLength];
			this.buffer_.get(read);
			String result = new String(read);
			return result;
		}
		
		public int length() {
			return this.buffer_.remaining();
		}
	}
	

