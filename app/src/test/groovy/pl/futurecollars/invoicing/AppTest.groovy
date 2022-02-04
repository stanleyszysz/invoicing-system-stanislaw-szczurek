package pl.futurecollars.invoicing

import spock.lang.Specification

class AppTest extends Specification {

    def "coverage test for app class"() {
        expect:
        def app = new App()
        app.main()
    }
}
