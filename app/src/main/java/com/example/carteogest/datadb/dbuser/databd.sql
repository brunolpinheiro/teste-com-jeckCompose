-- Tabela de papéis (roles)
CREATE TABLE roles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT UNIQUE
);

-- Tabela de permissões
CREATE TABLE permissoes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT UNIQUE
);

-- Relacionamento entre papéis e permissões
CREATE TABLE role_permissoes (
    role_id INTEGER,
    permissao_id INTEGER,
    PRIMARY KEY (role_id, permissao_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (permissao_id) REFERENCES permissoes(id)
);

-- Usuários
CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT,
    login TEXT UNIQUE,
    senha_hash TEXT,
    email TEXT,
    role_id INTEGER,
    ativo INTEGER DEFAULT 1,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Inserir papéis básicos
INSERT INTO roles (nome) VALUES ('Administrador'), ('Estoquista'), ('Vendedor'), ('Auditor');
