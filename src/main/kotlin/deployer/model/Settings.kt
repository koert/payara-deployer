package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class Settings (
        var useVersion: Boolean = false,
        var downloadRepositoryUrl: Array<String> = arrayOf<String>(),
        var uploadRepositoryUrl: String? = null,
        var clusterSettings: Array<ClusterSetting> = arrayOf<ClusterSetting>()) {

//    fun findCluster(name: String?): ClusterSetting? {
//        var foundClusterSetting: ClusterSetting? = null
//        for (clusterSetting: ClusterSetting in clusterSettings) {
//            if (clusterSetting.name.equals(name)) {
//                foundClusterSetting = clusterSetting
//            }
//        }
//        return foundClusterSetting
//    }
}