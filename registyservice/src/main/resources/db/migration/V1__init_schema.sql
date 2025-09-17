CREATE TABLE clients (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         phone VARCHAR(50) UNIQUE NOT NULL,
                         email VARCHAR(255) UNIQUE
);

CREATE TABLE apartments (
                            id BIGSERIAL PRIMARY KEY,
                            owner_id BIGINT NOT NULL,
                            address VARCHAR(500) NOT NULL,
                            rooms INT NOT NULL,
                            area DECIMAL(10,2) NOT NULL,
                            floor INT,
                            price BIGINT NOT NULL,
                            CONSTRAINT fk_apartment_owner FOREIGN KEY (owner_id) REFERENCES clients(id) ON DELETE CASCADE
);

CREATE TYPE order_type AS ENUM ('BUY', 'SELL', 'EXCHANGE');
CREATE TYPE order_status AS ENUM ('OPEN', 'MATCHED', 'CLOSED', 'CANCELLED');

CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        client_id BIGINT NOT NULL,
                        apartment_id BIGINT,
                        type VARCHAR(15) NOT NULL,
                        status VARCHAR(15) NOT NULL DEFAULT 'OPEN',
                        price_min BIGINT,
                        price_max BIGINT,
                        created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                        CONSTRAINT fk_order_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
                        CONSTRAINT fk_order_apartment FOREIGN KEY (apartment_id) REFERENCES apartments(id) ON DELETE SET NULL
);

CREATE TYPE trade_status AS ENUM ('PENDING', 'EXECUTED', 'REJECTED');

CREATE TABLE trades (
                        id BIGSERIAL PRIMARY KEY,
                        order_id BIGINT NOT NULL,
                        counter_order_id BIGINT NOT NULL,
                        status VARCHAR(15) NOT NULL DEFAULT 'PENDING',
                        created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                        CONSTRAINT fk_trade_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                        CONSTRAINT fk_trade_counter FOREIGN KEY (counter_order_id) REFERENCES orders(id) ON DELETE CASCADE
);
