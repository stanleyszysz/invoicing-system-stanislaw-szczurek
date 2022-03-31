package pl.futurecollars.invoicing.services


import pl.futurecollars.invoicing.exceptions.FileSystemException
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.Car
import spock.lang.Specification

class JsonServiceTest extends Specification {

    def jsonService = new JsonService()

    def "should convert object to json and vice versa"() {
        given:
        def car = new Car(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), "AA 12345", false)

        when:
        def carToString = jsonService.toJson(car)

        and:
        def carFromJson = jsonService.toObject(carToString, Car)

        then:
        Objects.deepEquals(car, carFromJson)
    }

    def "should returned exception when creating object from empty string"() {
        when:
        jsonService.toObject("", Invoice)

        then:
        thrown FileSystemException
    }
}
