package deployer

import deployer.model.DeploymentItem
import deployer.model.Settings
import org.slf4j.LoggerFactory
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 *
 * @author Koert Zeilstra
 */
class DownloadRepository(val downloadDirectory: File, val settings: Settings) {

    val log = LoggerFactory.getLogger(DownloadRepository::class.java)

    init {
        downloadDirectory.mkdirs();
    }

    fun getDownloadedArtifact(deployment: DeploymentItem, defaultArtifactType: String): DownloadedArtifact {
        var done = false;
        var downloadedArtifact: DownloadedArtifact? = null
        val type: String = deployment.type ?: defaultArtifactType
        for (i in 0..settings.downloadRepositoryUrl.size-1) {
            if (!done) {
                val repositoryUrl = settings.downloadRepositoryUrl[i];
                val fileName = StringBuffer(deployment.artifact).append("-").append(deployment.version).append(".").append(type);
                val url = StringBuffer(repositoryUrl);
                if (deployment.group != null) {
                    url.append("/").append(deployment.group);
                }
                url.append("/").append(deployment.artifact).append("/").append(deployment.version).append("/").append(fileName);
                try {
                    val downloadUrl: URL = URL(url.toString());
                    val downloadFile: File = File(downloadDirectory, fileName.toString());
                    try {
                        downloadUrl.openStream().use {
                            log.debug("download {}", downloadUrl.toString());
                            Files.copy(it, Paths.get(downloadFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING)
                            downloadedArtifact = DownloadedArtifact(downloadFile)
                            done = true
                        }
                    } catch (e: Exception) {
                        log.debug("failed to open URL {}", url.toString());
                    }
                } catch (e: MalformedURLException) {
                    throw RuntimeException("Invalid URL", e)
                }
            }
        }
        if (!done || downloadedArtifact == null) {
            throw RuntimeException("download failed: " + deployment.artifact + "-" + deployment.version + "." + type)
        }

        return downloadedArtifact!!
    }

}

