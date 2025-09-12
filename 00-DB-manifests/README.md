# POSTGRESQL Database Manifests


## For Docker / Podman

podman compose -f 00-DB-manifests/postgresql-container/compose.yaml up -d

## For k8s / OCP

```shell script
export $OCP_PROJECT_NAME=demo-project

oc new-project $OCP_PROJECT_NAME

oc create -f 00-DB-manifests/postgresql-k8s-OCP -n $OCP_PROJECT_NAME
```

## Note

Image used is : registry.redhat.io/rhel9/postgresql-16

For more info https://catalog.redhat.com/en/software/containers/rhel9/postgresql-16/657b03866783e1b1fb87e142#overview


<!-- # PostgreSQL image for OpenShift.
# Volumes:
#  * /var/lib/pgsql/data   - Database cluster for PostgreSQL
# Environment:
#  * $POSTGRESQL_USER     - Database user name
#  * $POSTGRESQL_PASSWORD - User's password
#  * $POSTGRESQL_DATABASE - Name of the database to create
#  * $POSTGRESQL_ADMIN_PASSWORD (Optional) - Password for the 'postgres'
#                           PostgreSQL administrative account -->