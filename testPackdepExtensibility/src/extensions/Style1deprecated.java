package extensions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.ZestStyles;

import pa.iscde.packdep.extensions.packdepStyle;

public class Style1deprecated implements ExtensionPointDeprecated {
	
	
	@Override
	public int packageSizeHeight() {
		return 30;
	}
	
	@Override
	public int packageSizeWidth() {
		return 100;
	}

	@Override
	public Color packageBackgroundColor() {
		return new Color(Display.getDefault(), 0, 0, 200);
	}

	@Override
	public Color packageForegroundColor() {
		return new Color(Display.getDefault(), 255, 255, 255);
	}

	@Override
	public Font packageFont() {
		return new Font(Display.getCurrent(), "style", 8, 0);
	}

	@Override
	public int highlightedSizeHeight() {
		return 30;
	}
	
	@Override
	public int highlightedSizeWidth(){
		return 100;
	}

	@Override
	public Color highlightedBackgroundColor() {
		return new Color(Display.getDefault(), 0, 0, 0);
	}

	@Override
	public Color highlightedForegroundColor() {
		return new Color(Display.getDefault(), 255, 255, 255);
	}

	@Override
	public Font highlightedFont() {
		return new Font(Display.getDefault(), "style", 8, 1);
	}

	@Override
	public Image icon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color backgroundColor() {
		return new Color(Display.getDefault(), 255, 255, 255);
	}
	
	@Override
	public int connectionStyle(){
		return ZestStyles.CONNECTIONS_DIRECTED;
	}
}
