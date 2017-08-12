package deployer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import deployer.model.Deployment
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException

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
//            System.out.println(ReflectionToStringBuilder.toString(deployment, ToStringStyle.MULTI_LINE_STYLE));
//            Settings settings = mapper.readValue(new File("deployer/src/test/data/settings.yaml"), Settings.class);
//            System.out.println(ReflectionToStringBuilder.toString(deployment, ToStringStyle.MULTI_LINE_STYLE));
//
//            List<Download> downloads = new ArrayList<>();
//            for (Cluster cluster : deployment.getClusters()) {
//                for (Deploy deploy : cluster.getDeploy()) {
//                String type = deploy.getType();
//                if (type == null) {
//                    type = settings.findCluster(cluster.getName()).map(clusterSetting -> clusterSetting.getDefaultArtifactType())
//                    .orElse("war");
//                }
//                downloads.add(new Download(deploy.getGroup(), deploy.getArtifact(), deploy.getVersion(), type));
//            }
//            }
//            downloadDirectory.mkdirs();
//            for (Download download : downloads) {
//                download.execute(settings, downloadDirectory);
//            }
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    private static class Download {
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
//        public void execute(Settings settings, File downloadDirectory) {
//            boolean done = false;
//            for (int i = 0; !done && i < settings.getDownloadRepositoryUrl().length; i++) {
//                String repositoryUrl = settings.getDownloadRepositoryUrl()[i];
//                StringBuffer fileName = new StringBuffer(artifact).append("-").append(version).append(".").append(extension);
//                StringBuffer url = new StringBuffer(repositoryUrl);
//                if (this.group != null){
//                    url.append("/").append(group);
//                }
//                url.append("/").append(artifact).append("/").append(version).append("/").append(fileName);
//                try {
//                    URL downloadUrl = new URL(url.toString());
//                    File downloadFile = new File(downloadDirectory, fileName.toString());
//                    try (InputStream inputStream = downloadUrl.openStream();) {
//                        log.debug("download {}", downloadUrl.toString());
//                        Files.copy(inputStream, Paths.get(downloadFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
////            byte[] buffer = new byte[1024];
////            int length = inputStream.read(buffer);
////            while (length > 0) {
////              outputStream.write(buffer, 0, length);
////              length = inputStream.read(buffer);
////            }
////            outputStream.flush();
//                        done = true;
//                    } catch (IOException e) {
//                        log.debug("failed to open URL {}", url.toString());
//                    }
//                    } catch (MalformedURLException e) {
//                        throw new RuntimeException("Invalid URL", e);
//                    }
//                }
//            if (!done) {
//                throw new RuntimeException("download failed: " + artifact + "-" + version + "." + extension);
//            }
//        }
//    }
}