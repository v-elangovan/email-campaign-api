CREATE TABLE campaign (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    sender_email VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,

    scheduled_at DATETIME NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE recipient (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    campaign_id BIGINT NOT NULL,

    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_campaign
        FOREIGN KEY (campaign_id)
        REFERENCES campaign(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_campaign_email
        UNIQUE (campaign_id, email)
);