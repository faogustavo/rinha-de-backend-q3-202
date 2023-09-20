# Rinha de Backend Q3/2023 - Kotlin

De julho a agosto de 2023, foi lançado um desafio chamado "Rinha de backend". A ideia era implementar uma API rest
baseada na especificação definida [Nesse Link](https://github.com/zanfranceschi/rinha-de-backend-2023-q3/blob/main/INSTRUCOES.md).
A idea principal seria realizar um série de stress-testes na API, para verificar se quais lingaguns/frameworks aguntariam
a carga definida.

Infelizmente, quando tomei conhecimento sobre o evento já havia acabado. Porém, sendo um programador com foco em desenvolimento
de applicativos pra Android e iOS, decidi implementar a API em Kotlin, utilizando o framework Kotlin para aprender um pouco sobre
desenvolvimento backend.

## Tecnologias utilizadas

- Linguagem: [Kotlin](https://kotlinlang.org/)
  - Versão: 1.9.0
  - Plataforma: [JVM](https://kotlinlang.org/docs/jvm-overview.html)
  - Trabalho Assíncrono: [Kotlinx.Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
  - Serialização: [Kotlinx.Serialization](https://github.com/Kotlin/kotlinx.serialization/)
  - Datas: [Kotlinx.Datetime](https://github.com/Kotlin/kotlinx-datetime)
- Framework: [Ktor](https://ktor.io/)
  - Engine: [CIO](https://ktor.io/docs/engines.html)
  - Serialização: [Kotlinx.Serialization](https://ktor.io/docs/serialization.html)
  - Tratamento de erros: [Ktor.StatusPages](https://ktor.io/docs/status-pages.html)
- Banco de dados: [PostgreSQL](https://www.postgresql.org/)
  - ORM: [Exposed](https://github.com/JetBrains/Exposed)
  - Connection Pool: [HikariCP](https://github.com/brettwooldridge/HikariCP)
- Cache: [Redis](https://redis.io/)

## Rodando o projeto

### Requisitos:
- Java 17
- PostgreSQL + Redis or Docker

### Comandos

(Opcional) Caso não tenha o PostgreSQL e Redis instalados, você pode subir o `docker-compose-dev.yml` que irá subir os 
containers para os dois serviços:

```shell
docker-compose -f docker-compose-dev.yml up -d
```

Assim que tiver as dependencias rodando, basta rodar o projeto executando o seguinte comando:

```shell
./gradlew run
```

Por padrão o projeto irá rodar na porta `8080`, porém você pode alterar a porta por meio de uma variável de ambiete.


### Build

Para gerar a image final que será rodada no desafio, você pode realizar a build da Dockerfile que existe na raiz do projeto:

```shell
docker build -t rinha-de-backend-q3-2023 .
```

E para subir o ambiente conforme solicitado pelo desafio, basta rodar o `docker-compose-local.yml`:

```shell
docker-compose -f docker-compose-local.yml up
```

O projeto também possui uma pipeline que compila uma nova versão a cada push na branch `main`, e publica ela no Github 
Registry, podendo ser acessada pelo `docker-compose.yml`.

```shell
docker-compose up
```

