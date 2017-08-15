package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class Deployment(var clusters: Array<Cluster> = arrayOf<Cluster>())