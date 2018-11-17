package deployer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import deployer.model.ApplicationServer
import deployer.model.DeploymentConfig
import deployer.model.Settings
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.File

/**
 *
 * @author Koert Zeilstra
 */
@ExtendWith(MockKExtension::class)
class DeploymentPlannerTest {

    @MockK
    lateinit var downloadRepository: DownloadRepository

    @Test
    fun test() {
        print("run test")
        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule()) // Enable Kotlin support
        val deploymentConfig: DeploymentConfig = mapper.readValue(File("src/test/data/deployment-test1.yaml"), DeploymentConfig::class.java)
        val settings: Settings = mapper.readValue(File("src/test/data/settings.yaml"), Settings::class.java)
//        val applicationServer = ApplicationServer(settings)

//        val downloadRepository = Mockito.mock(DownloadRepository::class.java)
        val downloadedArtifact0 = mockk<DownloadedArtifact>()
        val downloadedArtifact1 = mockk<DownloadedArtifact>()
        every { downloadRepository.getDownloadedArtifact(any(), "war") } returns downloadedArtifact0 andThen  downloadedArtifact1
//        Mockito.`when`(downloadRepository.getDownloadedArtifact(Mockito.any(DeploymentItem::class.java), Mockito.eq("war")))
//                .thenReturn(downloadedArtifact0, downloadedArtifact1)
        val applicationServer = mockk<ApplicationServer>()
//        val deploymentConfig = DeploymentConfig(
//                defaultArtifactType = "war",
//                deployments = arrayOf()
//        )
        val clusters = listOf<ApplicationServer.ServiceCluster>()

        val planner = DeploymentPlanner(deploymentConfig, downloadRepository, applicationServer)
        val planning = planner.makePlanning(clusters)

        assertThat(planning.undeployments.size).isEqualTo(1)
        assertThat(planning.deployments.size).isEqualTo(1)
    }

}
