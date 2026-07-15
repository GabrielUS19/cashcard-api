CREATE TABLE tb_user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);

CREATE TABLE tb_cashcard (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount NUMERIC(19, 2) NOT NULL DEFAULT 0.00,
    user_id UUID NOT NULL,

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE
);