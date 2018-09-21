package deployer.model

/**
 * @param name Environment name.
 * @param type Type of setup: server (standalone server), das (domain administration server with clusters)
 * @param rollingUpdate True - perform rolling updates across nodes
 * @author Koert Zeilstra
 */
data class EnvironmentItem(var name: String? = null,
                           var type: String? = null,
                           var rollingUpdate: Boolean? = false,
                           var deploy: Array<Deploy> = arrayOf<Deploy>()
)
