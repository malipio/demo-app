FROM node:12.19.0-alpine3.9

RUN npm install -g heroku
RUN heroku plugins:install java

ENTRYPOINT ["/usr/local/bin/heroku"]