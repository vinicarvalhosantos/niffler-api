# niffler-api

Este projeto tem a missão de receber um objeto de mensagem vinda de um chatbot da twitch e enviar para uma fila para que ela seja analisada e debitar um saldo de pontos para o usuário de acordo com a mensagem..

- [Recursos](#recursos)
    - [Documentação APIs](#documentação-apis)
- [Desenvolvimento](#desenvolvimento)
    - [Requisitos](#requisitos)
    - [Instalação](#instalação)
        - [Docker](#docker-compose)
    - [Configuração](#configuração)
    - [Testes](#Testes)

### Documentação APIs

Para a documentação é utilizado o [Swagger](https://swagger.io/). Ferramenta que provê interface para testes.

Por padrão a documentação está disponível no endpoint `/api/swagger-ui.html#/`.

### Catálogo de erros

| Erro | Descrição      | Ocorre quando                                                    |
|------|----------------|------------------------------------------------------------------|
| 200  | Sucesso        | Quando os dados enviados foram aceitos e seguiu o fluxo experado |
| 400  | Bad Request    | Os dados enviados no request estão inválidos                     |
| 500  | Internal Error | Acontece um erro interno no módulo                               |

## Desenvolvimento

### Requisitos

```

* Maven
* Docker
* Docker Compose
* Mysql
* RabbitMq

```

### Instalação

#### Docker compose:

Acessar a pasta raiz do projeto e executar:

```

https://docs.docker.com/compose/install/
docker-compose up -d

```

### Configuração

Lista de variáveis de ambiente necessárias para a execução da aplicação

| Variável          | Descrição                                                                    |   Tipo   | Obrigatório | Valor Padrão |
|-------------------|------------------------------------------------------------------------------|:--------:|:-----------:|:------------:|
| RABBITMQ_HOST     | Host para acesso ao RabbitMq                                                 |  Texto   |     Não     |   niffler    |
| RABBITMQ_PASSWORD | Senha para acesso ao RabbitMq                                                |  Texto   |     Não     |   niffler    |
| RABBITMQ_PORT     | Porta para acesso ao RabbitMq                                                | Numérico |     Não     |     5672     |
| RABBITMQ_USERNAME | Usuário para acesso ao RabbitMq                                              |  Texto   |     Não     |   niffler    |
| JWT_ISSUER        | Issuer do JWT para validar um token de autenticação                          |  Texto   |     Sim     |      -       |
| JWT_SECRET        | Secret criptografado em base 64 do JWT para validar um token de autenticação |  Texto   |     Sim     |      -       |


> Path da API local:
>> localhost:3000/api
>
> Rotas:
>> /v1/user-message/analyse

### Testes

```bash
# unit tests
$ mvn package

```

