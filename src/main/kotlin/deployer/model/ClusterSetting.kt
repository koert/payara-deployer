package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class ClusterSetting(var name: String, var nodes: Array<String>, var rollingUpdate: Boolean, var defaultArtifactType: String)
