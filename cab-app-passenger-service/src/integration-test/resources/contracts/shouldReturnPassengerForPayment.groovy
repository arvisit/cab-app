import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Return passenger with requested id"
    request {
        method GET()
        url("/api/v1/passengers/072f635e-0ee7-461e-aa7e-1901ae3d0c5e")
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body (
            id: "072f635e-0ee7-461e-aa7e-1901ae3d0c5e",
            name: "Vivienne Gutierrez",
            email: "vivienne.gutierrez@yahoo.com.ar",
            cardNumber: "5538411806914853"
        )
    }
}
