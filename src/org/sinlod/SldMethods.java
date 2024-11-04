package org.sinlod;

public class SldMethods {
	public static final byte SIMPLE_VERSION = 1;
	public static final short DOWNLOAD_METHOD = 0;
	public static final short SEVENZIP_TOMS_METHOD = 1;
	public static final short SEVENZIP_SINGLE_METHOD = 2;
	public static final short ZIP_TOMS_METHOD = 3;
	public static final short ZIP_SINGLE_METHOD = 4;
	
	public static class SldMethod
	{
		public String name;
		public short id;
		
		public SldMethod(String name,short id) {
			this.name = name;
			this.id = id;
		}
	}
	
	public static SldMethod[] loadMethods() {
		return new SldMethod[] {
				new SldMethod("DOWNLOAD_METHOD",(short)0),
		};
	}
	
	public static SldMethod[] unpackMethods() {
		return new SldMethod[] {
			new SldMethod("7ZIP_TOMS_METHOD",(short)1),
			new SldMethod("7ZIP_SINGLE_METHOD",(short)2),
			new SldMethod("ZIP_TOMS_METHOD",(short)3),
			new SldMethod("ZIP_SINGLE_METHOD",(short)4)
		};
	}
}