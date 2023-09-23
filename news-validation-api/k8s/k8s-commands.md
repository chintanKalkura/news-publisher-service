use powershell

```minikube start```

```minikube docker-env``` get env variables for docker in minikube cluster

```& minikube -p minikube docker-env --shell powershell | Invoke-Expression``` set docker daemon to use cluster docker daemon.

```kubectl apply -f .\deployment-news-validation.yaml``` apply deployment from file.

```kubectl get deployment```

```kubectl describe deployment\news-validation-api```

```kubectl get pods```

```kubectl describe pods```

```kubectl --help```

``` kubectl delete deployment/news-validation-api```

```kubectl apply -f .\service-news-validation-api.yaml``` expose the deployment as a service for consuming.

```kubectl get service```

```minikube service news-validation-api-svc --url``` open a tunnel to the service and show the url.