CREATE INDEX idx_campaign_status
ON campaign(status);

CREATE INDEX idx_campaign_scheduled_at
ON campaign(scheduled_at);

CREATE INDEX idx_campaign_created_at
ON campaign(created_at);

CREATE INDEX idx_recipient_campaign
ON recipient(campaign_id);

CREATE INDEX idx_recipient_status
ON recipient(status);