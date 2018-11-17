package deployer

import deployer.model.ApplicationServer
import deployer.model.DeploymentConfig
import deployer.model.DeploymentItem

/**
 *
 * @author Koert Zeilstra
 */
class DeploymentPlanner(
        val deploymentConfig: DeploymentConfig,
        val downloadRepository: DownloadRepository,
        val applicationServer: ApplicationServer) {

    fun makePlanning(clusters: List<ApplicationServer.ServiceCluster>): DeploymentPlanning {
        val undeployments = deploymentConfig.deployments
                .filter { deploymentItem -> isUndeployNeeded(clusters, deploymentItem) }
                .map (this::toScheduledDeployment)
                .toList()
        val deployments = deploymentConfig.deployments
                .filter { deploymentItem -> isUndeployNeeded(clusters, deploymentItem) }
                .map { deploymentItem ->
                    ScheduledDeployment(deploymentItem, downloadRepository.getDownloadedArtifact(deploymentItem, deploymentConfig.defaultArtifactType))
                }
                .toList()

//        val deployments = deploymentConfig.deployments.map { deploymentItem ->
//            ScheduledDeployment(deploymentItem, downloadRepository.getDownloadedArtifact(deploymentItem, deploymentConfig.defaultArtifactType))
//        }.toList()

        return DeploymentPlanning(undeployments, deployments)
    }

    private fun toScheduledDeployment(deploymentItem: DeploymentItem): ScheduledDeployment {
        return ScheduledDeployment(deploymentItem, downloadRepository.getDownloadedArtifact(deploymentItem,
                this.deploymentConfig.defaultArtifactType))
    }

    private fun isUndeployNeeded(clusters: List<ApplicationServer.ServiceCluster>, deploymentItem: DeploymentItem): Boolean {
        return deploymentItem.version == null && isInstalled(clusters, deploymentItem)
    }

    private fun isInstalled(clusters: List<ApplicationServer.ServiceCluster>, deploymentItem: DeploymentItem): Boolean {
        return true
    }

}