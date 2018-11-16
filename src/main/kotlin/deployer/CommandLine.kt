package deployer

import deployer.model.ClusterSetting
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.regex.Pattern

/**
 *
 * @author Koert Zeilstra
 */
class CommandLine(val cluster: ClusterSetting) {
    val log = LoggerFactory.getLogger(CommandLine::class.java)

    fun execute(options: List<String>): Result {
        if (this.cluster.adminCommand == null) {
            throw RuntimeException("adminCommand is not specified")
        }
        val commandLine = mutableListOf<String>(this.cluster.adminCommand,
                "-p", Integer.toString(cluster.adminPort),
                "--user", cluster.adminUser ?: "admin",
                "--terse=true", "--interactive=false")
        if (cluster.adminPasswordFile != null) {
            commandLine.addAll(listOf<String>("--passwordfile", cluster.adminPasswordFile))
        }
        options.forEach { option -> commandLine.add(option) }
        log.debug("commandLine: {}", commandLine.joinToString(separator = " "))
        val builder: ProcessBuilder = ProcessBuilder()
        builder.command(commandLine)
        val process: Process = builder.start()
        val streamLineReader = StreamLineReader(process.inputStream)
        val executor = Executors.newSingleThreadExecutor()
        executor.submit(streamLineReader)
        val exitCode = process.waitFor()
//        Thread.sleep(2000)
        log.debug("exitCode: {}", exitCode)
        log.debug("output: {}", streamLineReader.lines)
        executor.shutdown()
        return Result(exitCode, streamLineReader.lines)
    }

    data class Result (val exitCode: Int, val lines: List<String>) {

    }
}
