# Build phase
FROM node:lts-alpine AS builder
WORKDIR /app
COPY . ./app
RUN npm install
RUN npm run build

# Production phase
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD [ "nginx", "-g", "daemon off;" ]
