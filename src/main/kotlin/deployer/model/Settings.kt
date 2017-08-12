package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class Settings (var useVersion: Boolean, var downloadRepositoryUrl: List<String>, var uploadRepositoryUrl: String,
                     var clusterSettings: List<ClusterSetting>)