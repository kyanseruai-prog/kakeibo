-- システムデフォルトカテゴリ（支出）
INSERT INTO categories (category_name, category_type, is_system, icon, color) VALUES
('食費', 'EXPENSE', true, 'utensils', '#FF6384'),
('交通費', 'EXPENSE', true, 'bus', '#36A2EB'),
('娯楽費', 'EXPENSE', true, 'gamepad', '#FFCE56'),
('光熱費', 'EXPENSE', true, 'bolt', '#4BC0C0'),
('家賃', 'EXPENSE', true, 'home', '#9966FF'),
('医療費', 'EXPENSE', true, 'hospital', '#FF9F40'),
('通信費', 'EXPENSE', true, 'mobile', '#FFCD56'),
('その他（支出）', 'EXPENSE', true, 'ellipsis-h', '#9E9E9E')
ON CONFLICT DO NOTHING;

-- システムデフォルトカテゴリ（収入）
INSERT INTO categories (category_name, category_type, is_system, icon, color) VALUES
('給与', 'INCOME', true, 'money-bill-wave', '#4CAF50'),
('賞与', 'INCOME', true, 'gift', '#8BC34A'),
('副業収入', 'INCOME', true, 'laptop', '#66BB6A'),
('その他（収入）', 'INCOME', true, 'ellipsis-h', '#A5D6A7')
ON CONFLICT DO NOTHING;
