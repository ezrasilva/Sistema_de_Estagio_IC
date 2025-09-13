## Sistema de Classificados para Estágios e Iniciação Científica

Este é um sistema em Java para gerenciar e conectar alunos a vagas de estágio e projetos de iniciação científica (IC). O projeto foi desenvolvido para simular uma plataforma onde empresas e professores podem publicar oportunidades, e alunos podem buscar e se candidatar a elas.

### 📜 Funcionalidades Principais

* **Autenticação de Usuário**: Suporte a cadastro e login para diferentes tipos de usuários: **Alunos**, **Empresas** e **Professores**.
* **Gestão de Vagas**: Empresas e Professores podem publicar vagas de estágio ou IC, fornecendo detalhes como título e descrição.
* **Busca e Candidatura**: Alunos podem buscar vagas disponíveis e se candidatar a elas.
* **Visualização de Candidatos**: Publicadores de vagas podem listar os alunos que se candidataram a suas oportunidades.
* **Visualização de Perfil**: Cada usuário pode exibir seu perfil com informações específicas do seu tipo.
* **Filtragem de Vagas**: As vagas podem ser filtradas por tipo (Estágio ou IC) ou por palavra-chave no título.

### 💻 Tecnologias Utilizadas

* **Linguagem**: Java
* **Banco de Dados**: SQLite
* **Persistência**: JDBC (Java Database Connectivity)

### 📦 Estrutura do Projeto

A estrutura do projeto segue as convenções de pacotes do Java para melhor organização e modularidade:

```
Sys_IC/
├── src/
│   └──├── model/      # Classes de entidades (Aluno, Vaga, Usuario, etc.)
│      ├── dao/        # Classes de acesso a dados (DAO)
│      ├── db/         # Classes de gerenciamento do banco de dados
│      ├── util/       # Classes utilitárias (filtros, exceções)
│      └── SistemaClassificados.java # Classe principal do programa
└── lib/
└── sqlite-jdbc-3.50.3.0.jar  # Driver JDBC do SQLite

```
### 🚀 Como Usar

#### Pré-requisitos
* Java Development Kit (JDK) 8 ou superior.

#### Configuração e Execução

1.  **Clone o Repositório:**
    ```bash
    git clone [https://github.com/ezrasilva/Sistema_de_Estagio_IC.git](https://github.com/ezrasilva/Sistema_de_Estagio_IC.git)
    cd Sistema_de_Estagio_IC
    ```
2.  **Compile o Projeto:** Compile todos os arquivos `.java` do projeto, incluindo o driver JDBC no classpath.
    ```bash
    javac -cp .:Sys_IC/lib/sqlite-jdbc-3.50.3.0.jar Sys_IC/*.java
    ```
3.  **Seede o Banco de Dados (Opcional):** Para iniciar com dados de teste (usuários, vagas e candidaturas falsas), execute a classe `DatabaseSeeder`.
    * **Importante**: Exclua o arquivo `classificados.db` se ele já existir, para que o seeder crie um novo com a estrutura correta.
    ```bash
    java -cp .:Sys_IC/lib/sqlite-jdbc-3.50.3.0.jar DatabaseSeeder
    ```
4.  **Execute a Aplicação Principal:** Inicie o sistema a partir da classe `SistemaClassificados`.
    ```bash
    java -cp .:Sys_IC/lib/sqlite-jdbc-3.50.3.0.jar SistemaClassificados
    ```

### 🤝 Contribuições

Sinta-se à vontade para contribuir com este projeto. Relate bugs, sugira novas funcionalidades ou envie pull requests.

---

### 📄 Licença

Este projeto está licenciado sob a licença **MIT**.