import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Return driver with requested id"
    request {
        method GET()
        url("/api/v1/drivers/4c2b3a93-1d97-4ccf-a7b8-824daea08671")
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
            isAvailable: true
        )
    }
}
