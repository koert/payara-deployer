package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class ClusterSetting(var name: String, var nodes: List<String>, var rollingUpdate: Boolean, var defaultArtifactType: String)
