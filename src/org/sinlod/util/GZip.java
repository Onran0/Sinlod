package org.sinlod.util;

import java.io.*;
import java.util.zip.*;

public final class GZip {
	public static OutputStream getCompressedOut(OutputStream out) throws IOException {
		return new GZIPOutputStream(out);
	}
	
	public static InputStream getDecompressedIn(InputStream in) throws IOException {
		return new GZIPInputStream(in);
	}
}