package galina.testtask.compositelauncher.ui;

import org.eclipse.debug.internal.core.LaunchConfigurationType;
import org.eclipse.debug.internal.ui.DebugPluginImages;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import galina.testtask.compositelauncher.LaunchConfigurationItem;


@SuppressWarnings("restriction")
public class LaunchConfigurationLabelProvider extends BaseLabelProvider implements ITableLabelProvider {
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof LaunchConfigurationItem) {
			LaunchConfigurationItem configurationElement = (LaunchConfigurationItem) element;
				return DebugPluginImages.getImage(configurationElement.getType().getIdentifier());
		} else if (element instanceof LaunchConfigurationType) {
			LaunchConfigurationType configurationElement = (LaunchConfigurationType) element;
			return DebugPluginImages.getImage(configurationElement.getIdentifier());
		} else {
			return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		if (element instanceof LaunchConfigurationItem) {
			LaunchConfigurationItem configurationElement = (LaunchConfigurationItem) element;
			return configurationElement.getName();
		} else if (element instanceof LaunchConfigurationType) {
			LaunchConfigurationType configurationElement = (LaunchConfigurationType) element;
			return configurationElement.getName();
		} else {
			return null;
		}
	}

}
