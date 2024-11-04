package org.sinlod.cli;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import org.sinlod.*;
import org.sinlod.util.*;

public final class CLI {

	public static void main(String[] args) {
		try {
			start(args);
		} catch(Throwable t) {
			System.err.println("!-- Error: " + t.toString() + " --!");
			System.exit(1);
			return;
		}
		scanner.close();
		System.exit(0);
	}
	
	private static Scanner scanner;
	
	private static String readLine() {
		if(scanner == null) {
			scanner = new Scanner(System.in, "UTF-8");
		}
		return scanner.nextLine();
	}
	
	private static void start(String[] args) throws Throwable {
		SevenZip.init();
		System.out.println("--- Sinlod CLI v1.0 ---");
		SldDownloader downloader = new SldDownloader();
		SldCreator creator = new SldCreator();
		File sldf;
		File dest;
		if(args.length > 0) {
			sldf = new File(args[0]);
			if(sldf.exists()) {
				if(args.length > 1) {
					dest = new File(args[1]);
				} else {
					System.out.println("-?- Enter destination directory -?- ");
					System.out.print("> ");
					String l = readLine();
					dest = l.contains(":") ? new File(l) : new File(sldf.getParent(), l);
				}
				
				SldReader reader = new SldReader(GZip.getDecompressedIn(new FileInputStream(sldf)));
				SldFile sld = reader.read();
				reader.close();
				try {
					downloader.download(sld, Files.createTempDirectory("sldcli"), dest.toPath());
				} catch(Exception e) {
					System.err.println("!-- Error download .sld file content: " + e.toString() + " --!");
				}
			} else {
				System.out.println("!-- File " + sldf.getAbsolutePath() + " not exists --!");
			}
		} else {
			System.out.println("d - Download content from .sld file");
			System.out.println("c - Create .sld file");
			while(true) {
				System.out.print("> ");
				String cmd = readLine();
				if(cmd.startsWith("d")) {
					while(true) {
						System.out.println("-?- Enter .sld file -?-");
						System.out.print("> ");
						sldf = new File(readLine());
						if(sldf.exists()) {
							break;
						} else {
							System.out.println("!-- File " + sldf.getAbsolutePath() + " not exists --!");
						}
					}
					System.out.println("-?- Enter destination folder -?-");
					System.out.print("> ");
					dest = new File(readLine());
					
					SldReader reader = new SldReader(GZip.getDecompressedIn(new FileInputStream(sldf)));
					SldFile sld = reader.read();
					reader.close();
					try {
						downloader.download(sld, Files.createTempDirectory("sldcli"), dest.toPath());
					} catch(Exception e) {
						System.err.println("!-- Error download .sld file content: " + e.toString() + " --!");
					}
				} else if(cmd.startsWith("c")) {
					System.out.println("-?- Enter .sld file -?-");
					System.out.print("> ");
					sldf = new File(readLine());
					System.out.println("-?- Enter refs length -?-");
					System.out.print("> ");
					int refsCount = Integer.parseInt(readLine());
					String[] refs = new String[refsCount];
					int[] refsLength = new int[refsCount];
					System.out.println("-?- Enter refs in format ref:&size (in bytes) -?-");
					String[] ref;
					for(int i = 0;i < refsCount;i++) {
						System.out.print("> ");
						ref = readLine().split(":&");
						refs[i] = ref[0];
						refsLength[i] = Integer.parseInt(ref[1]);
						System.out.println();
					}
					System.out.println("-?- Enter unpack method -?-");
					SldMethods.SldMethod[] unpackMethods = SldMethods.unpackMethods();
					for(int i = 0;i < unpackMethods.length;i++) {
						System.out.println("--- " + unpackMethods[i].name + " (" + (i + 1) + ")");
					}
					System.out.print("> ");
					int id = Integer.parseInt(readLine()) - 1;
					short unpackMethod = unpackMethods[id].id;
					SldFile sld = new SldFile(refs,refsLength,unpackMethod);
					try {
						creator.create(sld, sldf);
					} catch(Exception e) {
						System.err.println("!-- Error create .sld file: " + e.toString() + " --!");
					}
				} else {
					System.out.println("!-- Unknown command --!");
				}
			}
		}
	}
}