package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class Deploy(var artifact: String, var group: String, var version: String, var name: String, var type: String)