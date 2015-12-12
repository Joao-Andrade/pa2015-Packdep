package extensions;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import pa.iscde.packdep.extensions.packdepStyle;
import pa.iscde.packdep.info.GlobalInfo;
import pa.iscde.packdep.info.PackageInfo;
import pa.iscde.packdep.info.PackageSize;

public class Style3 implements packdepStyle {

	@Override
	public void init(GlobalInfo gInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public Color getColor(PackageInfo info) {
		Color c;
		System.out.println(info.getnDependencies() + " " + info.getName());
		if(info.getnDependencies()<=0){
			c = new Color(Display.getDefault(), 0, 0, 200);
		}
		else{
			c = new Color(Display.getDefault(), 200, 0, 0);
		}
		return c;
	}

	@Override
	public Color getTextColor(PackageInfo info) {
		Color c = new Color(Display.getDefault(), 200, 200, 0);
		return c;
	}

	@Override
	public Color getHighlightColor(PackageInfo info) {
		Color c = new Color(Display.getDefault(), 200, 0, 0);
		return c;
	}

	@Override
	public Color getBorderColor(PackageInfo info) {
		Color c = new Color(Display.getDefault(), 200, 0, 0);
		return c;
	}

	@Override
	public Color getBorderHighlightColor(PackageInfo info) {
		Color c = new Color(Display.getDefault(), 0, 0, 0);
		return c;
	}

	@Override
	public PackageSize getSize(PackageInfo info) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Font getTextFont(PackageInfo info) {
		Font f = new Font(Display.getCurrent(), "style", 8, 0);
		return f;
	}

	@Override
	public Color getBackgroundColor(PackageInfo info) {
		Color c = new Color(Display.getDefault(), 0, 0, 0);
		return c;
	}

	@Override
	public Image getIcon(PackageInfo info) {
		// TODO Auto-generated method stub
		return null;
	}

}
