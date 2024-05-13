import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Return driver with updated availability"
    request {
        method PATCH()
        url("/api/v1/drivers/4c2b3a93-1d97-4ccf-a7b8-824daea08671/availability")
        headers {
            contentType applicationJson()
        }
        body(
            isAvailable: false
        )
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body (
            id: "4c2b3a93-1d97-4ccf-a7b8-824daea08671",
            name: "Jeremias Olsen",
            email: "jeremias.olsen@frontiernet.net",
            cardNumber: "1522613953683617",
            car: null,
            isAvailable: false
        )
    }
}
