package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class DeploymentConfig(
        var defaultArtifactType: String?,
        var deployments: Array<DeploymentItem> = arrayOf<DeploymentItem>())