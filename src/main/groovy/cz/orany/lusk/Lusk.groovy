package cz.orany.lusk

import groovy.transform.CompileStatic

@CompileStatic
class Lusk {

    private final String srcFolder
    private final String testFolder
    static enum Framework {
        micronaut, spring
    }

    private final Random random = new Random()
    private final File projectRoot
    private final String framework
    private final String pkg

    Lusk(File projectRoot, Framework framework, String pkg, String srcFolder = 'src/main/java', String testFolder = 'src/test/groovy') {
        this.pkg = pkg
        this.framework = framework.toString().toLowerCase()
        this.projectRoot = projectRoot
        this.srcFolder = srcFolder
        this.testFolder = testFolder
    }

    void generate(int count) {
        String beanTemplate = getClass().getResourceAsStream(framework + 'Bean.gtpl').text
        String controllerTemplate = getClass().getResourceAsStream(framework + 'Controller.gtpl').text
        String beanCounterTemplate = getClass().getResourceAsStream(framework + 'BeanCounter.gtpl').text
        String specTemplate = getClass().getResourceAsStream('Spec.gtpl').text

        CodeGenerator beanGenerator = new CodeGenerator(beanTemplate, controllerTemplate, beanCounterTemplate, specTemplate)
        Set<String> names = new NameGenerator().generateBeanNames(count)
        Set<String> rest = new LinkedHashSet<>(names)

        File pkgDir = new File(projectRoot, srcFolder + File.separator + pkg.split(/\./).join(File.separator))
        pkgDir.mkdirs()

        names.collect {
            rest.remove(it)

            File beanSource = new File(pkgDir, it + '.java')
            beanSource.createNewFile()

            Set<String> randomItems = randomItems(rest)

            rest.remove(randomItems)

            generateFile(beanSource, beanGenerator.generateBean(pkg, it, randomItems))

            File controllerSource = new File(pkgDir, it - 'Service' + 'Controller.java')
            controllerSource.createNewFile()

            generateFile(controllerSource, beanGenerator.generateController(pkg, it))
        }

        File testPkgDir = new File(projectRoot, testFolder + File.separator + pkg.split(/\./).join(File.separator))
        testPkgDir.mkdirs()

        File specSource = new File(testPkgDir, 'HttpSpec.groovy')
        specSource.createNewFile()

        generateFile(specSource, beanGenerator.generateSpec(pkg, names))

        File beanCounterSource = new File(pkgDir, 'BeanCounter.java')
        beanCounterSource.createNewFile()

        generateFile(beanCounterSource, beanGenerator.generateBeanCounter(pkg))

        appendFile(new File(projectRoot, 'build.gradle'), getClass().getResourceAsStream('gru.gradle').text)
    }

    Set<String> randomItems(Set<String> set) {
        if (!set) {
            return Collections.emptySet()
        }
        Set<String> chosen = [] as Set
        List<String> input = new ArrayList<>(set)
        int partition = Math.max(1, set.size().intdiv(10).toInteger())
        partition.times {
            chosen.add(input.removeAt(random.nextInt(input.size())))
        }
        return chosen;
    }

    private void generateFile(File file, String content) {
        println "Generating ${shortPath(file)}"
        file.text = content
    }

    private void appendFile(File file, String text) {
        if (file.exists()) {
            println "Appending ${shortPath(file)}"
            file.append(text)
        }
    }

    private String shortPath(File file) {
        file.canonicalPath.substring(projectRoot.canonicalPath.length())
    }
}
