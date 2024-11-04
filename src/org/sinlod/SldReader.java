package org.sinlod;

import java.io.*;

public class SldReader implements Closeable {
	private DataInputStream in;
	
	public SldReader(InputStream out) {
		this.in = new DataInputStream(out);
	}
	
	public SldFile read() throws IOException {
		int version = in.read();
		if(version < 0) {
			return null;
		}
		String[] refs = new String[in.readInt()];
		for(int i = 0;i < refs.length;i++) {
			refs[i] = in.readUTF();
		}
		int[] refsLength = new int[in.readInt()];
		for(int i = 0;i < refsLength.length;i++) {
			refsLength[i] = in.readInt();
		}
		short loadMethod = in.readShort();
		short unpackMethod = in.readShort();
		return new SldFile((byte)version,refs,refsLength,loadMethod,unpackMethod);
	}
	
	public void close() throws IOException {
		in.close();
	}
}