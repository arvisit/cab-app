\connect payment_service_db;

INSERT INTO passenger_payments(id, ride_id, passenger_id, driver_id, amount, fee_amount, payment_method, card_number, status, timestamp) VALUES
    ('cce748fb-1a3a-468e-a49e-08a26fe2a418', '942d8839-7a16-4dfa-9443-179220135b3a', '072f635e-0ee7-461e-aa7e-1901ae3d0c5e', 'd9343856-ad27-4256-9534-4c59fa5e6422', 100, 5, 'BANK_CARD', '5538411806914853', 'SUCCESS', '2024-04-04T12:01:00.0+03:00'),
    ('69ee1ad8-f630-445e-964e-0301ffaaa34b', 'b4de939f-3a3f-441c-b081-dc604f6bed20', '072f635e-0ee7-461e-aa7e-1901ae3d0c5e', 'd9343856-ad27-4256-9534-4c59fa5e6422', 100, 5, 'BANK_CARD', '5538411806914853', 'SUCCESS', '2024-04-04T12:02:00.0+03:00'),
    ('d884a140-88d6-4cf4-a6a6-8dfb7e31fd96', '9ea8a8b0-004b-4ded-8a3a-edc198c89e31', '072f635e-0ee7-461e-aa7e-1901ae3d0c5e', 'd9343856-ad27-4256-9534-4c59fa5e6422', 100, 5, 'CASH', null, 'SUCCESS', '2024-04-04T12:03:00.0+03:00'),
    ('26a05239-f86b-4c45-9b03-1a24c9070a23', 'aba3e655-a8b1-4dca-a4b5-6566dd74a47e', '072f635e-0ee7-461e-aa7e-1901ae3d0c5e', 'd9343856-ad27-4256-9534-4c59fa5e6422', 100, 5, 'CASH', null, 'SUCCESS', '2024-04-04T12:04:00.0+03:00');
