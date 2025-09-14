FROM httpd:2.4.65

RUN apt update && apt install -y git libapr1 libapr1-dev apache2-dev

RUN git clone https://github.com/FastCGI-Archives/mod_fastcgi.git /mod_fastcgi && \
    cd /mod_fastcgi/ && \
    apxs -i -a -n fastcgi -o mod_fastcgi.so -c *.c

RUN mkdir /usr/local/apache2/fcgi-bin
