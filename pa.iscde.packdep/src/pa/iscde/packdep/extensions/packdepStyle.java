package pa.iscde.packdep.extensions;

import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.ZestStyles;

import pa.iscde.packdep.info.GlobalInfo;
import pa.iscde.packdep.info.PackageInfo;
import pa.iscde.packdep.info.PackageSize;

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


	    void init(GlobalInfo gInfo);
	    
	    Color getColor(PackageInfo info);
	    
	    Color getTextColor(PackageInfo info);
	    
	    Color getHighlightColor(PackageInfo info);
	    
	    Color getBorderColor(PackageInfo info);
	    
	    Color getBorderHighlightColor(PackageInfo info);
	    
	    PackageSize getSize(PackageInfo info);
	    
	    Font getTextFont(PackageInfo info);
	    
	    Color getBackgroundColor(PackageInfo info);
	    
	    Image getIcon(PackageInfo info);
	
}
