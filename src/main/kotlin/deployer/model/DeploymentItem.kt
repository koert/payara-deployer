package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class DeploymentItem(
        var artifact: String? = null,
        var group: String? = null,
        var version: String? = null,
        var name: String? = null,
        var type: String? = null,
        var target: Array<String> = arrayOf<String>())