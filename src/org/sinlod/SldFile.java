package org.sinlod;

public class SldFile {
	public byte version;
	public String[] refs; 
	public int[] refsLength;
	public short loadMethod;
	public short unpackMethod;
	
	public SldFile(String[] refs,int[] refsLength,short unpackMethod) {
		this(SldMethods.SIMPLE_VERSION,refs,refsLength,SldMethods.DOWNLOAD_METHOD,unpackMethod);
	}
	
	public SldFile(byte version,String[] refs,int[] refsLength,short loadMethod,short unpackMethod) {
		this.version = version;
		this.refs = refs;
		this.refsLength = refsLength;
		this.loadMethod = loadMethod;
		this.unpackMethod = unpackMethod;
	}
}