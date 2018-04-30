#!/bin/bash
# $1 ENV_PATH               .env relative path for docker
# $2 DEPLOYMENT_TARGET      like 'dev' or 'live' | can be used to identify scopes or namespaces
# $3 MICROSERVICE_NAME      name of microservice

ENV="${1?Need to set ENV_PATH}"
TARGET="${2?Need to set DEPLOYMENT_TARGET}"
NAME="${3?Need to set MICROSERVICE_NAME}"

#NAMESPACE="eu-central-1"
#[ "$TARGET" == "dev" ] && NAMESPACE="${CON_IT_KUBERNETES_DEV_NAMESPACE?Need to set env CON_IT_KUBERNETES_DEV_NAMESPACE}"
#[ "$TARGET" == "live" ] && NAMESPACE="${CON_IT_KUBERNETES_LIVE_NAMESPACE?Need to set env CON_IT_KUBERNETES_LIVE_NAMESPACE}"

#: "${NAMESPACE?Need to set CON_IT_KUBERNETES_XXX_NAMESPACE according DEPLOYMENT_TARGET (2nd Argument)}"

NAMESPACE_EMPTY="empty-namespace"
#kubectl delete namespace "${NAMESPACE}"
kubectl create namespace "${NAMESPACE_EMPTY}"
echo "CON_IT_TEST_NAMESPACE_EMPTY=${NAMESPACE_EMPTY}">>${ENV}
export CON_IT_TEST_NAMESPACE_EMPTY=$NAMESPACE_EMPTY

NAMESPACE="test-namespace"
#kubectl delete namespace "${NAMESPACE}"
kubectl create namespace "${NAMESPACE}"
echo "CON_IT_TEST_NAMESPACE=${NAMESPACE}">>${ENV}
export CON_IT_TEST_NAMESPACE=$NAMESPACE

TEST_NODE="hello-node"
docker-compose build -t "consortit-docker-cme-local.jfrog.io/${TEST_NODE}" -f Dockerfile ./kubernetes-adapter/deploy/resource
cd ./kubernetes-adapter/deploy/resource
kubectl delete deployment hello-node --namespace test-namespace
sleep 2
kubectl run "${TEST_NODE}" --image="consortit-docker-cme-local.jfrog.io/${TEST_NODE}":v1 --port=8080 --namespace="${NAMESPACE}"
echo "CON_IT_TEST_NODE=${TEST_NODE}">>${ENV}
export CON_IT_TEST_NODE=$TEST_NODE
cd ../../..
