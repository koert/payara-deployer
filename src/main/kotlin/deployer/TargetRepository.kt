package deployer

import deployer.model.ClusterSetting
import deployer.model.Settings

/**
 *
 * @author Koert Zeilstra
 */
class TargetRepository(val settings: Settings) {

    fun findCluster(name: String?): ClusterSetting? {
        var foundClusterSetting: ClusterSetting? = null
        for (clusterSetting: ClusterSetting in this.settings.clusterSettings) {
            if (clusterSetting.name.equals(name)) {
                foundClusterSetting = clusterSetting
            }
        }
        return foundClusterSetting
    }

}