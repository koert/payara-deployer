package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class Settings (var useVersion: Boolean, var downloadRepositoryUrl: Array<String>, var uploadRepositoryUrl: String,
                     var clusterSettings: Array<ClusterSetting>) {
    fun findCluster(name: String): ClusterSetting? {
        var foundClusterSetting: ClusterSetting? = null
        for (clusterSetting: ClusterSetting in clusterSettings) {
            if (clusterSetting.name.equals(name)) {
                foundClusterSetting = clusterSetting
            }
        }
        return foundClusterSetting
    }
}