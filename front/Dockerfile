### STAGE 1: Build ###
FROM node:8-alpine as builder

RUN npm set progress=false && npm config set depth 0
RUN mkdir /web
WORKDIR /web
COPY ./src/ ./src/
COPY ./e2e ./e2e/
COPY ./*.json ./
RUN npm config set registry http://registry.npmjs.org/
RUN npm i
RUN $(npm bin)/ng build --prod --build-optimizer
RUN ls /web/dist/front

### STAGE 2: Setup ###
FROM nginx:1.13.3-alpine

COPY nginx/default.conf /etc/nginx/conf.d/

RUN rm -rf /usr/share/nginx/html/*

COPY --from=builder /web/dist/front/ /usr/share/nginx/html/

CMD ["nginx", "-g", "daemon off;"]
