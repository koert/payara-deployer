package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class DeploymentConfig(var deployments: Array<DeploymentItem> = arrayOf<DeploymentItem>())