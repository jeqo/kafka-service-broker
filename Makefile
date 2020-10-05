all:

build:
	mvn clean package

docker-build:
	docker build -t jeqo/kafka-service-broker .

docker-push:
	docker push jeqo/kafka-service-broker

minikube-delete:
	minikube delete

minikube-start:
	minikube start

minikube-dashboard:
	minikube dashboard

k8s-prepare:
	helm repo add svc-cat https://svc-catalog-charts.storage.googleapis.com
	kubectl create namespace catalog
	helm install catalog svc-cat/catalog --namespace catalog

k8s-service-broker:
	kubectl apply -f kubernetes/service-broker.yaml
	kubectl apply -f kubernetes/service-broker-service.yaml
	kubectl apply -f kubernetes/service-broker-registration.yaml

k8s-service-account-list:
	kubectl exec \
		-n catalog kafka-service-broker \
		-- curl -sSL http://cloud-controller:13E663CA-1514-4278-A69D-84ACB4BF5B38@localhost:8080/accounts

k8s-service-account-create:
	kubectl exec \
		-n catalog kafka-service-broker \
		-- curl -sSL -X POST -H "Content-Type: application/json" -d '{ "User:118652": {"apiKey":"${CCLOUD_API_KEY}", "apiSecret":"${CCLOUD_API_SECRET}"}}' http://cloud-controller:13E663CA-1514-4278-A69D-84ACB4BF5B38@localhost:8080/accounts

PROJECT_NAME := system1

k8s-project-namespace:
	kubectl create namespace ${PROJECT_NAME}

k8s-project-topic-instance:
	kubectl apply -f examples/service-instance.yaml

k8s-project-topic-binding:
	kubectl apply -f examples/service-binding.yaml
