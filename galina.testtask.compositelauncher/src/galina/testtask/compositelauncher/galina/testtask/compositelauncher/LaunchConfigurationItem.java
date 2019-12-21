package galina.testtask.compositelauncher;

import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;

public class LaunchConfigurationItem {
	private final ILaunchConfigurationType type;
	private final ILaunchConfiguration configuration;
	private final Set<String> modes;
	private final String name;


	private enum ExecutionType{
		PARALLEL,
		SEQUENTIAL;
	}
	
	private LaunchConfigurationItem( ILaunchConfiguration configuration, String name,ILaunchConfigurationType type,
			Set<String> modes) {
		this.type = type;
		this.configuration = configuration;
		this.modes = modes;
		this.name = name;

	}

	public static LaunchConfigurationItem of (ILaunchConfiguration configuration, String name, ILaunchConfigurationType type,
			Set<String> modes)  {
		return new LaunchConfigurationItem(configuration, name, type, modes);
	}
	
	public final ILaunchConfigurationType getType() {
		return type;
	}

	public final ILaunchConfiguration getConfiguration() {
		return configuration;
	}

	public final Set<String> getModes() {
		return modes;
	}

	public final String getName() {
		return name;
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LaunchConfigurationItem)) {
			return false;
		}
		return ((LaunchConfigurationItem) obj).getName().equals(name);
	}
	
	
}