package cz.orany.lusk

import groovy.transform.CompileStatic

@CompileStatic
class NameGenerator {

    private static final Random RANDOM = new Random()
    private static final Range<String> UPPER = 'A'..'Z'
    private static final Range<String> LOWER = 'a'..'z'
    private static final int NAME_LENGTH = 10
    private static final String BEAN_SUFFIX = 'Service'

    static Set<String> generateBeanNames(int count) {
        Set<String> names = new HashSet<>()
        while (names.size() < count) {
            names.add(generateName() + BEAN_SUFFIX)
        }
        return names
    }

    private static String generateName() {
        StringBuilder builder = new StringBuilder(UPPER[RANDOM.nextInt(UPPER.size())])
        (NAME_LENGTH - 1).times {
            builder.append(LOWER[RANDOM.nextInt(LOWER.size())])
        }
        return builder.toString()
    }

}
