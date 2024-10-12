# Azure EventHub Producer

## Create an Event Hub

* Set environment variables

```cmd
set RESOURCE_GROUP=njkol-analytics
set EVENT_HUB_NAMESPACE=njkolhub
set EVENT_HUB_NAME=sales
set EVENT_HUB_AUTHORIZATION_RULE=salesauthorule
```

* Create an event hub

```cmd
az eventhubs eventhub create ^
    --resource-group %RESOURCE_GROUP% ^
    --name %EVENT_HUB_NAME% ^
    --namespace-name %EVENT_HUB_NAMESPACE% ^
    --message-retention 1
```

* Create an event hub authorization rule

```cmd
az eventhubs eventhub authorization-rule create ^
    --resource-group %RESOURCE_GROUP% ^
    --name %EVENT_HUB_AUTHORIZATION_RULE% ^
    --eventhub-name %EVENT_HUB_NAME% ^
    --namespace-name %EVENT_HUB_NAMESPACE% ^
    --rights Listen Send
```



## References

https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-java-get-started-send
