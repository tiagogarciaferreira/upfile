# 📁 File Storage API (S3-Compatible, MinIO)

## 📖 Sobre o projeto

API para **upload de arquivos**, permitindo:

- Upload de arquivos
- Consulta por ID
- Exclusão
- Listagem com **paginação e filtros**
- Download de arquivos
- Links de download temporários

---

## 🧰 O que foi utilizado

- Java 25
- Lombok
- MapStruct
- Apache Tika
- Hash4j
- Spring Doc Open API
- Spring Boot 4.0
- Spring Data JPA + Hibernate
- Spring Security + OAuth2 Resource Server (JWT Ed25519)
- Flyway + PostgreSQL 17
- Docker + Docker Compose (com secrets)
- Cosign (assinatura de imagens)
- MinIO(S3)
- Postman (coleção de requisições)
- Makefile (automação)
- OpenSSL + Step-CLI
- Gradle

---

## 📋 Requisitos (o que você precisa ter instalado)

| Ferramenta     | Para que serve                                                    |
|----------------|-------------------------------------------------------------------|
| Git            | Clonar o repositório                                              |
| Java 25        | Build e execução local                                            |
| IntelliJ IDEA  | Desenvolvimento e execução do projeto                             |
| Docker         | Build de imagens e execução de containers                         |
| Docker Compose | Subir o ambiente completo (app + postgres + prometheus + grafana) |
| Make           | Executar todos os comandos do Makefile                            |
| OpenSSL        | Gerar certificado TLS                                             |
| Step-CLI       | Gerar certificado autoassinado (usado nos scripts)                |
| Cosign         | Assinar e verificar imagem Docker                                 |
| jq             | Formatador JSON para saída do `verify-signature`                  |
| Postman        | Importar coleção de requisições                                   |
| Gradle         | Executar builds localmente                                        |

---

## 📥 Clonar o projeto

```bash
  git clone https://github.com/tiagogarciaferreira/upfile.git  
  cd upfile
```

---

## 🛠️ Comandos Makefile

| Comando                      | O que faz                                                                |
|------------------------------|--------------------------------------------------------------------------|
| > make setup-local           | Configura todo o ambiente local (host, certificados, chaves JWT, Cosign) |
| > make clean-local           | Remove todos os artefatos gerados localmente                             |
| > make add-host              | Adiciona api.snakes.tgfcodes.com ao /etc/hosts                           |
| > make generate-cert         | Gera certificado TLS autoassinado (PKCS12)                               |
| > make clean-cert            | Remove os arquivos de certificado gerados                                |
| > make generate-jwt-keys     | Gera par de chaves Ed25519 para JWT (formato JWK)                        |
| > make clean-jwt-keys        | Remove os arquivos de chave JWT                                          |
| > make generate-cosign-key   | Gera par de chaves Cosign para assinatura de imagem                      |
| > make clean-cosign-key      | Remove os arquivos de chave Cosign                                       |
| > make build-image           | Constrói a imagem Docker nativa (GraalVM)                                |
| > make add-image-tag         | Adiciona tag com o número da versão do projeto                           |
| > make image-push            | Envia a imagem para o registry Docker                                    |
| > make publish-image         | Executa build-image + add-image-tag + image-push em sequência            |
| > make image-signature       | Assina a imagem Docker usando Cosign (pelo digest)                       |
| > make verify-signature      | Verifica a assinatura da imagem e exibe os dados em JSON                 |
| > make sign-and-verify-image | Executa assinatura e verificação em sequência                            |
| > make compose-up            | Sobe os containers (app, postgres, prometheus, grafana)                  |
| > make compose-down          | Derruba os containers                                                    |
| > make compose-clean         | Remove containers e volumes (limpeza completa)                           |
| > make compose-recreate      | Recria o ambiente do zero (clean + up)                                   |
| > make help                  | Exibe a lista de todos os comandos com descrições                        |

---

## 🔗 Endpoints da API

### 🌐 Endpoints públicos (sem autenticação)

| Método | Endpoint                   | Descrição                                     |
|--------|----------------------------|-----------------------------------------------|
| POST   | /api/auth/login            | Autentica usuário e retorna JWT               |
| GET    | /actuator/health           | Status geral de saúde da aplicação            |
| GET    | /actuator/health/liveness  | Probe de liveness para Kubernetes/containers  |
| GET    | /actuator/health/readiness | Probe de readiness para Kubernetes/containers |
| GET    | /actuator/info             | Informações da aplicação (versão, nome, etc.) |

