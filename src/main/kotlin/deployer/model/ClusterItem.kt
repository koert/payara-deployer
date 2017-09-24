package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class ClusterItem(var name: String? = null, var environments: Array<EnvironmentItem> = arrayOf<EnvironmentItem>())