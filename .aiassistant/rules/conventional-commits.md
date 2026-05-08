---
apply: always
---

---
apply: always
---

# Regras de Conventional Commits

## Visão Geral

Todas as mensagens de commit devem seguir a especificação **Conventional Commits 1.0.0**.
O objetivo é manter um histórico de commits legível, semântico e compatível com geração automática de changelogs e versionamento semântico (SemVer).

---

## Formato da Mensagem

```
<tipo>[escopo opcional]: <descrição>

[corpo opcional]

[rodapé(s) opcional(is)]
```

### Regras obrigatórias

- A mensagem de commit **deve** ter um tipo e uma descrição.
- O tipo **deve** ser em letras minúsculas.
- A descrição **não deve** terminar com ponto final.
- A descrição **deve** ser escrita no **imperativo presente** (ex: `adiciona`, `corrige`, `remove`).
- O separador entre tipo/escopo e descrição **deve** ser `: ` (dois-pontos seguido de espaço).
- O corpo e os rodapés são **separados da descrição por uma linha em branco**.
- Cada rodapé deve seguir o formato: `Token: valor` ou `Token #valor`.

---

## Tipos Permitidos

| Tipo | Uso |
|------|-----|
| `feat` | Adição de nova funcionalidade (gera incremento de versão **MINOR**) |
| `fix` | Correção de bug (gera incremento de versão **PATCH**) |
| `docs` | Alterações apenas em documentação |
| `style` | Formatação, ponto-e-vírgula ausente, etc. (sem mudança de lógica) |
| `refactor` | Refatoração de código sem adição de feature ou correção de bug |
| `perf` | Melhoria de performance |
| `test` | Adição ou correção de testes |
| `build` | Mudanças no sistema de build ou dependências externas (ex: Maven, npm) |
| `ci` | Mudanças em arquivos e scripts de CI/CD (ex: GitHub Actions, Jenkins) |
| `chore` | Tarefas de manutenção que não alteram código de produção ou testes |
| `revert` | Reversão de um commit anterior |

---

## Escopo (Opcional)

O escopo fornece contexto adicional sobre **qual parte do sistema** foi alterada.
Deve ser escrito entre parênteses, logo após o tipo.

**Exemplos de escopo:**

```
feat(auth): adiciona autenticação via OAuth2
fix(pagamento): corrige cálculo de desconto para cupons expirados
refactor(usuario): extrai lógica de validação para classe dedicada
```

> Use escopos consistentes em todo o projeto. Defina os escopos válidos no `CONTRIBUTING.md`.

---

## Breaking Changes (Mudanças Incompatíveis)

Breaking changes geram incremento de versão **MAJOR** no SemVer.

Existem **duas formas** de indicar breaking change:

### 1. Ponto de exclamação após o tipo/escopo

```
feat!: remove suporte ao endpoint legado de autenticação
feat(api)!: altera contrato de resposta do endpoint /usuarios
```

### 2. Rodapé `BREAKING CHANGE`

```
feat(api): adiciona paginação obrigatória nos endpoints de listagem

BREAKING CHANGE: o parâmetro `page` agora é obrigatório em todas as
requisições GET de listagem. Requisições sem este parâmetro retornarão 400.
```

> Ambas as formas podem ser combinadas.

---

## Corpo da Mensagem (Opcional)

Use o corpo para explicar **o quê** e **por quê**, não **como**.

- Separado da descrição por **uma linha em branco**.
- Pode conter múltiplos parágrafos.
- Cada linha deve ter no máximo **72 caracteres**.

```
fix(cache): corrige condição de corrida no carregamento de dados

O mecanismo anterior de cache não era thread-safe em ambientes com
múltiplas requisições concorrentes, causando dados inconsistentes.

A solução utiliza ConcurrentHashMap e sincronização no bloco de
atualização para garantir atomicidade.
```

---

## Rodapés (Opcional)

Os rodapés ficam após o corpo, separados por linha em branco.

### Formatos válidos

```
Reviewed-by: João Silva <joao@empresa.com>
Refs: #123
Closes: #456
Co-authored-by: Maria Souza <maria@empresa.com>
BREAKING CHANGE: descrição da mudança incompatível
```

> `BREAKING CHANGE` é o único token de rodapé com semântica especial no SemVer.

---

## Exemplos Completos

### Feat simples

```
feat(relatorio): adiciona exportação de relatórios em PDF
```

### Fix com corpo

```
fix(login): corrige redirecionamento após sessão expirada

Usuários eram redirecionados para a home ao invés da página de login
quando a sessão expirava durante uma requisição autenticada.
```

### Feat com breaking change no rodapé

```
feat(usuario): substitui campo `nome` por `nome_completo` na API

BREAKING CHANGE: o campo `nome` foi removido do payload de resposta
do endpoint GET /usuarios/{id}. Use `nome_completo` no lugar.
```

### Breaking change com `!` e corpo

```
refactor(auth)!: migra autenticação para JWT stateless

Remove a dependência de sessões no servidor. Todos os clientes devem
atualizar o mecanismo de autenticação para utilizar Bearer Token.

BREAKING CHANGE: sessões baseadas em cookie não são mais suportadas.
Refs: #789
```

### Revert

```
revert: feat(pagamento): adiciona integração com gateway XYZ

Reverte commit abc1234 por instabilidade identificada em produção.
```

### Chore

```
chore(deps): atualiza dependências para versões estáveis
```

---

## Regras de Validação (Resumo)

| Regra | Obrigatório |
|-------|-------------|
| Tipo válido e em minúsculas | ✅ |
| Separador `: ` entre tipo e descrição | ✅ |
| Descrição no imperativo, sem ponto final | ✅ |
| Linha em branco entre header e corpo | ✅ (quando há corpo) |
| Linha em branco entre corpo e rodapé | ✅ (quando há rodapé) |
| Breaking change declarado com `!` ou `BREAKING CHANGE:` | ✅ (quando aplicável) |
| Linhas do corpo com até 72 caracteres | ✅ |

---

## Anti-padrões a Evitar

```
# ❌ Sem tipo
corrige bug no login

# ❌ Tipo inválido
update: ajusta configuração do banco

# ❌ Descrição no passado
fix: corrigiu o cálculo de frete

# ❌ Descrição com ponto final
feat: adiciona campo de busca.

# ❌ Sem espaço após os dois-pontos
fix:corrige erro de validação

# ❌ Descrição vaga
fix: correção

# ❌ Tipo em maiúsculas
FEAT: adiciona relatório mensal
```

---

## Referências

- [Conventional Commits 1.0.0](https://www.conventionalcommits.org/pt-br/v1.0.0/)
- [Semantic Versioning 2.0.0](https://semver.org/lang/pt-BR/)
- [Angular Commit Message Guidelines](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#commit)
