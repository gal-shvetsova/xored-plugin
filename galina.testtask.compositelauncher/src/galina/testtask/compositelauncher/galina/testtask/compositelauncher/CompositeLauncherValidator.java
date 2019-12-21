package galina.testtask.compositelauncher;

import org.eclipse.debug.core.ILaunchConfiguration;

public class CompositeLauncherValidator {

	public static boolean validateConfiguration(ILaunchConfiguration configuration, String mode) {
		return !hasCycle(configuration, mode);
	}

	public static boolean hasCycle(ILaunchConfiguration currentConfiguration, String mode) {
		return CompositeLauncherConfigurationHelper.getCompositeConfigurations(currentConfiguration, mode).stream()
				.anyMatch(configuration -> contains(configuration, currentConfiguration, mode));
	}

	private static boolean contains(ILaunchConfiguration configuration, ILaunchConfiguration subConfiguration, String mode) {
		return CompositeLauncherConfigurationHelper.getChosenConfigurations(configuration, mode).stream()
				.map(c -> c.getConfiguration())
				.anyMatch(config -> CompositeLauncherConfigurationHelper.equals(config, subConfiguration)
						|| contains(config, subConfiguration, mode));
	}
}
	