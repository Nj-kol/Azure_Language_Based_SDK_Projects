FROM python:3.9-alpine

LABEL Author="Nilanjan Sarkar"
LABEL E-mail="Nilanjan.Sarkar@gmail.com"
LABEL version="0.0.1"

ENV PYTHONDONTWRITEBYTECODE 1
ENV FLASK_APP "product_api"
ENV FLASK_ENV "development"
ENV FLASK_DEBUG True

# Install curl
RUN apk add --no-cache curl
RUN apk update && apk add curl && apk add gcc && apk add build-base && apk add unixodbc-dev

# Install the Microsoft ODBC driver Linux.Follow the mssql documentation: https://docs.microsoft.com/en-us/sql/connect/odbc/linux-mac/installing-the-microsoft-odbc-driver-for-sql-server?view=sql-server-ver15
RUN curl -O https://download.microsoft.com/download/e/4/e/e4e67866-dffd-428c-aac7-8d28ddafb39b/msodbcsql17_17.8.1.1-1_amd64.apk
RUN curl -O https://download.microsoft.com/download/e/4/e/e4e67866-dffd-428c-aac7-8d28ddafb39b/mssql-tools_17.8.1.1-1_amd64.apk

# Install the package(s)
RUN apk add --allow-untrusted msodbcsql17_17.8.1.1-1_amd64.apk
RUN apk add --allow-untrusted mssql-tools_17.8.1.1-1_amd64.apk

RUN mkdir /flask_product_api
WORKDIR /flask_product_api

COPY . /flask_product_api/

RUN pip install --upgrade pip && \
    pip install pipenv && \
    pipenv install --dev --system --deploy --ignore-pipfile

ADD . /flask_product_api

EXPOSE 5000

CMD flask run --host=0.0.0.0
