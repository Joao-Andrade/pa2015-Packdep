package pa.iscde.packdep.extensions;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.ZestStyles;

/**
 * Set up the graphical elements in the package dependencies view.
 * It is possible to change the following elements:
 * -packages boxes size.
 * -package box foreground and background color.
 * -package box highlighted size, background color and foreground color. 
 * -font.
 * -icon.
 * -background color.
 * 
 * Note: The user only sets the properties that it wants. 
 * If a property is not especified, meaning returns 0 or null, a default value is used.
 */
public interface packdepStyle {
	/**
	 * @return package box height.
	 */
	int packageSizeHeight();
	
	/**
	 * @return package box width.
	 */
	int packageSizeWidth();
	
	/**
	 * Color example:
	 * Color blue = new Color( Display.getDefault(), 0, 0, 255 );
	 * @return package box color.
	 */
	Color packageBackgroundColor();
	
	/**
	 * Color example:
	 * Color blue = new Color( Display.getDefault(), 0, 0, 255 );
	 * @return package box letters color.
	 */
	Color packageForegroundColor();
	
	/**
	 * Font example:
	 * Font f = new Font(Display.getDefault(),"style", 16, 5);
	 * last parameter explanation:
	 * 0-normal
	 * 1-bold
	 * 2-italico
	 * 3-bold italico
	 * @return package box letters font.
	 */
	Font packageFont();
	
	/**
	 * @return highlighted package box height.
	 */
	int highlightedSizeHeight();
	
	/**
	 * 
	 * @return highlighted package box width.
	 */
	int highlightedSizeWidth();
	
	/**
	 * Color example:
	 * Color blue = new Color( Display.getDefault(), 0, 0, 255 );
	 * @return highlighted package box color.
	 */
	Color highlightedBackgroundColor();
	
	/**
	 * Color example:
	 * Color blue = new Color( Display.getDefault(), 0, 0, 255 );
	 * @return highlighted package box letters color.
	 */
	Color highlightedForegroundColor();
	
	/**
	 * Font example:
	 * Font f = new Font(Display.getDefault(),"style", 16, 5);
	 * last parameter explanation:
	 * 0-normal
	 * 1-bold
	 * 2-italico
	 * 3-bold italico
	 * @return highlighted package box letters font.
	 */
	Font highlightedFont();
	
	/**
	 * @return package box icon.
	 */
	Image icon();
	
	/**
	 * Color example:
	 * Color blue = new Color( Display.getDefault(), 0, 0, 255 );
	 * @return view background color.
	 */
	Color backgroundColor();
	
	/**
	 * Implementation example:
	 * return ZestStyles.CONNECTIONS_DIRECTED
	 * @return style of the connections of the dependencies.
	 */
	int connectionStyle();
}
