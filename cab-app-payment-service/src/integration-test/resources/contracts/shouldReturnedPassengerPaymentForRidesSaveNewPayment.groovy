import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Return saved passenger payment on request"
    request {
        method POST()
        url("/api/v1/passenger-payments")
        headers {
            contentType applicationJson()
        }
        body(
            rideId: "ffe34487-dfa3-4660-96dc-ed108e06ab77",
            passengerId: "deecaeef-454b-487d-987c-54df212385b3",
            driverId: "d9343856-ad27-4256-9534-4c59fa5e6422",
            paymentMethod: "CASH",
            cardNumber: null,
            amount: 100
        )
    }

    response {
        status CREATED()
        headers {
            contentType applicationJson()
        }
        body (
            id: "cce748fb-1a3a-468e-a49e-08a26fe2a418",
            rideId: "ffe34487-dfa3-4660-96dc-ed108e06ab77",
            passengerId: "deecaeef-454b-487d-987c-54df212385b3",
            driverId: "d9343856-ad27-4256-9534-4c59fa5e6422",
            amount: 100,
            feeAmount: 5,
            paymentMethod: "CASH",
            cardNumber: null,
            status: "SUCCESS",
            timestamp: "2024-04-04T12:06:00+03:00"
        )
    }
}
