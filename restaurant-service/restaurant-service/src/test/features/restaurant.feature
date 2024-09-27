Feature: Gerenciamento de Restaurantes

  Scenario: Registrar um novo restaurante
    Given um restaurante com nome "Restaurante Teste", localização "Rua Teste, 123", tipo de cozinha "Italiana", horário de funcionamento "10:00-22:00", e capacidade 100
    When o usuário registra o restaurante
    Then o status da resposta deve ser 200
    And a resposta deve conter os detalhes do restaurante

  Scenario: Buscar restaurantes por localização e tipo de cozinha
    Given existem restaurantes com localização "Rua Teste, 123" e tipo de cozinha "Italiana"
    When o usuário busca por restaurantes com localização "Rua Teste, 123" e tipo de cozinha "Italiana"
    Then o status da resposta deve ser 200
    And a resposta deve conter a lista de restaurantes

  Scenario: Buscar todos os restaurantes
    Given existem restaurantes
    When o usuário busca por todos os restaurantes
    Then o status da resposta deve ser 200
    And a resposta deve conter a lista de todos os restaurantes

  Scenario: Buscar restaurantes por nome
    Given existem restaurantes com nome "Restaurante Teste"
    When o usuário busca por restaurantes com nome "Restaurante Teste"
    Then o status da resposta deve ser 200
    And a resposta deve conter a lista de restaurantes com o nome "Restaurante Teste"