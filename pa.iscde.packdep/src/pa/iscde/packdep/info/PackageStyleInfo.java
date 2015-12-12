package pa.iscde.packdep.info;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.widgets.GraphNode;

public class PackageStyleInfo {

	String name;
	Color color;
	Color textColor;
	Color highlightColor;
	Color borderColor;
	Color borderHighlightColor;
	PackageSize size;
	Font textFont;
	Color backgroundColor;
	Image icon;
	GraphNode node;
	
	public PackageStyleInfo(String name, Color color, Color textColor, Color highlightColor, Color borderColor, Color borderHighlightColor,
			PackageSize size, Font textFont, Color backgroundColor, Image icon) {
		this.name = name;
		//this.node = node;
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
	
	public String getName(){
		return name;
	}

	public GraphNode getNode() {
		return node;
	}

	public Color getColor() {
		return color;
	}

	public Color getTextColor() {
		return textColor;
	}

	public Color getHighlightColor() {
		return highlightColor;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public Color getBorderHighlightColor() {
		return borderHighlightColor;
	}

	public PackageSize getSize() {
		return size;
	}

	public Font getTextFont() {
		return textFont;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public Image getIcon() {
		return icon;
	}
	
	
}
