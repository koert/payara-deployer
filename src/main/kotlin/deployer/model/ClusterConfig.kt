package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class ClusterConfig(var clusters: Array<ClusterItem> = arrayOf<ClusterItem>())