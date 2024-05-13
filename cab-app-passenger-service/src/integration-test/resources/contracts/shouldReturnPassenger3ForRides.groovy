import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Return passenger with requested id"
    request {
        method GET()
        url("/api/v1/passengers/2b6716c3-0c8d-4a3d-90f5-49ebcc2b77d8")
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body (
            id: "2b6716c3-0c8d-4a3d-90f5-49ebcc2b77d8",
            name: "Vivienne Gutierrez",
            email: "vivienne.gutierrez@yahoo.com.ar",
            cardNumber: "7853471929691513"
        )
    }
}
