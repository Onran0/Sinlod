package org.sinlod.util;

import java.io.*;
import java.net.*;

public final class Download {
	
	public static void download(String fileUrl, File destination) throws IOException {
		download(fileUrl, destination, null);
	}
	
	public static void download(String fileUrl, File destination, IDownloadListener listener) throws IOException {
		URL url = new URL(fileUrl);
		URLConnection connection = url.openConnection();
		connection.setAllowUserInteraction(true);
		connection.setDoInput(true);
		connection.setDoOutput(false);
		connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
		connection.connect();
		
		BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destination));
		int downloaded = 0;
		int downloadedKbytes = 0;
		short kbCounter = 0;
		int len;
		boolean listenerIsNotNull = listener != null;
		while((len = in.read()) != -1) {
			out.write(len);
			
			downloaded++;
			
			if(listenerIsNotNull) {
				kbCounter++;
				
				if(kbCounter == 1000) {
					kbCounter = 0;
					downloadedKbytes++;
					listener.onKByteDownloaded(downloaded, downloadedKbytes);
				}
			}
		}
		in.close();
		out.flush();
		out.close();
	}
}