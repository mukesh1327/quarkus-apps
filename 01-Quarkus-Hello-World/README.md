# Java Quarkus hello world - Noventiq value point

## Testing in local

./mvnw test

<!-- registry.redhat.io/rh-syft-tech-preview/syft-rhel9@sha256:15ed82f0b5311a570ccb8ea02135d9776c6d61e545c51b256b3fc5b5db20ba67 # 1.29.0 -->
<!-- https://github.com/anchore/syft/releases/ -->
syft dir:. -o cyclonedx-json > sbom.cdx.json


<!-- docker.io/anchore/grype:latest -->
<!-- https://github.com/anchore/grype/releases -->
grype sbom:sbom.cdx.json -o table --fail-on high



__A demo hello world java quarkus application__

___Running on port :___ 8080

___Endpoints :___   
    /

___Environment variables :___   
APP_TITLE=string


## How to run in local (Use any one)

<u>**_Run as JVM (Java JDK)_**</u>  
> mvn quakus:dev

<u>**_Run as native (Requires podman or docker and GraalVM runtimes)_**</u>  
> mvn quakus:dev -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=podman

## Run as container (Use any one)

<u>**_Run as JVM_**</u>   

> mvn package

> podman build -t quarkus-jvm-hello:v1.0 -f ./src/main/docker/Dockerfile.jvm .

> podman run -d -p 8080:8080 --name quarkus-jvm-hello quarkus-jvm-hello:v1.0

<u>**_Run as Native_** </u>

> mvn package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=podman

> podman build -t quarkus-native-hello:v1.0 -f ./src/main/docker/Dockerfile.native .

> podman run -d -p 8080:8080 --name quarkus-native-hello quarkus-native-hello:v1.0

<u>**_Run as Native-micro_**</u>

> mvn package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=podman

> podman build -t quarkus-nativemicro-hello:v1.0 -f ./src/main/docker/Dockerfile.native-micro .

> podman run -d -p 8080:8080 --name quarkus-nativemicro-hello quarkus-nativemicro-hello:v1.0

## Deploy in k8s-ocp (Use any one)

<u>**_Run as JVM_**</u>  

> oc apply -f ./k8s-manifests/jvm -n demo-namespace

<u>**_Run as Native_** </u>

> oc apply -f ./k8s-manifests/native -n demo-namespace

<u>**_Run as Native-micro_**</u>

> oc apply -f ./k8s-manifests/native-micro -n demo-namespace


## Test image

<!-- https://mirror.openshift.com/pub/rhacs/assets/latest/bin/Windows/ -->

