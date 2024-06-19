# Projeto de API para realização de Transferência bancaria .

## Desafio

Desenhar e desenvolver uma solução que permita que os clientes do Itau consigam realizar
Transferência entre contas. Essa solução precisa ser resiliente, ter alta disponibilidade e de fácil
evolução/manutenção 


## Diagrama de classe


## Arquitetura de solução


## Arquitetura do software

Por ser um projeto relativamente pequeno e por eu ter maior experiencia em um modelo especifico, optei por estruturar o mesmo em camadas, fazendo com que em cada pacote fique uma parte do codigo. 
- **controller** : parte de entrada (endpoints da api)
- **service** : parte que fica responsavel pela as regras de negocios
- **repository** : parte que faz comunicacao com o banco de dados (DAO's e repositories)


## Payloads

Para realizar uma transferencia 

**POST** : /transfer-api/transfer

```
  {
    "receivingClientId" : "",
    "amount" : 2.399,
    "sendingAccount" : "",
    "receivingAccount" : ""
  }
```

## Tecnologias

- Spring boot
- Java 17
- Junit
- Mockito
  
## Possiveis melhorias

- Implementacao de spring security (token/jwt/auth 2.0)


url swaqgger : http://localhost:8081/transfer-api/swagger-ui/index.html
