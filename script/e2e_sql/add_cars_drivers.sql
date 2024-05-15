\connect driver_service_db;

INSERT INTO cars(id, manufacturer_id, color_id, registration_number) VALUES
    ('8805899c-8fcd-4585-8ccb-1edec54a964a', 9, 7, 'E070CC-4'),
    ('6810330b-769e-4278-b050-f66371e2ee52', 1, 2, 'E635TO-6'),
    ('08ca8ae6-f39c-4093-9b2a-3aa8fe57987f', 8, 1, 'E472XH-5'),
    ('fd937a51-12ea-47aa-bb1a-68289a50111c', 3, 2, 'E391MP-4'),
    ('3193a79f-dcf5-481d-b7c9-5aeed2bb000a', 20, 12, 'E820TH-2'),
    ('f17fc567-8280-4eeb-90c4-c397cba1e342', 7, 12, '1560MP-7'),
    ('784e8eea-ba4a-43cd-bf04-96fb46ab5d8d', 14, 13, 'E020HA-7');
INSERT INTO drivers(id, name, email, card_number, car_id, is_available) VALUES
    ('a7dd0543-0adc-4ea6-9ca7-7b72065ca011', 'Johnny Doe', 'johnny.doe@yahoo.de', '1522613953683617', '8805899c-8fcd-4585-8ccb-1edec54a964a', TRUE),
    ('d1e04703-1af4-43ee-aae7-4ee06881ee59', 'John Doe', 'john.doe@mail.com', '3917881684449050', '6810330b-769e-4278-b050-f66371e2ee52', FALSE),
    ('d80e3e08-5b82-4551-9428-bc5d45e6f3f6', 'Janny Doe', 'janny.doe@yahoo.com.br', '9239176603428452', '08ca8ae6-f39c-4093-9b2a-3aa8fe57987f', TRUE),
    ('ad4225da-7a89-4290-884f-a22701c83e94', 'Jane Doe', 'jane.doe@yahoo.com.ar', '9902232414679759', 'fd937a51-12ea-47aa-bb1a-68289a50111c', FALSE),
    ('4c2b3a93-1d97-4ccf-a7b8-824daea08671', 'Darren Randolph', 'darren.randolph@virgilio.it', '8972821332297027', '3193a79f-dcf5-481d-b7c9-5aeed2bb000a', FALSE),
    ('d9343856-ad27-4256-9534-4c59fa5e6422', 'Julia Beck', 'julia.beck@libero.it', '8972821332297027', 'f17fc567-8280-4eeb-90c4-c397cba1e342', FALSE),
    ('e90dbf58-e20d-458a-9a6d-68ac2ede9c37', 'Wylder Little', 'wylder.little@gmx.de', '7003667350261330', '784e8eea-ba4a-43cd-bf04-96fb46ab5d8d', FALSE);
