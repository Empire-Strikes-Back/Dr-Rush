FROM adoptopenjdk/openjdk16:jdk-16.0.2_7-ubuntu AS jar

RUN apt-get update && apt-get install -y sudo git

# clojure
RUN curl -O https://download.clojure.org/install/linux-install-1.11.1.1113.sh && \
    chmod +x linux-install-1.11.1.1113.sh && \
    sudo ./linux-install-1.11.1.1113.sh

# nodejs
RUN curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash - && \
    sudo apt-get install -y nodejs

WORKDIR /avatar
COPY package.json .
RUN npm i
COPY deps.edn .
RUN clojure -A:Moana -Stree
COPY . .
RUN bash avatar.sh release

FROM nginx:1.21.6

WORKDIR /avatar
COPY --from=jar /avatar/out /usr/share/nginx/html
RUN ls /usr/share/nginx/html