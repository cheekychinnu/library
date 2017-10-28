    drop table if exists book;
    
    drop table if exists book_catalog; 
    
    drop table if exists penalty;
    
    drop table if exists rating_and_review;
    
    drop table if exists rent;
    
    drop table if exists subscriber;
    
    drop table  if exists user;
    
    drop table  if exists watcher;
    
    drop sequence if exists hibernate_sequence;

    create sequence hibernate_sequence start with 1 increment by 1;
    
    create table book (
       id bigint not null,
        comments varchar(255),
        is_active boolean,
        is_available boolean,
        provider varchar(255),
        book_catalog_id bigint,
        primary key (id)
    );
    
    create table book_catalog (
       id bigint not null,
        author varchar(255),
        isbn varchar(255),
        name varchar(255),
        primary key (id)
    );
    
    create table penalty (
       rent_id bigint not null,
        amount double,
        book_id bigint,
        reason varchar(255),
        status varchar(255),
        type varchar(255),
        primary key (rent_id)
    );
    
    create table rating_and_review (
       book_catalog_id bigint not null,
        user_id varchar(255) not null,
        rating integer,
        review varchar(255),
        primary key (book_catalog_id, user_id)
    );
    
    create table rent (
       id bigint not null,
        actual_return_date timestamp,
        due_date timestamp,
        is_closed boolean,
        issued_date timestamp,
        user_id varchar(255),
        book_id bigint,
        primary key (id)
    );
    
    create table subscriber (
       event_type varchar(255) not null,
        user_id varchar(255) not null,
        primary key (event_type, user_id)
    );
    
    create table user (
       id varchar(255) not null,
        email_id varchar(255),
        first_name varchar(255),
        last_name varchar(255),
        password varchar(255),
        primary key (id)
    );
    
    create table watcher (
       book_catalog_id bigint not null,
        user_id varchar(255) not null,
        primary key (book_catalog_id, user_id)
    );
    
    alter table book 
       add constraint FK7y7so46b7trdgmuf6ft66bjej 
       foreign key (book_catalog_id) 
       references book_catalog;
    
    alter table penalty 
       add constraint FKkkalclgopo9jyk02wp86fq93a 
       foreign key (book_id) 
       references book;
    
    alter table rating_and_review 
       add constraint FKq6os7plmq1pc622i379tt15mq 
       foreign key (book_catalog_id) 
       references book_catalog;
    
    alter table rent 
       add constraint FKrrha5eemtxt1g2smvjf5xsuuy 
       foreign key (book_id) 
       references book;
    
    alter table watcher 
       add constraint FKnimhtgmag59k3j6ecyln87jjm 
       foreign key (book_catalog_id) 
       references book_catalog;
       
    insert into user values ('admin','vinodhinic@gmail.com','admin','admin', 'admin123');
