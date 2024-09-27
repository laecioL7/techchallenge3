Feature: Review Management

  Scenario: Submeter uma avaliação com sucesso
    Given que o usuário com ID "1" existe
    And o restaurante com ID "1" existe
    When o usuário submete uma avaliação com nota "5" e comentário "Comida e serviço excelentes!"
    Then o status da resposta deve ser "200"
    And a mensagem da resposta deve ser "Avaliação realizada com sucesso"