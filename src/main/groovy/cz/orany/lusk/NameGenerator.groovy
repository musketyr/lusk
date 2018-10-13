package cz.orany.lusk

import groovy.transform.CompileStatic

@CompileStatic
class NameGenerator {

    private static final Random RANDOM = new Random()
    private static final Range<String> UPPER = 'A'..'Z'
    private static final Range<String> LOWER = 'a'..'z'
    private static final int NAME_LENGTH = 10

    private final String beanSuffix
    private final String factorySuffix

    NameGenerator(String beanSuffix, String factorySuffix) {
        this.beanSuffix = beanSuffix
        this.factorySuffix = factorySuffix
    }

    NameGenerator() {
        this('Service', 'Factory')
    }

    Set<String> generateBeanNames(int count) {
        Set<String> names = new HashSet<>()
        while (names.size() < count) {
            names.add(generateName() + beanSuffix)
        }
        return names
    }

    String generateFactoryName() {
        return generateName() + factorySuffix
    }

    private static String generateName() {
        StringBuilder builder = new StringBuilder(UPPER[RANDOM.nextInt(UPPER.size())])
        (NAME_LENGTH - 1).times {
            builder.append(LOWER[RANDOM.nextInt(LOWER.size())])
        }
        return builder.toString()
    }

}
