package pl.futurecollars.invoicing.services

import pl.futurecollars.invoicing.exceptions.FileSystemException
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.StandardOpenOption

class FileServiceTest extends Specification {

    def invalidPath = Path.of("/home/stanley/invoicing-system-stanislaw-szczurek/app/src/main/resources/invoices-non-existing.txt")
    def fileService = new FileService(invalidPath)

    def "WriteToFile"() {
        when:
        fileService.writeToFile("Test")

        then:
        thrown FileSystemException
    }

    def "OverwriteTheFile"() {
        when:
        fileService.overwriteTheFile("Test", StandardOpenOption.TRUNCATE_EXISTING)

        then:
        thrown FileSystemException
    }

    def "ReadFile"() {
        when:
        fileService.readFile()

        then:
        thrown FileSystemException
    }
}
