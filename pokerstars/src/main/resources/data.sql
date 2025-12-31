DELETE FROM poker_table;

-- Insert initial poker tables (players list empty)
INSERT INTO poker_table (id, name, small_blind, big_blind, starting_amt)
VALUES
('1', 'Beginner Table', 1, 2, 500),
('2', 'Intermediate Table', 5, 10, 1000),
('3', 'High Roller Table', 25, 50, 5000),
('4', 'VIP Table', 50, 100, 10000);