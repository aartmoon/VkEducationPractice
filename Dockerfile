FROM postgres:14-alpine

ENV POSTGRES_DB=vkedu
ENV POSTGRES_USER=myuser
ENV POSTGRES_PASSWORD=mypassword

VOLUME ["/docker-entrypoint-initdb.d"]

EXPOSE 5432
