import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Return driver with requested id"
    request {
        method GET()
        url("/api/v1/drivers/d9343856-ad27-4256-9534-4c59fa5e6422")
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body (
            id: "d9343856-ad27-4256-9534-4c59fa5e6422",
            name: "Jeremias Olsen",
            email: "jeremias.olsen@frontiernet.net",
            cardNumber: "8972821332297027",
            car: null,
            isAvailable: true
        )
    }
}
