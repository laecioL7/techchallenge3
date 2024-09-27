Feature: Gerenciamento de Reservas

  Scenario: Fazer uma nova reserva
    Given uma reserva com ID "1", usuário com ID "1", restaurante com ID "1", horário "2024-10-10T19:00:00", e número de pessoas 4
    When o usuário faz a reserva
    Then o status da resposta deve ser 200
    And a resposta deve conter os detalhes da reserva

  Scenario: Visualizar reservas por usuário
    Given existem reservas para o usuário com ID "1"
    When o usuário visualiza as reservas pelo ID do usuário "1"
    Then o status da resposta deve ser 200
    And a resposta deve conter a lista de reservas do usuário

  Scenario: Visualizar reservas por restaurante
    Given existem reservas para o restaurante com ID "1"
    When o usuário visualiza as reservas pelo ID do restaurante "1"
    Then o status da resposta deve ser 200
    And a resposta deve conter a lista de reservas do restaurante

  Scenario: Atualizar uma reserva
    Given uma reserva com ID "1" existe
    When o usuário atualiza a reserva com ID "1" para horário "2024-10-10T20:00:00" e número de pessoas 5
    Then o status da resposta deve ser 200

  Scenario: Cancelar uma reserva
    Given uma reserva com ID "1" existe
    When o usuário cancela a reserva com ID "1"
    Then o status da resposta deve ser 200