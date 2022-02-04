package pl.futurecollars.invoicing.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.nio.file.StandardOpenOption

@SpringBootTest
class FileServiceTest extends Specification {

    @Autowired
    private FileService fileService

    void cleanup() {
        fileService.clear()
    }

    def "should write object to file"() {
        when:
        fileService.writeToFile("Test")

        then:
        noExceptionThrown()
    }

    def "should overwrite object to file"() {
        when:
        fileService.overwriteTheFile("Test", StandardOpenOption.TRUNCATE_EXISTING)

        then:
        noExceptionThrown()
    }

    def "should read file"() {
        when:
        fileService.readFile()

        then:
        noExceptionThrown()
    }
}
