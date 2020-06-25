FROM alpine
COPY src/main/webapp/index.html /
CMD ["/src/main/webapp/index.html"]
