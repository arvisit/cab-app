import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Return passenger with requested id"
    request {
        method GET()
        url("/api/v1/passengers/3abcc6a1-94da-4185-aaa1-8a11c1b8efd2")
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body (
            id: "3abcc6a1-94da-4185-aaa1-8a11c1b8efd2",
            name: "Vivienne Gutierrez",
            email: "vivienne.gutierrez@yahoo.com.ar",
            cardNumber: "7853471929691513"
        )
    }
}
