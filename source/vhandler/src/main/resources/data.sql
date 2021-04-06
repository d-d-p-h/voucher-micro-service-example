DROP TABLE IF EXISTS voucher_hist;

CREATE TABLE voucher_hist (
id IDENTITY PRIMARY KEY,
phone_num VARCHAR (15) NOT NULL,
voucher_cd VARCHAR (20),
voucher_typ VARCHAR (20) NOT NULL ,
received_by VARCHAR (5),
creat_by VARCHAR (20),
updt_by VARCHAR (20),
creat_dt DATE,
updt_dt DATE
);
