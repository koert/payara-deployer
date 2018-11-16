package deployer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import deployer.model.ApplicationServer
import deployer.model.DeploymentConfig
import deployer.model.DeploymentItem
import deployer.model.Settings
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.io.File

/**
 *
 * @author Koert Zeilstra
 */
//@RunWith(MockitoJUnitRunner::class.java)
@ExtendWith(MockitoExtension::class)
class DeploymentPlannerTest {

//    @Mock
//    lateinit var downloadRepository: DownloadRepository


    @Test
    fun test() {
        print("run test")
        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule()) // Enable Kotlin support
        val deploymentConfig: DeploymentConfig = mapper.readValue(File("src/test/data/deployment-test1.yaml"), DeploymentConfig::class.java)
        val settings: Settings = mapper.readValue(File("src/test/data/settings.yaml"), Settings::class.java)
//        val applicationServer = ApplicationServer(settings)

        val downloadRepository = Mockito.mock(DownloadRepository::class.java)
        val downloadedArtifact0 = Mockito.mock(DownloadedArtifact::class.java)
        val downloadedArtifact1 = Mockito.mock(DownloadedArtifact::class.java)
        Mockito.`when`(downloadRepository.getDownloadedArtifact(Mockito.any(DeploymentItem::class.java), Mockito.eq("war")))
                .thenReturn(downloadedArtifact0, downloadedArtifact1)
        val applicationServer = Mockito.mock(ApplicationServer::class.java)
//        val deploymentConfig = DeploymentConfig(
//                defaultArtifactType = "war",
//                deployments = arrayOf()
//        )
        val clusters = listOf<ApplicationServer.ServiceCluster>()

        val planner = DeploymentPlanner(downloadRepository, applicationServer)
        val planning = planner.makePlanning(deploymentConfig, clusters)

        assertThat(planning.undeployments).isEqualTo(2)
    }

}
