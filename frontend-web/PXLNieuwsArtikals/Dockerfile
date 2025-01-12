FROM node:18-alpine AS build

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build -- --output-path=dist

FROM nginx:1.23-alpine

COPY nginx.conf /etc/nginx/nginx.conf

COPY ./dist/customer-apps/browser /usr/share/nginx/html


EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
