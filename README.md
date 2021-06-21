# StoriesKit

## Usage

Add repository
```groovy
allprojects {
    repositories {
        maven { 
            url "https://maven.pkg.github.com/eadm/StoriesKit"
            credentials {
                username = System.getenv('GITHUB_USER') ?: project.properties['GITHUB_USER']
                password = System.getenv('GITHUB_PERSONAL_ACCESS_TOKEN') ?: project.properties['GITHUB_PERSONAL_ACCESS_TOKEN']
            }
        }
    }
}
```

[Generate GitHub Token](https://github.com/settings/tokens/new) with permission `read:packages` and place it in `gradle.properties` (or root file in `~/.gradle/gradle.properties`).
```
GITHUB_USER=YOUR_GITHUB_USER_NAME
GITHUB_PERSONAL_ACCESS_TOKEN=YOUR_GITHUB_ACCESS_TOKEN
```

## Artifact

```groovy
dependencies {
    implementation 'ru.nobird.android:storieskit:1.1.2'
}
```