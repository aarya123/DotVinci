package pixelator;

class Pixel{

	private double x, y, red, green, blue;

	public Pixel(double x, double y, double red, double green, double blue) {
		super();
		this.x = x;
		this.y = y;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public double getRed() {
		return red;
	}

	public void setRed(double red) {
		this.red = red;
	}

	public double getGreen() {
		return green;
	}

	public void setGreen(double green) {
		this.green = green;
	}

	public double getBlue() {
		return blue;
	}

	public void setBlue(double blue) {
		this.blue = blue;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}	
}