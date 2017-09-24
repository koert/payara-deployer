package deployer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import deployer.model.*
import org.apache.commons.lang3.builder.ReflectionToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

val DEPLOYER_DOWNLOAD_DIR = "--downloadDir"
val DEPLOYER_DEPLOYMENT = "--deployment"
val DEPLOYER_CLUSTER = "--cluster"
val DEPLOYER_SETTINGS = "--settings"

fun main(args : Array<String>) {
    var downloadDirectory = File("download");
    var deploymentFile: File? = null;
    var clusterFile: File? = null;
    var settingsFile: File? = null;
    for (i: Int in 0..args.size-1) {
        val arg = args[i];
        if (arg.startsWith(ARG_DOWNLOAD_DIR + "=")) {
            val directoryName = arg.substring(ARG_DOWNLOAD_DIR.length + 1);
            downloadDirectory = File(directoryName);
        } else if (arg.startsWith(ARG_DEPLOYMENT + "=")) {
            val fileName = arg.substring(ARG_DEPLOYMENT.length + 1);
            deploymentFile = File(fileName);
        } else if (arg.startsWith(DEPLOYER_CLUSTER + "=")) {
            val fileName = arg.substring(DEPLOYER_CLUSTER.length + 1);
            clusterFile = File(fileName);
        } else if (arg.startsWith(ARG_SETTINGS + "=")) {
            val fileName = arg.substring(ARG_SETTINGS.length + 1);
            settingsFile = File(fileName);
        }
    }

    if (deploymentFile == null) {
        deploymentFile = File("src/test/data/deployment.yaml");
    }
    if (clusterFile == null) {
        clusterFile = File("src/test/data/cluster.yaml");
    }
    if (settingsFile == null) {
        settingsFile = File("src/test/data/settings.yaml");
    }

    val mapper = ObjectMapper(YAMLFactory());
    try {
        val deploymentConfig: DeploymentConfig = mapper.readValue(deploymentFile, DeploymentConfig::class.java)
        println(ReflectionToStringBuilder.toString(deploymentConfig, ToStringStyle.MULTI_LINE_STYLE));
        val clusterConfig: Deployment = mapper.readValue(clusterFile, Deployment::class.java)
        println(ReflectionToStringBuilder.toString(clusterConfig, ToStringStyle.MULTI_LINE_STYLE));
        val settings: Settings = mapper.readValue(settingsFile, Settings::class.java);
        println(ReflectionToStringBuilder.toString(settings, ToStringStyle.MULTI_LINE_STYLE));

        val downloads: MutableList<Packager.Download> = ArrayList();
        for (deployment: DeploymentItem in deploymentConfig.deployments) {
            for (deploy: Deploy in cluster.deploy) {
                var type: String? = deploy.type
                if (type == null) {
                    val foundCluster: ClusterSetting? = settings.findCluster(cluster.name)
                    type = if (foundCluster == null) "war" else foundCluster.defaultArtifactType
                }
                val artifact: String? = deploy.artifact
                val version: String? = deploy.version
                if (type != null && artifact != null && version != null) {
                    downloads.add(Packager.Download(deploy.group, artifact, version, type));
                }
            }
        }
        downloadDirectory.mkdirs();
        downloads.forEach { it.execute(settings, downloadDirectory) }
    } catch (e: IOException) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}

/**
 *
 * @author Koert Zeilstra
 */
class Deployer {

    data class Download(val group: String?, val artifact: String, val version: String, val extension: String) {

        val log = LoggerFactory.getLogger(Packager::class.java)

        //        private String group;
//        private String artifact;
//        private String version;
//        private String extension;
//
//        public Download(String group, String artifact, String version, String extension) {
//            this.group = group;
//            this.artifact = artifact;
//            this.version = version;
//            this.extension = extension;
//        }
//
        fun execute(settings: Settings, downloadDirectory: File) {
            var done = false;
            for (i in 0..settings.downloadRepositoryUrl.size-1) {
                if (!done) {
                    val repositoryUrl = settings.downloadRepositoryUrl[i];
                    val fileName = StringBuffer(artifact).append("-").append(version).append(".").append(extension);
                    val url = StringBuffer(repositoryUrl);
                    if (this.group != null) {
                        url.append("/").append(group);
                    }
                    url.append("/").append(artifact).append("/").append(version).append("/").append(fileName);
                    try {
                        val downloadUrl: URL = URL(url.toString());
                        val downloadFile: File = File(downloadDirectory, fileName.toString());
                        try {
                            downloadUrl.openStream().use {
                                //                        log.debug("download {}", downloadUrl.toString());
                                Files.copy(it, Paths.get(downloadFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
                                done = true;
                            }
                        } catch (e: Exception) {
                            log.debug("failed to open URL {}", url.toString());
                        }
                    } catch (e: MalformedURLException) {
                        throw RuntimeException("Invalid URL", e)
                    }
                }
            }
            if (!done) {
                throw RuntimeException("download failed: " + artifact + "-" + version + "." + extension)
            }
        }
    }
}