import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Return ride with requested id"
    request {
        method GET()
        url("/api/v1/rides/69b509c6-20fe-4b70-a07e-31e52ea05a54")
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body (
            id: "69b509c6-20fe-4b70-a07e-31e52ea05a54",
            passengerId: "072f635e-0ee7-461e-aa7e-1901ae3d0c5e",
            driverId: "d9343856-ad27-4256-9534-4c59fa5e6422",
            paymentMethod: "BANK_CARD",
            startAddress: "Bramcote Grove, 42",
            destinationAddress: "Llewellyn Street, 35",
            initialCost: 100,
            finalCost: 100,
            isPaid: false,
            status: "END_RIDE",
            promoCode: null,
            passengerScore: null,
            driverScore: null,
            bookRide: "2024-04-04T12:02:00+03:00",
            cancelRide: null,
            acceptRide: "2024-04-04T12:02:00+03:00",
            beginRide: "2024-04-04T12:02:00+03:00",
            endRide: "2024-04-04T12:02:00+03:00",
            finishRide: null
        )
    }
}
