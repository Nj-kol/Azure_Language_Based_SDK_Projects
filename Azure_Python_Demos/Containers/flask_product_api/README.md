# Flask REStful API demo


## Azure SQL DB Set up

```
CREATE TABLE product (
id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
name VARCHAR(100),
description VARCHAR(200),
price FLOAT,
qty INT,
UNIQUE (name)
);
```

## Running the application locally

**Set up python Environment**

```bash
pipenv install
pipenv shell
```


```
cd flask_product_api
export FLASK_APP=product_api
export FLASK_ENV=development
flask run
```

## URLs

```
POST http://127.0.0.1:5000/product
GET http://127.0.0.1:5000/product
PUT http://127.0.0.1:5000/product/<product_id>
DELETE http://127.0.0.1:5000/product/<product_id>
```

**Swagger UI**

http://127.0.0.1:5000/apidocs/index.html 

# Run as a Azure Container Instance

## Push Docker image to Azure Container Registry

```
// Build docker image
docker build -t flask_product_api .

docker login njkolcontainerregistry.azurecr.io

docker tag flask_product_api njkolcontainerregistry.azurecr.io/flask_product_api

docker push njkolcontainerregistry.azurecr.io/flask_product_api
```

## Start a container instance

* It is is best to do this from the Portal
* Name the container as `njkolproducts` and expose port 5000, then the sire could be seen at `njkolproducts.centralindia.azurecontainer.io:5000/apidocs/index.html/`


# Deploy to Azure Kubernetes Service (AKS)

**Point local kubectl to Azure**

```bash
az aks get-credentials \
--resource-group njkol-analytics \
--name njkolk8s \
--file ${HOME}/azureconfig

export KUBECONFIG=${HOME}/azureconfig

# Check
kubectl get nodes

kubectl get pods --all-namespaces
```

**Deploy app on Azure Kubernetes Service(AKS)**

```bash
kubectl apply -f deploy/


# Housekeeping
kubectl get po -o wide
kubectl logs flask-product-api-59857c865c-lqxzr -f
kubectl exec -it flask-product-api-59857c865c-9g2j2 bash
kubectl get deploy
kubectl get svc
```

**UI**

http://20.204.237.204/apidocs/

![flask on AKS](azurek8s.jpg)


**Teardown**

```bash
kubectl delete -f deploy/
```

## References

**Swagger**

https://github.com/flasgger/flasgger/blob/master/examples/apispec_example.py ( Imp )

https://swagger.io/docs/specification/2-0/describing-request-body/

https://swagger.io/docs/specification/describing-request-body/ ( v3 )

http://brunorocha.org/python/flask/flasgger-api-playground-with-flask-and-swagger-ui.html

**Docker**

https://dev.to/riverfount/dockerize-a-flask-app-17ag

