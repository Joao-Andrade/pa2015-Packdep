package pa.iscde.packdep.info;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.widgets.GraphNode;

//style of each package
public class PackageStyleInfo {

	/**
	 * name of the package
	 */
	String name;

	/**
	 * color
	 */
	Color color;
	
	/**
	 * text color
	 */
	Color textColor;

	/**
	 * package color when highlighted
	 */
	Color highlightColor;
	/**
	 * border color
	 */
	Color borderColor;
	
	/**
	 * border color when the package is highlighted
	 */
	Color borderHighlightColor;
	
	/**
	 * node (package) size
	 */
	PackageSize size;
	
	/**
	 * text font
	 */
	Font textFont;
	
	/**
	 * view's background color
	 */
	Color backgroundColor;
	
	/**
	 * icon
	 */
	Image icon;
	
	//constructor.
	public PackageStyleInfo(String name, Color color, Color textColor, Color highlightColor, Color borderColor, Color borderHighlightColor,
			PackageSize size, Font textFont, Color backgroundColor, Image icon) {
		this.name = name;
		this.color = color;
		this.textColor = textColor;
		this.highlightColor = highlightColor;
		this.borderColor = borderColor;
		this.borderHighlightColor = borderHighlightColor;
		this.size = size;
		this.textFont = textFont;
		this.backgroundColor = backgroundColor;
		this.icon = icon;
	}
	/**
	 * @return name of the package
	 */
	public String getName(){
		return name;
	}
	/**
	 * @return color
	 */
	public Color getColor() {
		return color;
	}
	/**
	 * @return text color
	 */
	public Color getTextColor() {
		return textColor;
	}
	/**
	 * @return package color when highlighted
	 */
	public Color getHighlightColor() {
		return highlightColor;
	}
	/**
	 * @return border color
	 */
	public Color getBorderColor() {
		return borderColor;
	}
	/**
	 * @return border color when the package is highlighted
	 */
	public Color getBorderHighlightColor() {
		return borderHighlightColor;
	}
	/**
	 * @return node (package) size
	 */
	public PackageSize getSize() {
		return size;
	}
	/**
	 * @return text font
	 */
	public Font getTextFont() {
		return textFont;
	}
	/**
	 * @return view's background color
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	/**
	 * @return icon
	 */
	public Image getIcon() {
		return icon;
	}
	
	
}
