package pa.iscde.packdep.info;

//Graph node's size representing a package
public class PackageSize {

	private int width;
	private int height;
	
	public PackageSize(int width, int height){
		this.width = width;
		this.height = height;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
}
