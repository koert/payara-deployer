package deployer.model

import deployer.CommandLine
import deployer.DeployedComponent
import deployer.ScheduledDeployment
import org.slf4j.LoggerFactory
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors

/**
 *
 * @author Koert Zeilstra
 */
class ApplicationServer(val settings: Settings) {
    private val log = LoggerFactory.getLogger(ApplicationServer::class.java)

    val serverClusters: List<ServiceCluster> = this.retrieveClusters(settings)

    fun deploy(scheduledDeployments: List<ScheduledDeployment>): Unit {
        val targetDeployments: HashMap<String, MutableList<ScheduledDeployment>> = HashMap()
        scheduledDeployments.forEach({scheduledDeployment ->
            scheduledDeployment.deployment.target.forEach({target ->
                if (!targetDeployments.containsKey(target)) {
                    targetDeployments.put(target, ArrayList<ScheduledDeployment>())
                }
                val deployments = targetDeployments.get(target)
                if (deployments != null) {
                    deployments.add(scheduledDeployment)
                }
            })
        })

        val emptyDeploymentList = Collections.emptyList<ScheduledDeployment>()

        targetDeployments.keys.forEach({targetName ->
            log.info("Deploy to target {}", targetName)
            val deploymentsToCluster: List<ScheduledDeployment> = targetDeployments.get(targetName) ?: emptyDeploymentList
            val serviceCluster = this.findCluster(targetName)
            if (serviceCluster != null) {
                if (serviceCluster.instances.size == 1) {
                    this.deployToInstance(deploymentsToCluster, serviceCluster, serviceCluster.instances[0])
                } else if (serviceCluster.instances.size > 1) {
                    this.deployToInstance(deploymentsToCluster, serviceCluster, serviceCluster.instances[0])
                    this.createApplicationRef(deploymentsToCluster, serviceCluster,
                            serviceCluster.instances.subList(1, serviceCluster.instances.size - 1))
                }
            }
        })
    }

    fun retrieveClusters(settings: Settings): List<ServiceCluster> {
        return this.settings.clusterSettings.map { cluster ->
            val commandLine = CommandLine(cluster)
            val clusterInstances = cluster.instances.map { instance ->
                val result = commandLine.execute(Arrays.asList("list-applications", instance.name))
                val deployedComponents: List<DeployedComponent> = if (result.exitCode == 0) {
                    val pattern = Pattern.compile("(\\S+)\\s*<([^>]+)>")
                    result.lines.stream()
                            .map{line -> pattern.matcher(line)}
                            .filter({matcher -> matcher.matches()})
                            .map({matcher -> DeployedComponent(matcher.group(1)) })
                            .collect(Collectors.toList())
                } else {
                    ArrayList<DeployedComponent>()
                }
                ClusterInstance(instance, deployedComponents)
            }
            ServiceCluster(cluster.name, cluster, clusterInstances)
        }
    }

    private fun findCluster(name: String): ServiceCluster? {
        var foundClusterSetting: ServiceCluster? = null
        for (serviceCluster: ServiceCluster in this.serverClusters) {
            if (serviceCluster.name.equals(name)) {
                foundClusterSetting = serviceCluster
            }
        }
        return foundClusterSetting
    }

    private fun deployToInstance(deployments: List<ScheduledDeployment>, serviceCluster: ServiceCluster, clusterInstance: ClusterInstance): Unit {
        val commandLine = CommandLine(serviceCluster.clusterSetting)
        deployments.forEach { scheduledDeployment ->
            val result = commandLine.execute(Arrays.asList("deploy",
                    "--target", clusterInstance.instance.name,
                    "--name", scheduledDeployment.name,
                    scheduledDeployment.download.file.absolutePath))
            if (result.exitCode == 0) {
            } else {
                log.error("deployment failed: {}", scheduledDeployment.name)
            }
        }
    }

    private fun createApplicationRef(deployments: List<ScheduledDeployment>, serviceCluster: ServiceCluster, clusterInstances: List<ClusterInstance>): Unit {
        deployments.forEach { scheduledDeployment ->
            clusterInstances.forEach({ clusterInstance: ClusterInstance ->
                val commandLine = CommandLine(serviceCluster.clusterSetting)
                val result = commandLine.execute(Arrays.asList("create-application-ref",
                        "--target", clusterInstance.instance.name,
                        scheduledDeployment.name))
                if (result.exitCode == 0) {
                } else {
                    log.error("deployment failed: {}", scheduledDeployment.name)
                }
            })
        }
    }

    data class ServiceCluster(val name: String,
                              val clusterSetting: ClusterSetting,
                              val instances: List<ClusterInstance>) {
    }

    data class ClusterInstance(val instance: Instance, val components: List<DeployedComponent>) {}

}