---

### 🔐 Endpoints Autenticados (JWT obrigatório)

Todos os endpoints abaixo requerem autenticação via token JWT válido no header `Authorization`.

| Método | Endpoint                     | Descrição                                                      |
|--------|------------------------------|----------------------------------------------------------------|
| POST   | /api/files                   | Realiza o upload de um novo arquivo                            |
| GET    | /api/files/{fileId}          | Recupera os metadados de um arquivo pelo seu identificador     |
| DELETE | /api/files/{fileId}          | Remove permanentemente um arquivo                              |
| GET    | /api/files                   | Lista arquivos com suporte a paginação, ordenação e filtros    |
| GET    | /api/files/{fileId}/download | Realiza o download direto do arquivo (streaming ou redirect)   |
| GET    | /api/files/{fileId}/link     | Gera um link temporário (presigned URL) para acesso ao arquivo |

> 🔐 Todos os endpoints da API de negócio (`/api/files/*`) exigem **Bearer Token JWT** obtido em `/api/auth/login`.  
> Os endpoints do **Actuator** e o **login** são públicos.

---

## 🚀 Orientação para rodar o projeto

### Criar arquivo `.env.local`

```bash
  cp .env.template .env.local
```

---

### ⚙️ Configuração

Configurações via variáveis de ambiente no arquivo `.env`:

#### Environment Variables

| Variável                              | Descrição                         | Exemplo              | Observações                           |
|---------------------------------------|-----------------------------------|----------------------|---------------------------------------|
| **SERVER_PORT**                       | Porta HTTP onde a aplicação sobe  | `8080`               | Deve estar liberada no host/container |
| **HOSTNAME**                          | URL base pública da aplicação     | `http://example.com` | Usado para geração de links absolutos |
| **API_VERSION**                       | Versão da API                     | `1.0`                | Útil para versionamento e headers     |
| **SPRING_SERVLET_MULTIPART_LOCATION** | Diretório temporário para uploads | `/tmp/upload`        | Garantir espaço e permissões          |

### Postgres

| Variável              | Descrição        | Exemplo       | Observações                     |
|-----------------------|------------------|---------------|---------------------------------|
| **POSTGRES_HOST**     | Host do banco    | `localhost`   | Pode ser DNS em produção        |
| **POSTGRES_PORT**     | Porta do banco   | `5432`        | Default do PostgreSQL           |
| **POSTGRES_DB**       | Nome do database | `myapp-db`    | Criado previamente              |
| **POSTGRES_USER**     | Usuário do banco | `my-user`     | Privilégios mínimos necessários |
| **POSTGRES_PASSWORD** | Senha do banco   | `my-password` | Nunca versionar                 |

### SSL

| Variável               | Descrição               | Exemplo                           | Observações                       |
|------------------------|-------------------------|-----------------------------------|-----------------------------------|
| **SSL_KEY_STORE**      | Caminho do keystore TLS | `classpath:name-tls-keystore.p12` | Pode ser file system ou classpath |
| **SSL_STORE_PASSWORD** | Senha do keystore       | `password`                        | Sensível                          |
| **SSL_KEY_ALIAS**      | Alias da chave          | `tls-alias`                       | Deve existir no keystore          |

### Spring

| Variável                           | Descrição            | Exemplo                                | Observações                  |
|------------------------------------|----------------------|----------------------------------------|------------------------------|
| **SPRING_PROFILES_ACTIVE**         | Profile ativo        | `local`                                | Controla config por ambiente |
| **SPRING_SQL_INIT_DATA_LOCATIONS** | Scripts SQL iniciais | `classpath:db/data/afterMigration.sql` | Executado após migrations    |
| **SPRING_FLYWAY_LOCATIONS**        | Local das migrations | `classpath:db/migration`               | Versionamento do schema      |

### Logs

| Variável            | Descrição               | Exemplo                        | Observações                 |
|---------------------|-------------------------|--------------------------------|-----------------------------|
| **LOG_PATH**        | Diretório de logs       | `/logs`                        | Persistir fora do container |
| **LOG_CONFIG_PATH** | Configuração do Logback | `classpath:logback-spring.xml` | Customização de appenders   |

### Security

