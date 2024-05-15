\connect rides_service_db;

INSERT INTO promo_codes(id, keyword, discount_percent, is_active) VALUES
    (1, 'PAIN49', 49, TRUE),
    (2, 'PAIN49', 49, FALSE),
    (3, 'BRILLIANT10', 10, TRUE),
    (4, 'RICE23', 23, FALSE),
    (5, 'DIAMOND10', 10, TRUE);

SELECT nextval('promo_codes_id_seq');
SELECT nextval('promo_codes_id_seq');
SELECT nextval('promo_codes_id_seq');
SELECT nextval('promo_codes_id_seq');
SELECT nextval('promo_codes_id_seq');

INSERT INTO rides(id, initial_cost, final_cost, passenger_id, driver_id, promo_code_id, status, payment_method, is_paid, start_address, destination_address, passenger_score, driver_score, book_ride, cancel_ride, accept_ride, begin_ride, end_ride, finish_ride) VALUES
    ('69b509c6-20fe-4b70-a07e-31e52ea05a54', 100, 100, '072f635e-0ee7-461e-aa7e-1901ae3d0c5e', null, null, 'BOOKED', 'BANK_CARD', FALSE, 'Bramcote Grove, 42', 'Llewellyn Street, 35', null, null, '2024-04-04T12:00:00.0+03:00', null, null, null, null, null),
    ('a5b0a1f9-f45d-4287-bbad-ea6253976d0d', 100, 100, '3c4e9fdf-f9c1-4a59-8bc0-c3758151f7e0', '4c2b3a93-1d97-4ccf-a7b8-824daea08671', null, 'ACCEPTED', 'BANK_CARD', FALSE, 'Bramcote Grove, 42', 'Llewellyn Street, 35', null, null, '2024-04-04T12:00:00.0+03:00', null, '2024-04-04T12:02:00.0+03:00', null, null, null),
    ('48c14ff2-f020-473a-a98b-651f203c3843', 100, 100, '51334f37-482c-4498-ad0f-122929edc0ff', 'd80e3e08-5b82-4551-9428-bc5d45e6f3f6', null, 'ACCEPTED', 'BANK_CARD', FALSE, 'Bramcote Grove, 42', 'Llewellyn Street, 35', null, null, '2024-04-04T12:00:00.0+03:00', null, '2024-04-04T12:02:00.0+03:00', null, null, null),
    ('72b20027-23d1-4651-865a-a2f7cd74fdb6', 100, 100, '2b6716c3-0c8d-4a3d-90f5-49ebcc2b77d8', 'e90dbf58-e20d-458a-9a6d-68ac2ede9c37', null, 'BEGIN_RIDE', 'BANK_CARD', FALSE, 'Bramcote Grove, 42', 'Llewellyn Street, 35', null, null, '2024-04-04T12:00:00.0+03:00', null, '2024-04-04T12:02:00.0+03:00', '2024-04-04T12:03:00.0+03:00', null, null),
    ('acd6f53f-a19b-4109-9b6f-1d15814db2f1', 100, 100, '2b6716c3-0c8d-4a3d-90f5-49ebcc2b77d8', 'e90dbf58-e20d-458a-9a6d-68ac2ede9c37', null, 'BEGIN_RIDE', 'CASH', FALSE, 'Bramcote Grove, 42', 'Llewellyn Street, 35', null, null, '2024-04-04T12:00:00.0+03:00', null, '2024-04-04T12:02:00.0+03:00', '2024-04-04T12:03:00.0+03:00', null, null),
    ('ffe34487-dfa3-4660-96dc-ed108e06ab77', 100, 100, 'deecaeef-454b-487d-987c-54df212385b3', 'd9343856-ad27-4256-9534-4c59fa5e6422', null, 'END_RIDE', 'CASH', FALSE, 'Bramcote Grove, 42', 'Llewellyn Street, 35', null, null, '2024-04-04T12:00:00.0+03:00', null, '2024-04-04T12:02:00.0+03:00', '2024-04-04T12:03:00.0+03:00', '2024-04-04T12:04:00.0+03:00', null),
    ('942d8839-7a16-4dfa-9443-179220135b3a', 100, 100, 'deecaeef-454b-487d-987c-54df212385b3', 'd9343856-ad27-4256-9534-4c59fa5e6422', null, 'END_RIDE', 'BANK_CARD', FALSE, 'Bramcote Grove, 42', 'Llewellyn Street, 35', null, null, '2024-04-04T12:00:00.0+03:00', null, '2024-04-04T12:02:00.0+03:00', '2024-04-04T12:03:00.0+03:00', '2024-04-04T12:04:00.0+03:00', null),
    ('b4de939f-3a3f-441c-b081-dc604f6bed20', 100, 100, 'deecaeef-454b-487d-987c-54df212385b3', 'd9343856-ad27-4256-9534-4c59fa5e6422', null, 'END_RIDE', 'BANK_CARD', TRUE, 'Bramcote Grove, 42', 'Llewellyn Street, 35', null, null, '2024-04-04T12:00:00.0+03:00', null, '2024-04-04T12:02:00.0+03:00', '2024-04-04T12:03:00.0+03:00', '2024-04-04T12:04:00.0+03:00', null),
    ('9ea8a8b0-004b-4ded-8a3a-edc198c89e31', 100, 100, '072f635e-0ee7-461e-aa7e-1901ae3d0c5e', '4c2b3a93-1d97-4ccf-a7b8-824daea08671', null, 'FINISHED', 'BANK_CARD', TRUE, 'Bramcote Grove, 42', 'Llewellyn Street, 35', null, null, '2024-04-04T12:00:00.0+03:00', null, '2024-04-04T12:02:00.0+03:00', '2024-04-04T12:03:00.0+03:00', '2024-04-04T12:04:00.0+03:00', '2024-04-04T12:05:00.0+03:00'),
    ('aba3e655-a8b1-4dca-a4b5-6566dd74a47e', 100, 100, '072f635e-0ee7-461e-aa7e-1901ae3d0c5e', '19ee7917-8e48-4b3e-8b21-28c3d1e53ca4', null, 'FINISHED', 'BANK_CARD', TRUE, 'Bramcote Grove, 42', 'Llewellyn Street, 35', 4, 5, '2024-04-04T12:00:00.0+03:00', null, '2024-04-04T12:02:00.0+03:00', '2024-04-04T12:03:00.0+03:00', '2024-04-04T12:04:00.0+03:00', '2024-04-04T12:05:00.0+03:00');
