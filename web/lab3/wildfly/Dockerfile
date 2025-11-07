FROM quay.io/wildfly/wildfly

COPY modules/postgres /opt/jboss/wildfly/modules/org/postgresql/main
COPY data-source.cli entrypoint.sh ./

CMD ["sh", "-c", "./entrypoint.sh"]