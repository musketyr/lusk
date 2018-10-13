package cz.orany.lusk

import groovy.transform.CompileStatic

@CompileStatic
class Lusk {

    public static enum Framework {
        MICRONAUT, SPRING
    }

    private final Random random = new Random()
    private final File sourcesRoot
    private final String framework
    private final String pkg

    Lusk(File sourcesRoot, Framework framework, String pkg) {
        this.pkg = pkg
        this.framework = framework.toString().toLowerCase()
        this.sourcesRoot = sourcesRoot
    }

    void generate(int count) {
        String beanTemplate = getClass().getResourceAsStream(framework + 'Bean.gtpl').text
        String controllerTemplate = getClass().getResourceAsStream(framework + 'Controller.gtpl').text
        CodeGenerator beanGenerator = new CodeGenerator(beanTemplate, controllerTemplate)
        Set<String> names = new NameGenerator().generateBeanNames(count)
        Set<String> rest = new LinkedHashSet<>(names)

        names.collect {
            rest.remove(it)

            File pkgDir = new File(sourcesRoot, pkg.split(/\./).join(File.separator))
            pkgDir.mkdirs()

            File beanSource = new File(pkgDir, it + '.java')
            beanSource.createNewFile()

            Set<String> randomItems = randomItems(rest)

            rest.remove(randomItems)

            beanSource.text = beanGenerator.generateBean(pkg, it, randomItems)

            File controllerSource = new File(pkgDir, it - 'Service' + 'Controller.java')
            controllerSource.createNewFile()

            controllerSource.text = beanGenerator.generateController(pkg, it)
        }
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
}
