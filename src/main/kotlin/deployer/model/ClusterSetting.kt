package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class ClusterSetting(var name: String? = null,
                          var type: String? = null,
                          val nodes: Array<String> = arrayOf<String>(),
                          var rollingUpdate: Boolean = false,
                          var defaultArtifactType: String? = null,
                          var httpPort: Int? = null,
                          val adminCommand: String? = null,
                          var adminPort: Int = 4848,
                          var adminUser: String? = null,
                          val adminPasswordFile: String? = null)
