## 스프링 DB 1편

### DB

- h2 database

- 초기 세팅

```sql
drop table member if exists cascade;
  create table member (
      member_id varchar(10),
      money integer not null default 0,
      primary key (member_id)
  );
  insert into member(member_id, money) values ('hi1',10000);
  insert into member(member_id, money) values ('hi2',20000);
```

### 강의

- 스프링 고급편이랑 연관되어 있는 부분이 좀 있어서 고급 편을 듣고 이해 안된 부분을 보는 것도 좋을 듯 하다
  (AOP, 템플릿 콜백 패턴)
