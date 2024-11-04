package org.sinlod;

import java.io.*;

public class SldWriter implements Closeable {
	private DataOutputStream out;
	
	public SldWriter(OutputStream out) {
		this.out = new DataOutputStream(out);
	}
	
	public void write(SldFile sld) throws IOException {
		out.write((int)(sld.version & 0xff));
		out.writeInt(sld.refs.length);
		for(int i = 0;i < sld.refs.length;i++) {
			out.writeUTF(sld.refs[i]);
		}
		out.writeInt(sld.refsLength.length);
		for(int i = 0;i < sld.refsLength.length;i++) {
			out.writeInt(sld.refsLength[i]);
		}
		out.writeShort(sld.loadMethod);
		out.writeShort(sld.unpackMethod);
	}
	
	public void close() throws IOException {
		out.close();
	}
}