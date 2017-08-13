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

/**
 *
 * @author Koert Zeilstra
 */
class Packager {

    val ARG_DOWNLOAD_DIR = "--downloadDir"

    val log = LoggerFactory.getLogger(Packager::class.java)

    fun main(args : Array<String>) {
        var downloadDirectory = File("download");
        for (i: Int in 0..args.size) {
            val arg = args[i];
            if (arg.startsWith(ARG_DOWNLOAD_DIR + "=")) {
                val directoryName = arg.substring(ARG_DOWNLOAD_DIR.length + 1);
                downloadDirectory = File(directoryName);
            }
        }

        val mapper = ObjectMapper(YAMLFactory());
        try {
            val deployment: Deployment = mapper.readValue(File("deployer/src/test/data/deployment.yaml"), Deployment::class.java)
            println(ReflectionToStringBuilder.toString(deployment, ToStringStyle.MULTI_LINE_STYLE));
            val settings: Settings = mapper.readValue(File("deployer/src/test/data/settings.yaml"), Settings::class.java);
            println(ReflectionToStringBuilder.toString(deployment, ToStringStyle.MULTI_LINE_STYLE));

            val downloads: MutableList<Download> = ArrayList();
            for (cluster: Cluster in deployment.clusters) {
                for (deploy: Deploy in cluster.deploy) {
                    var type: String? = deploy.type
                    if (type == null) {
                        val foundCluster: ClusterSetting? = settings.findCluster(cluster.name)
                        type = if (foundCluster == null) "war" else foundCluster.defaultArtifactType
                    }
                    downloads.add(Download(deploy.group, deploy.artifact, deploy.version, type));
                }
            }
            downloadDirectory.mkdirs();
            downloads.forEach { it.execute(settings, downloadDirectory) }
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private data class Download(val group: String?, val artifact: String, val version: String, val extension: String) {

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
            for (i in 0..settings.downloadRepositoryUrl.size) {
                val repositoryUrl = settings.downloadRepositoryUrl[i];
                val fileName = StringBuffer(artifact).append("-").append(version).append(".").append(extension);
                val url = StringBuffer(repositoryUrl);
                if (this.group != null){
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
                    throw RuntimeException("Invalid URL", e);
                }
//                }
//            if (!done) {
//                throw new RuntimeException("download failed: " + artifact + "-" + version + "." + extension);
            }
        }
    }
}