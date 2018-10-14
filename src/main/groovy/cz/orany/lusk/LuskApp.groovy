package cz.orany.lusk

import picocli.CommandLine

@CommandLine.Command(name = 'Lusk', version = '0.2',
    mixinStandardHelpOptions = true, // add --help and --version options
    description = '@|bold Lusk Bean Generator|@')
class LuskApp implements Runnable {

    @CommandLine.Option(names = ["-f", '--framework'], description = 'projects \'s framework')
    Lusk.Framework framework

    @CommandLine.Option(names = ["-p", '--package'], description = 'package of generated classes')
    String pkg

    @CommandLine.Option(names = ["-c", '--count'], description = 'number of beans (default 100)')
    int count = 1000

    @CommandLine.Parameters(index = "0", arity = "0..1", paramLabel = 'PROJECT_DIR')
    String path = System.getProperty("user.dir")

    @CommandLine.Option(names = ["-m", '--main-sources'], description = 'main source directory (default src/main/java)')
    String mainSourceDir = 'src/main/java'

    @CommandLine.Option(names = ["-t", '--test-sources'], description = 'test source directory (default src/test/groovy)')
    String testSourceDir = 'src/test/groovy'

    void run() {
        try {
            File projectDir = new File(path)
            Hint hint = new Hint(framework, pkg)
            if (!framework || !pkg) {
                hint = new FrameworkDetector(projectDir, mainSourceDir).detect()
                if (!hint) {
                    System.err.println('Framework and package needs to be selected')
                    return
                }
            }

            println "*** Generating $count ${hint.framework.toString().capitalize()} beans and corresponding controllers into package ${hint.pkg}... ***"

            new Lusk(projectDir, hint.framework, hint.pkg, mainSourceDir, testSourceDir).generate(count)

            println '\n *** DONE *** \n'
        } catch(Exception e) {
            e.printStackTrace()
        }

    }

    static void main(String[] args) {
        CommandLine.run(new LuskApp(), args)
    }

}
