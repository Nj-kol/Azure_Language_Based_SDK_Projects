# Telemetry App

## Setup Function App on Azure

* Create the Function App on Azure

```bash
set STORAGE_ACCOUNT=njkolbronze
set RESOURCE_GROUP=njkol-analytics
set FUNCTION_APP=njkol-telemetry-app
set LOCATION=centralindia

az functionapp create ^
    --resource-group %RESOURCE_GROUP% ^
    --name %FUNCTION_APP% ^
    --storage-account %STORAGE_ACCOUNT% ^
    --consumption-plan-location %LOCATION% ^
    --runtime java ^
    --functions-version 4
```

* Update settings

```bash
SET AZURE_WEB_JOBS_STORAGE=DefaultEndpointsProtocol=https;EndpointSuffix=core.windows.net;AccountName=njkolbronze;AccountKey=qDSGQTiyEIChqnNs4j0TTwBZtMJCXMWGrZz6JsKoqSsucDL0+w2s+7gDPJXn7+YIV3KTZl67w3b3xwOqiERVrQ==
SET EVENT_HUB_CONNECTION_STRING=Endpoint=sb://njkolhub.servicebus.windows.net/;SharedAccessKeyName=telemetryauthorule;SharedAccessKey=q7CPh7Lz5K+o6Q+ty8Bil19kihvnK72y7yMdQOiErF4=;EntityPath=telemetry
SET COSMOS_DB_CONNECTION_STRING=AccountEndpoint=https://njkol-cosmosdb-core-sql.documents.azure.com:443/;AccountKey=gjeI2ZjTpuv33PO4GO6W2a7nGXZT4YMNG2IEjdRSzDpXP9gmMcsRI2a02e6GSYvjScTmdDbgKI0qkXcdyyRLWA==;

az functionapp config appsettings set ^
    --resource-group %RESOURCE_GROUP% ^
    --name %FUNCTION_APP% ^
    --settings ^
        AzureWebJobsStorage=%AZURE_WEB_JOBS_STORAGE% ^
        EventHubConnectionString=%EVENT_HUB_CONNECTION_STRING% ^
        CosmosDBConnectionString=%COSMOS_DB_CONNECTION_STRING%
```

## Generate a Maven project to house the function code

```bash
mvn archetype:generate --batch-mode ^
    -DarchetypeGroupId=com.microsoft.azure ^
    -DarchetypeArtifactId=azure-functions-archetype ^
    -DappName=%FUNCTION_APP% ^
    -DresourceGroup=%RESOURCE_GROUP% ^
    -DappRegion=%LOCATION% ^
    -DgroupId=com.njkol ^
    -DartifactId=telemetry-functions
```

## Autogenerate the local.settings.json file for local testing

```bash
# Go the the project root
cd D:\Repos\Java\Azure\telemetry-functions

func azure functionapp fetch-app-settings %FUNCTION_APP%
```

This will auto populate the `local.settings.json` file. This will only work if the settings have been updated properly on the Azure Function App

## Run Locally

```bash
mvn clean package

mvn azure-functions:run
```

## Deploy to Azure

```bash
mvn azure-functions:deploy
```

## Notes

* Every function app requires a storage account to operate

## References

https://docs.microsoft.com/en-us/azure/azure-functions/functions-event-hub-cosmos-db?tabs=cmd