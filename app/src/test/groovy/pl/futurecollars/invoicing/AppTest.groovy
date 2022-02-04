package pl.futurecollars.invoicing

import spock.lang.Specification

class AppTest extends Specification {

    def "should initialize applications"() {
        expect:
        def app = new App()
        app.main()
    }
}
