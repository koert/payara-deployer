package deployer.model

/**
 *
 * @author Koert Zeilstra
 */
data class Instance(var name: String,
                    var hostName: String? = null,
                    var httpPort: Int? = null) {

}