#!/usr/bin/env sh

heroku plugins:install java
exec /usr/local/bin/heroku $@