# Projeto de API para Transferencia bancaria 

" Desenhar e desenvolver uma solução que permita que os clientes consigam realizar
Transferência entre contas. Essa solução precisa ser resiliente, ter alta disponibilidade e de fácil
evolução/manutenção "


## Arquitetura de solução
![aws-itau-test-sem(1)](https://github.com/luanvsfeo/itau-test/assets/54812655/745b6971-23fb-468f-b244-86a1ceb08abb)



## Arquitetura do software

Por ser um projeto relativamente pequeno e por eu ter maior experiencia em um modelo especifico, optei por estruturar o mesmo em camadas, fazendo com que em cada pacote fique uma parte do codigo. 
- **controller** : parte de entrada (endpoints da api)
- **service** : parte que fica responsavel pela as regras de negocios
- **repository** : parte que faz comunicação  com o banco de dados (DAO's e repositories)


## Rodando o projeto

### Passo 1 : Ambiente
Clone o projeto para a sua maquina usando o Git
```
git clone https://github.com/luanvsfeo/itau-test.git
```
```
cd itau-test
```

### Passo 2 : Construcao

Na raiz do projeto, execute um dos comandos abaixo para rodar a aplicação na sua maquina 


```
docker build . --tag=transfer-api:latest
```
```
docker run -p 8081:9091 transfer-api:latest
```
ou

```
docker-compose build
```
```
docker-compose up
```

## Payloads

### Endpoints

> Para realizar uma transferencia 

**POST** | /transfer-api/transfer

```
  {
    "receivingClientId" : "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f",
    "amount" : 40,
    "sendingAccount" : "d0d32142-74b7-4aca-9c68-838aeacef96b",
    "receivingAccount" :  "41313d7b-bd75-4c75-9dea-1f4be434007f"
  }
```
### Dicionario de dados

| campo | descrição  |
|----|-----------|
|receivingClientId | UUID do usuario que vai receber a transferencia|
|amount | Valor a ser transferido |
|sendingAccount | UUID da conta do usuario que vai realizar a transferencia |
|receivingAccount | UUID da conta do usuario que vai receber a transferencia |

> OBS: Todos os campos sao **obrigatorios** 

### Swagger
/transfer-api/swagger-ui/index.html

## Tecnologias

- Spring boot
- Java
- Swagger
- Junit
- Mockito

## Possiveis melhorias

- Implementação de Spring security (token/jwt/auth 2.0)
- Criação de uma tabela de auditoria das transferencias (DynamoDb)
- Implementação de caching (Amazon ElastiCache ou Redis)
- Migração dessa arquitetura para uma estrutura serverless (utilizando o Lambda no lugar do EC2)
  - Criação de um outro AWS Lambda para fazer a parte de autenticação (consultando no secrets manager)


## Proposta de melhoria de arquitetura (serverless)

![aws-itau-test-serverless](https://github.com/luanvsfeo/itau-test/assets/54812655/251c7014-6ac3-48e6-8301-3c5d9b3d1290)



