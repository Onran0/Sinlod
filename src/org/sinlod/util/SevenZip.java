package org.sinlod.util;

import java.io.*;

public final class SevenZip {
	
	public static boolean extract(File archive, File dest) {
	    Integer exit = execute("x", archive, "-o\"" + dest.getAbsolutePath() + "\"");
	    
	    if(exit == null) {
	    	System.err.println("-I- Internal exception occured while extracting archive(s) -I-");
	    	return false;
	    } else if(exit != 0) {
	    	System.err.print("-I- Failed to extract archives. Exit code: ");
	    	System.err.print(exit);
	    	System.err.println(" -I-");
	    	return false;
	    } else {
	    	System.out.println("-I- Archive(s) successfully extracted -I-");
	    	return true;
	    }
	}
	
	private static void initialize() throws IOException {
		String osName = System.getProperty("os.name").toLowerCase();
		
		String fileName =
		(
			osName.contains("win") ? "7z_win_x86.exe" :
			osName.contains("linux") || osName.contains("unix") ? "7z_linux_x86" :
			osName.contains("mac") || osName.contains("osx") ? "7z_macos_x86" : ""
		);
		
		if(fileName.isEmpty())
			throw new IOException("Unknown OS \"" + System.getProperty("os.name") + "\"");
		
		File dir = new File(System.getProperty("user.home"), "Sinlod");
		
		if(!dir.exists()) dir.mkdirs();
		
		File exef = new File(dir, fileName);
		
		exe = exef.getAbsolutePath();
		
		if(exef.exists())
			return;
		
		Download.download("https://raw.githubusercontent.com/Onran0/Sinlod/main/7z-bin/" + fileName, exef);
	}
	
	private static Integer execute(Object...commands) {
		try {
			int cmdlen = commands.length + 1;
			String[] cmd = new String[cmdlen];
			
			cmd[0] = exe;
			
			for(int i = 1;i < cmdlen;i++) {
				Object obj = commands[i - 1];
				
				if(obj != null && obj instanceof File) {
					cmd[i] = ((File) obj).getAbsolutePath();
				} else {
					cmd[i] = String.valueOf(obj);
				}
			}
			
			return Runtime.getRuntime().exec(cmd).waitFor();
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String exe;
	
	public static void init() {
		try {
			initialize();
		} catch(Exception e) {
			System.err.println("-*- Failed to initialize 7zip! -*-");
			e.printStackTrace();
			System.exit(1);
		}
	}
}