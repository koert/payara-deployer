package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class Deploy(var artifact: String? = null, var group: String? = null, var version: String? = null, var name: String? = null, var type: String? = null)