| Variável            | Descrição         | Exemplo                         | Observações          |
|---------------------|-------------------|---------------------------------|----------------------|
| **JWT_PUBLIC_KEY**  | Chave pública JWT | `classpath:jwt-public-key.jwk`  | Validação de tokens  |
| **JWT_PRIVATE_KEY** | Chave privada JWT | `classpath:jwt-private-key.jwk` | Assinatura de tokens |

### Users

| Variável                | Descrição                        | Exemplo      | Observações               |
|-------------------------|----------------------------------|--------------|---------------------------|
| **USER_READ**           | Usuário com permissão de leitura | `user-read`  | Controle básico de acesso |
| **USER_READ_PASSWORD**  | Senha do usuário leitura         | `password`   | Sensível                  |
| **USER_WRITE**          | Usuário admin/escrita            | `user-admin` | Permissões elevadas       |
| **USER_WRITE_PASSWORD** | Senha do usuário admin           | `password`   | Sensível                  |

### MinIO (Object Storage)

| Variável                     | Descrição                      | Exemplo                   | Observações                     |
|------------------------------|--------------------------------|---------------------------|---------------------------------|
| **STORAGE_HOST**             | Host do MinIO                  | `api.upfile.tgfcodes.com` | Endpoint S3-compatible          |
| **STORAGE_PORT**             | Porta da API                   | `9000`                    | Default MinIO                   |
| **STORAGE_REGION_NAME**      | Região                         | `region-name`             | Deve bater com config do bucket |
| **STORAGE_ADMIN_USER**       | Usuário admin                  | `admin-user`              | Acesso total                    |
| **STORAGE_ADMIN_PASSWORD**   | Senha admin                    | `password`                | Sensível                        |
| **STORAGE_CONSOLE_PORT**     | Porta console web              | `9001`                    | UI do MinIO                     |
| **STORAGE_CONSOLE_ENABLE**   | Habilita console               | `true`                    | Desabilitar em produção pública |
| **STORAGE_BUCKET_NAME**      | Nome do bucket                 | `bucket-name`             | Deve existir ou ser criado      |
| **STORAGE_ACCESS_KEY**       | Access key S3                  | `access-key`              | Para acesso programático        |
| **STORAGE_SECRET_KEY**       | Secret key S3                  | `secret-key`              | Sensível                        |
| **STORAGE_UPLOAD_USERNAME**  | Usuário de upload              | `upload-username`         | Escopo restrito                 |
| **STORAGE_UPLOAD_PASSWORD**  | Senha upload                   | `upload-password`         | Sensível                        |
| **STORAGE_LINK_TTL_SECONDS** | Expiração de links em segundos | `600`                     | Valor de expiração              |

---

### Executar setup e subir ambiente

```bash
  make setup-local
  make compose-up
```

---

### 🌐 Acesso à API

API estará disponível em:

- **Host:** api.upfile.tgfcodes.com
- **Porta:** 8443
- **URL completa:** https://api.upfile.tgfcodes.com:8443

---

## 📬 Importar coleção do Postman

1. Abra o Postman
2. Vá em **Import → Upload Files**
3. Selecione os arquivos da pasta `postman`:

- `Upfile.postman_collection.json`
- `API Upfile - Local.postman_environment.json`

4. No seletor de **environment**, escolha:  
   `API Upfile - Local`

✅ Pronto. As requisições já estarão configuradas.

---

## 👤 Usuários de teste (pré-carregados)

Os seguintes usuários já existem no banco (via `seed_tb_users.sql`).  
As senhas estão armazenadas como hash (**Argon2id**), mas os valores em texto claro para autenticação são:

| Usuário (login) | Senha (texto claro)              | O que pode fazer                            |
|-----------------|----------------------------------|---------------------------------------------|
| user_read       | dLy87K594m59tDR18j4Uo7qX6VxguhT0 | Consultar arquivos (GET)                    |
| user_write      | AW4PuyBbSZxvTyYJXJADHj2K8RBTf63s | Criar, baixar, pesquisar e deletar arquivos |

---

Use essas credenciais no endpoint:

> POST /api/auth/login

Para obter o **JWT** e acessar os demais endpoints protegidos.

⚠️ Estas senhas são **exclusivas para ambiente local de teste**.

--- 

## 📘 Documentação Swagger

A documentação interativa da API (OpenAPI) pode ser
acessada [aqui](https://api.upfile.tgfcodes.com:8443/swagger-ui/index.html).

---

O Swagger está disponível apenas quando a aplicação está em execução.

Para testar endpoints protegidos:

- É necessário estar autenticado (Bearer Token JWT)