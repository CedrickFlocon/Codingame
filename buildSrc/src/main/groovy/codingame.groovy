import groovy.io.FileType
import org.gradle.api.DefaultTask
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency
import org.gradle.api.tasks.TaskAction

class Codingame extends DefaultTask {

    @TaskAction
    def buildSingleFile() {
        def outputDir = new File("${project.buildDir}/codingame/")
        def outputFile = new File("${project.buildDir}/codingame/codingame.kt")
        def bestPackage = "org.neige.codingame"

        outputDir.mkdirs()

        if (outputFile.exists()) {
            outputFile.text = ''
        } else {
            outputFile.createNewFile()
        }

        def importLine = new ArrayList()
        def codeLine = new ArrayList()

        def internalProjects = project.configurations.find { it.name == "-runtime" }.dependencies.findAll { it instanceof DefaultProjectDependency }.stream().map { it.dependencyProject }.collect()

        [project, *internalProjects].each {
            println(it)
            def sourceSetFile = it.sourceSets.main.kotlin.srcDirs[0]

            sourceSetFile.eachFileRecurse(FileType.FILES) {
                if (it.name.endsWith(".kt")) {
                    it.readLines().each {
                        if (it.startsWith("import")) {
                            if (!importLine.contains(it) && !it.contains(bestPackage)) {
                                importLine.add(it)
                            }
                        } else if (!it.startsWith("package")) {
                            codeLine.add(it)
                        }
                    }
                }
            }
        }

        importLine.each {
            outputFile << it + "\n"
        }

        codeLine.each {
            outputFile << it + "\n"
        }
    }

}
