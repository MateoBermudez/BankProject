insert into account (account_number, account_type, balance, client_id) values (1, 'Credit',  -100.00,  1);
insert into account (account_number, account_type, balance, client_id) values (2, 'Savings',  100.00,  1);
insert into transfer (amount, destination_account, origin_account, transfer_date, description) values (100.00,  '1', '2',  '2020-01-01', 'Initial deposit');