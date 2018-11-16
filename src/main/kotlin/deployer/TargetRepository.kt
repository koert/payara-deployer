package deployer

import deployer.model.ClusterSetting
import deployer.model.Settings
import org.slf4j.LoggerFactory
import java.util.Arrays.asList
import java.util.regex.Pattern
import java.util.stream.Collectors

/**
 *
 * @author Koert Zeilstra
 */
class TargetRepository(val settings: Settings) {
    val log = LoggerFactory.getLogger(TargetRepository::class.java)

    fun findCluster(name: String?): ClusterSetting? {
        var foundClusterSetting: ClusterSetting? = null
        for (clusterSetting: ClusterSetting in this.settings.clusterSettings) {
            if (clusterSetting.name.equals(name)) {
                foundClusterSetting = clusterSetting
            }
        }
        return foundClusterSetting
    }

    fun retrieveDeployedArtifacts(): Unit {
        this.settings.clusterSettings.forEach { cluster ->
            val commandLine = CommandLine(cluster)
            cluster.instances.forEach { instance ->
                val builder: ProcessBuilder = ProcessBuilder()
                if (cluster.adminCommand == null) {
                    log.error("cannot deploy, adminCommand is not specified")
                } else {
                    val result = commandLine.execute(asList("list-applications", instance.name))
                    if (result.exitCode == 0) {
                        val pattern = Pattern.compile("(\\S+)\\s*<([^>]+)>")
                        val deployedComponents = result.lines.stream()
                                .map{line -> pattern.matcher(line)}
                                .filter({matcher -> matcher.matches()})
                                .map({matcher -> DeployedComponent(matcher.group(1))})
                                .collect(Collectors.toList())
                    }

                }
            }
        }
    }

}