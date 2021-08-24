# InteractionRewardingAdsAndroidLib

The purpose of this library is to integrate Interaction Rewarding Ads into an existing app.
The library is written in Kotlin.

## License
The license can be found in `LICENSE`.

## Test

### Instrumented Tests
! Please Note: Some Test require an active Internet Connection. This is because an external script is tested.
Failing tests may be caused by internet connection or network security policies.

To execute instrumented test connect an Android Device to your computer or use an android emulator.

To execute all instrumented Tests:

`gradlew connectedAndroidTest`

## Deployment

The AdLib-Module is build and deployed to a nexus instance as an aar package. 
The instance can be found <a href="https://nexus.trustmeimansoftware.engineer"> here</a>.

All deployed versions can be found <a href="https://nexus.trustmeimansoftware.engineer/#browse/browse:maven-releases"> here </a>. 

### Requirements

To deploy the module, you need to set the credentials for the nexus instance.

To do this, define in the root local.properties

```
USERNAME=YOUR_ADMIN_USERNAME
PASSWORD=YOUR_PASSWORD
```

### Deploying a new version

To deploy a new version, update the `VERSION_NAME` in the root gradle.properties. 

Run `gradlew uploadArchives` to upload to the nexus instance.





