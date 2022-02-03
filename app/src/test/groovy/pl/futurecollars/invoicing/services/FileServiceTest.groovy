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
    def "WriteToFile"() {
        when:
        fileService.writeToFile("Test")

        then:
        noExceptionThrown()
    }

    def "OverwriteTheFile"() {
        when:
        fileService.overwriteTheFile("Test", StandardOpenOption.TRUNCATE_EXISTING)

        then:
        noExceptionThrown()
    }

    def "ReadFile"() {
        when:
        fileService.readFile()

        then:
        noExceptionThrown()
    }
}
