package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class ClusterSetting(var name: String? = null, var type: String? = null, var nodes: Array<String> = arrayOf<String>(), var rollingUpdate: Boolean = false,
                          var defaultArtifactType: String? = null, var httpPort: Int? = null)
