package extensions;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import pa.iscde.packdep.extensions.packdepStyle;
import pa.iscde.packdep.info.GlobalInfo;
import pa.iscde.packdep.info.PackageInfo;
import pa.iscde.packdep.info.PackageSize;

public class Style1 implements packdepStyle{

	int nClasses;
	
	
	@Override
	public void init(GlobalInfo gInfo) {
		nClasses = gInfo.getNumberOfClasses();
	}

	@Override
	public Color getColor(PackageInfo info) {
		if(info.getNumberOfClass()==0){
			return new Color(Display.getDefault(), 200, 0, 0);
		}
		else if(info.getNumberOfClass()==1){
			return new Color(Display.getDefault(), 200, 200, 0);
		}
		else{
			return new Color(Display.getDefault(), 0, 200, 0);
		}
	}

	@Override
	public Color getTextColor(PackageInfo info) {
		return new Color(Display.getDefault(), 255, 255, 255);
	}

	@Override
	public Color getHighlightColor(PackageInfo info) {
		return new Color(Display.getDefault(), 0, 0, 200);
	}

	@Override
	public Color getBorderColor(PackageInfo info) {
		return new Color(Display.getDefault(), 0, 0, 0);
	}

	@Override
	public Color getBorderHighlightColor(PackageInfo info) {
		return new Color(Display.getDefault(), 0, 0, 0);
	}

	@Override
	public PackageSize getSize(PackageInfo info) {
		PackageSize size = new PackageSize(100,50);
		return size;
	}

	@Override
	public Font getTextFont(PackageInfo info) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getBackgroundColor(PackageInfo info) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getIcon(PackageInfo info) {
		return null;
	}

}
