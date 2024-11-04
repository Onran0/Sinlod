package org.sinlod;

import java.io.*;
import java.nio.file.*;

import org.sinlod.cli.ProgressBar;
import org.sinlod.util.*;

public class SldDownloader implements IDownloadListener {
	private String fileName;
	private int len;

	public void download(SldFile sld,Path temp,Path destination) throws IOException {
		File tmp = temp.toFile();
		File dest = destination.toFile();
		if(!dest.exists()) {
			dest.mkdirs();
		}
		File[] refs = new File[sld.refs.length];
		switch(sld.loadMethod) {
		case SldMethods.DOWNLOAD_METHOD:
			for(int i = 0;i < sld.refs.length;i++) {
				fileName = getFileName(sld.refs[i]);
				len = sld.refsLength[i];
				
				refs[i] = new File(tmp,fileName);
				
				if(task != null)
					task.finish();
				
				System.out.println("\nDownloading " + fileName + " ");
				
				task = new ProgressBar(35, '█', '▒');

				task.draw();

				try {
					Download.download(sld.refs[i], refs[i], this);
					
					System.out.println();
				} catch(IOException e) {
					task.clear();
					task.finish();
					
					throw e;
				}
			}
			break;
		}
		
		File ar = null;
		
		if(sld.unpackMethod == SldMethods.ZIP_SINGLE_METHOD || sld.unpackMethod == SldMethods.SEVENZIP_SINGLE_METHOD) {
			System.out.println("Using single archive method");
			ar = refs[0];
		} else if(sld.unpackMethod == SldMethods.ZIP_TOMS_METHOD || sld.unpackMethod == SldMethods.SEVENZIP_TOMS_METHOD) {
			System.out.println("Using split archive method");
			for(int i = 0;i < refs.length;i++) {
				if(refs[i].getName().endsWith(".001")) {
					ar = refs[i];
					break;
				}
			}
		}
		
		decompress(ar,sld,dest);
		
		System.out.println("Cleaning temp");
		
		deleteDir(tmp);
	}
	
	private void deleteDir(File dir) throws IOException {
		File[] files = dir.listFiles();
		for(int i = 0;i < files.length;i++) {
			if(files[i].isDirectory()) {
				deleteDir(files[i]);
			} else if(files[i].isFile()) {
				Files.delete(files[i].toPath());
			}
		}
		Files.delete(dir.toPath());
	}
	
	private void decompress(File ar,SldFile sld,File dest) throws IOException {
		System.out.println("Extracting " + ar.getAbsolutePath());
		SevenZip.extract(ar, dest);
	}
	
	private String getFileName(String url) {
		String r = url.contains("/") ? url.substring(url.lastIndexOf("/") + 1) : url.substring(url.lastIndexOf("\\") + 1);
		if(r.contains("?")) {
			r = r.substring(0,r.lastIndexOf("?"));
		}
		return r;
	}

	private ProgressBar task;
	private int oldPercent = -1;
	
	public void onKByteDownloaded(int downloaded, int downloadedKbytes) {
		int percent = (int) ((double)downloaded / (double)len * 100.0D);
		
		if(percent > 100)
			percent = 100;
		
		if(percent != oldPercent) {
			task.setPercent(percent);
			task.redraw();
		}
		oldPercent = percent;
	}
}