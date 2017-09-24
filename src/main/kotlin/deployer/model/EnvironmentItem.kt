package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class EnvironmentItem(var name: String? = null, var type: String? = null, var rollingUpdate: Boolean? = false, var deploy: Array<Deploy> = arrayOf<Deploy>())