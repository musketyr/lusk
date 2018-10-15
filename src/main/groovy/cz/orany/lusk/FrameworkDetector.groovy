package cz.orany.lusk

class FrameworkDetector {

    private final File projectDirectory
    private final String mainSourceDirectory

    FrameworkDetector(File projectDirectory, String mainSourceDirectory) {
        this.projectDirectory = projectDirectory
        this.mainSourceDirectory = mainSourceDirectory
    }

    Hint detect() {
        File sourceRoot = new File(projectDirectory, mainSourceDirectory)

        if (!sourceRoot.exists()) {
            return null
        }

        Hint hint = null

        sourceRoot.eachFileRecurse {
            if (it.name.contains('Application.')) {
                List<String> lines = it.readLines()

                String pkg = lines.find { it.startsWith('package') }?.getAt([8..-1])?.replace(';', '')?.trim()

                Lusk.Framework framework = null

                if (lines.any { it.contains('io.micronaut.runtime.Micronaut') }) {
                    framework = Lusk.Framework.micronaut
                } else if(lines.any { it.contains('org.springframework.boot.SpringApplication')}) {
                    framework = Lusk.Framework.spring
                }
                if (pkg && framework) {
                    hint = new Hint(framework, pkg, it.name)
                }
            }
        }

        return hint
    }
}

class Hint {
    final Lusk.Framework framework
    final String pkg
    final String applicationClassName

    Hint(Lusk.Framework framework, String pkg, String applicationClassName) {
        this.framework = framework
        this.pkg = pkg
        this.applicationClassName = applicationClassName
    }
}
