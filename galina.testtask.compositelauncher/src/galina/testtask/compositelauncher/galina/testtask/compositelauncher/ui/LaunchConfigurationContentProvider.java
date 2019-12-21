package galina.testtask.compositelauncher.ui;

import org.eclipse.jface.viewers.ITreeContentProvider;

import galina.testtask.compositelauncher.LaunchConfigurationItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.debug.core.ILaunchConfigurationType;

public class LaunchConfigurationContentProvider implements ITreeContentProvider {
	private List<LaunchConfigurationItem> configurations;

	@Override
	public Object[] getChildren(Object arg0) {
		if (arg0 instanceof ILaunchConfigurationType) {
			ILaunchConfigurationType type = (ILaunchConfigurationType) arg0;
		return configurations.stream()
				.filter(config -> 
					config.getType().equals(type))
				.toArray();
		} else {
			return new Object[0];
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		Set<ILaunchConfigurationType> notEmptyTypes = new HashSet<ILaunchConfigurationType>();
		if (inputElement instanceof List<?>) {
			configurations = (List<LaunchConfigurationItem>) inputElement;
			configurations.stream()
				.forEach(config -> 
					notEmptyTypes.add(config.getType()));
		}
		return notEmptyTypes.toArray();
	}

	@Override
	public Object getParent(Object launchConfigurationItem) {
		if (launchConfigurationItem instanceof LaunchConfigurationItem) {
			return ((LaunchConfigurationItem) launchConfigurationItem).getType().getName();
		} else {
			return null;
		}
	}

	@Override
	public boolean hasChildren(Object type) {
		if (type instanceof ILaunchConfigurationType) {
			ILaunchConfigurationType launchConfigurationType = (ILaunchConfigurationType) type;
			return configurations.stream()
					.filter(config -> 
						config.getType().equals(launchConfigurationType))
					.toArray().length > 0;
		} else {
			return false;
		}
	}

}
