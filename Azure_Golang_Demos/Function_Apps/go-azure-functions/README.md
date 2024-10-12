# Azure Function in Golang

## Setup Function App on Azure

* Create the Function App on Azure

```cmd
set STORAGE_ACCOUNT=njkolbronze
set RESOURCE_GROUP=njkol-analytics
set FUNCTION_APP=njkol-golang-serverless
set LOCATION=centralindia

az functionapp create ^
    --resource-group %RESOURCE_GROUP% ^
    --name %FUNCTION_APP% ^
    --storage-account %STORAGE_ACCOUNT% ^
    --consumption-plan-location %LOCATION% ^
    --os-type linux ^
    --runtime custom ^
    --functions-version 4
```

## Create a Function App folder locally

It is is best to use VSCode to generate this.

Ref - https://www.youtube.com/watch?v=RPCEH247twU


## Build Locally

#### Cross compilation : Compile on Windows for Linux as a target

**Set environment variables**

```cmd
SET GOOS=linux
SET GOARCH=amd64
```

**Check**

```cmd
go env GOOS GOARCH
```

**Build the code**

```cmd
go build handler.go
```

## Configure

In the `host.json` file, update the value of the property `defaultExecutablePath` to that of the name of the binary file generated

```json
 "customHandler": {
    "description": {
      "defaultExecutablePath": "handler",
      "workingDirectory": "",
      "arguments": []
    },
    "enableForwardingHttpRequest": true
  }
```

## Run amd test locally

```bash
func start
```

## Deploy to Azure

```bash
func azure functionapp publish %FUNCTION_APP% 
```

## Check

https://njkol-golang-serverless.azurewebsites.net/api/go-http-trigger?name=Nilanjan

## Notes

* Every function app requires a storage account to operate

## References

https://docs.microsoft.com/en-us/azure/azure-functions/create-first-function-vs-code-other?tabs=go%2Cwindows