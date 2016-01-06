package extensions;

import java.util.List;

import pa.iscde.packdep.extensions.packdepAction;
import pa.iscde.packdep.info.PackageInfo;

public class Action1 implements packdepAction {

	@Override
	public String getActionName() {
		// TODO Auto-generated method stub
		
		return new String ("PACKAGE SELECTED NAME");
	}
	
	@Override
	public void execute(List<PackageInfo> lista) {
		// TODO Auto-generated method stub
		for(PackageInfo p : lista){
			System.out.println(p.getPackageName());
		}
	}

}
