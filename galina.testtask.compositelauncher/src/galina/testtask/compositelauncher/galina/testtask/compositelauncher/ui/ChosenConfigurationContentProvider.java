package galina.testtask.compositelauncher.ui;

import java.util.List;
import org.eclipse.jface.viewers.ITreeContentProvider;

import galina.testtask.compositelauncher.LaunchConfigurationItem;

public class ChosenConfigurationContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object list) {
		if (list instanceof List<?>) {
			return ((List<LaunchConfigurationItem>) list).toArray();
		}

		return new Object[0];
	}

	@Override
	public Object getParent(Object arg0) {
		return null;
	}

	@Override
	public boolean hasChildren(Object arg0) {
		return false;
	}

}
