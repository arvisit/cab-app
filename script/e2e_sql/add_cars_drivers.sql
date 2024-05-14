\connect driver_service_db;

INSERT INTO cars(id, manufacturer_id, color_id, registration_number) VALUES
    ('8805899c-8fcd-4585-8ccb-1edec54a964a', 9, 7, 'E070CC-4'),
    ('6810330b-769e-4278-b050-f66371e2ee52', 1, 2, 'E635TO-6'),
    ('08ca8ae6-f39c-4093-9b2a-3aa8fe57987f', 8, 1, 'E472XH-5'),
    ('fd937a51-12ea-47aa-bb1a-68289a50111c', 3, 2, 'E391MP-4');
INSERT INTO drivers(id, name, email, card_number, car_id, is_available) VALUES
    ('a7dd0543-0adc-4ea6-9ca7-7b72065ca011', 'Johnny Doe', 'johnny.doe@yahoo.de', '1522613953683617', '8805899c-8fcd-4585-8ccb-1edec54a964a', TRUE),
    ('d1e04703-1af4-43ee-aae7-4ee06881ee59', 'John Doe', 'john.doe@mail.com', '3917881684449050', '6810330b-769e-4278-b050-f66371e2ee52', FALSE),
    ('d80e3e08-5b82-4551-9428-bc5d45e6f3f6', 'Janny Doe', 'janny.doe@yahoo.com.br', '9239176603428452', '08ca8ae6-f39c-4093-9b2a-3aa8fe57987f', TRUE),
    ('ad4225da-7a89-4290-884f-a22701c83e94', 'Jane Doe', 'jane.doe@yahoo.com.ar', '9902232414679759', 'fd937a51-12ea-47aa-bb1a-68289a50111c', FALSE);
