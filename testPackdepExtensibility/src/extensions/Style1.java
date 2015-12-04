package extensions;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import pa.iscde.packdep.extensions.packdepStyle;

public class Style1 implements packdepStyle {
	@Override
	public int packageSizeHeight() {
		return 100;
	}
	
	@Override
	public int packageSizeWidth() {
		return 100;
	}

	@Override
	public Color packageBackgroundColor() {
		return new Color(Display.getDefault(), 255, 0, 0);
	}

	@Override
	public Color packageForegroundColor() {
		return new Color(Display.getDefault(), 0, 0, 255);
	}

	@Override
	public Font packageFont() {
		return new Font(Display.getDefault(), "style", 8, 2);
	}

	@Override
	public int highlightedSizeHeight() {
		return 50;
	}
	
	@Override
	public int highlightedSizeWidth(){
		return 50;
	}

	@Override
	public Color highlightedBackgroundColor() {
		return new Color(Display.getDefault(), 110, 110, 110);
	}

	@Override
	public Color highlightedForegroundColor() {
		return new Color(Display.getDefault(), 255, 0, 0);
	}

	@Override
	public Font highlightedFont() {
		return new Font(Display.getDefault(), "style", 16, 3);
	}

	@Override
	public Image icon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color backgroundColor() {
		return new Color(Display.getDefault(), 10, 10, 10);
	}
}
