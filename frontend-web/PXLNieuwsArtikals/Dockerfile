# Step 1: Use Node.js image to build the app
FROM node:18-alpine AS build

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy all files
COPY . .

# Build the Angular app
RUN npm run build -- --output-path=dist

# Step 2: Use NGINX to serve the app
FROM nginx:1.23-alpine

COPY nginx.conf /etc/nginx/nginx.conf

COPY ./dist/customer-apps/browser /usr/share/nginx/html


# Expose the default NGINX port
EXPOSE 80

# Start NGINX
CMD ["nginx", "-g", "daemon off;"]
