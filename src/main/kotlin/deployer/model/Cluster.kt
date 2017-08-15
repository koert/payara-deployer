package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class Cluster(var name: String? = null, var deploy: Array<Deploy> = arrayOf<Deploy>())