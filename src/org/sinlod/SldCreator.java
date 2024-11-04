package org.sinlod;

import java.io.*;
import org.sinlod.util.GZip;

public class SldCreator {
	public void create(SldFile sld,File sldFile) throws IOException {
		System.out.println("-*- Creating Zip file out -*-");
		SldWriter writer = new SldWriter(GZip.getCompressedOut(new FileOutputStream(sldFile)));
		System.out.println("-*- Writing -*-");	
		writer.write(sld);
		System.out.println("-*- Closing -*-");
		writer.close();
	}
}