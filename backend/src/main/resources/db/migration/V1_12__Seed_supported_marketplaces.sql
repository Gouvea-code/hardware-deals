INSERT INTO stores (id, name, slug, website, active)
VALUES
    ('10000000-0000-0000-0000-000000000001', 'Mercado Livre', 'mercado-livre', 'https://www.mercadolivre.com.br', true),
    ('10000000-0000-0000-0000-000000000002', 'Amazon Brasil', 'amazon-brasil', 'https://www.amazon.com.br', true),
    ('10000000-0000-0000-0000-000000000003', 'KaBuM!', 'kabum', 'https://www.kabum.com.br', true),
    ('10000000-0000-0000-0000-000000000004', 'Magazine Luiza', 'magazine-luiza', 'https://www.magazineluiza.com.br', true),
    ('10000000-0000-0000-0000-000000000005', 'Shopee', 'shopee', 'https://shopee.com.br', true)
ON CONFLICT (slug) DO UPDATE SET
    name = EXCLUDED.name,
    website = EXCLUDED.website,
    active = EXCLUDED.active,
    updated_at = CURRENT_TIMESTAMP;
