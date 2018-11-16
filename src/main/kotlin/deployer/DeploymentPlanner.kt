package deployer

import deployer.model.ApplicationServer
import deployer.model.DeploymentConfig

/**
 *
 * @author Koert Zeilstra
 */
class DeploymentPlanner(
        val downloadRepository: DownloadRepository,
        val applicationServer: ApplicationServer) {

    fun makePlanning(deploymentConfig: DeploymentConfig, clusters: List<ApplicationServer.ServiceCluster>): DeploymentPlanning {

        val deployments = deploymentConfig.deployments.map { deploymentItem ->
            val type: String = deploymentItem.type ?: deploymentConfig.defaultArtifactType
            val downloadedArtifact = downloadRepository.getDownloadedArtifact(deploymentItem, type)
            if (downloadedArtifact == null) {
                throw RuntimeException("download not found")
            }
            ScheduledDeployment(deploymentItem, downloadedArtifact)
        }.toList()


        return DeploymentPlanning(deployments)
    }

}