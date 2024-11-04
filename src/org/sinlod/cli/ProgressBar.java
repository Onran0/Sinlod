package org.sinlod.cli;

public final class ProgressBar {
	private final int size;
	private int percent;
	private char fill;
	private char empty = ' ';
	private boolean hasDraw = false;
	private boolean finished = false;
	
	public void finish() {
		this.finished = true;
	}
	
	private void checkFinished() {
		if(finished)
			throw new IllegalStateException("console task is finished");
	}
	
	public ProgressBar() {
		this(50);
	}
	
	public ProgressBar(int size) {
		this(size, '#');
	}
	
	public ProgressBar(int size, char fill) {
		this(size, fill, ' ');
	}
	
	public ProgressBar(int size, char fill, char empty) {
		if(size < 0)
			throw new IllegalArgumentException("size less than zero");
		
		this.size = size;
		
		this.setFill(fill);
		this.setEmpty(empty);
	}
	
	public void redraw() {
		
		this.checkFinished();
		
		if(hasDraw) {
			this.clear();
			System.out.print("\r");
		}
		
		this.draw();
	}
	
	public void draw() {
		
		this.checkFinished();
		
		int fillCount = (int) (size * (percent / 100.0F));
		
		System.out.print("[");
		
		for(int i = 0;i < size;i++) {
			System.out.print(i <= fillCount ? fill : empty);
		}
		
		System.out.print("] ");
		if(percent >= 10 && percent < 100) {
			System.out.print(" ");
		} else if(percent < 10) {
			System.out.print("  ");
		}
		System.out.print(percent);
		System.out.print('%');
		
		hasDraw = true;
	}
	
	public void clear() {
		
		this.checkFinished();
		
		if(hasDraw) {
			System.out.print('\r');
			for(int i = 0;i < getFullSize();i++) {
				System.out.print(' ');
			}
		}
	}
	
	public int getFullSize() {
		return size + 7;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void setEmpty(char empty) {
		this.empty = empty;
	}
	
	public char getEmpty() {
		return this.empty;
	}
	
	public void setFill(char fill) {
		this.fill = fill;
	}
	
	public char getFill() {
		return fill;
	}
	
	public int getPercent() {
		return this.percent;
	}
	
	public void setPercent(int percent) {
		if(percent > 999 || percent < 0)
			throw new IllegalArgumentException("percent " + percent + " higher than 999 or less than zero");
		
		this.percent = percent;
	}
}